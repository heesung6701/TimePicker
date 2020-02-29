package com.github.heesung6701.timepicker.timepicker

import android.content.Context
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.github.heesung6701.timepicker.R
import com.github.heesung6701.timepicker.timepicker.model.TimeItem
import com.github.heesung6701.timepicker.timepicker.publisher.TimeItemArrayPublisher
import com.github.heesung6701.timepicker.timepicker.interfaces.Observer
import com.github.heesung6701.timepicker.timepicker.options.Option
import com.github.heesung6701.timepicker.timepicker.options.OptionBuilder
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.properties.Delegates

class TimePickerView : View,
    Observer<Array<TimeItem>> {

    private val mRect = Rect()
    private val mPaint = Paint()
    private val calc = TimePickerCalculator()
    private val options: Option

    var timeItemArray by Delegates.notNull<TimeItemArrayPublisher>()

    private var currentDir = 0

    private var mCenterPosX: Float = 0.0f
    private var mCenterPosY: Float = 0.0f
    private var mRadius: Float = 0.0f


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        with(context.obtainStyledAttributes(attrs, R.styleable.time_picker)) {
            val clockSize = getInt(R.styleable.time_picker_clockSize, 24)
            val clockPadding = getFloat(R.styleable.time_picker_clockPadding, 50.0f)
            val timePadding = getFloat(R.styleable.time_picker_timePadding, 50.0f)
            val timeTextColor = getColor(R.styleable.time_picker_timeTextColor, Color.GRAY)
            val timeSelectedTextColor = getColor(R.styleable.time_picker_timeSelectedTextColor, Color.WHITE)
            val timeBackgroundColor = getColor(R.styleable.time_picker_timeBackgroundColor, Color.GRAY)
            val timeTextSize = getDimension(R.styleable.time_picker_timeTextSize, 0.0f)
            val clockBorderColor = getColor(R.styleable.time_picker_clockBorderColor, Color.GRAY)
            val clockColor = getColor(R.styleable.time_picker_clockColor, Color.WHITE)
            options = OptionBuilder()
                .clockSize(clockSize)
                .clockPadding(clockPadding)
                .timePadding(timePadding)
                .timeTextColor(timeTextColor)
                .timeSelectedTextColor(timeSelectedTextColor)
                .timeBackgroundColor(timeBackgroundColor)
                .timeTextSize(timeTextSize)
                .clockBorderColor(clockBorderColor)
                .clockColor(clockColor)
                .build()
        }
        timeItemArray =
            TimeItemArrayPublisher(
                options.clockSize
            ) { TimeItem.makeDefault() }
        timeItemArray.add(this)

    }

    override fun update(item: Array<TimeItem>) {
        invalidate()
    }

    private var isCancel = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isCancel = false
                return checkWhatItemClicked(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isCancel) return false
                return checkWhatItemClicked(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                initPrevHour()
            }
            else -> {
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClock(canvas)
//        drawSearchIcon(canvas)
        drawTimeBackground(canvas)
        drawTime(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initCalculate()
    }

    private fun initCalculate() {
        mCenterPosY = height.toFloat() / 2
        mCenterPosX = width.toFloat() / 2
        val minAttr = min(height / 2, width / 2)
        mRadius = minAttr - options.clockPadding
        for (hour in options.mClockHours) {
            val angle = 2 * Math.PI / options.clockSize * (hour - options.clockSize / 4.0f)
            options.timeAngle[hour] = angle.toFloat()
            val x = (mCenterPosX + cos(angle) * (mRadius - options.timePadding)).toFloat()
            options.timePosX[hour] = x
            val y = (mCenterPosY + sin(angle) * (mRadius - options.timePadding)).toFloat()
            options.timePosY[hour] = y
        }
    }

    private var prevHour = -1
    private fun checkWhatItemClicked(touchX: Float, touchY: Float): Boolean {
        if (calc.calculateDistanceSquare(
                mCenterPosX,
                mCenterPosY,
                touchX,
                touchY
            ) > MIN_DISTANCE_FROM_CENTER * MIN_DISTANCE_FROM_CENTER
        ) {
            val hour = samplingHour(touchX, touchY)
//            LogDebug.d(LOG_CANVAS, "hour: $hour")
            if (prevHour != hour) {
                if (currentDir != 0 && prevHour - currentDir == hour) {
                    clickTimeItem(hour + currentDir)
                }
                currentDir = when {
                        prevHour == -1 -> 0
                        hour - prevHour > options.clockSize / 2 -> -1
                        hour - prevHour < -options.clockSize / 2 -> 1
                        hour < prevHour -> -1
                        prevHour < hour -> 1
                    else -> 0
                }
//                LogDebug.d(LOG_CANVAS, "currentDir: $currentDir")
                when {
                    prevHour == -1 -> clickTimeItem(hour)
                    currentDir == -1 -> {
                        var interpolationHour = prevHour
                        while (interpolationHour != hour) {
                            interpolationHour -= 1
                            if (interpolationHour < 0) interpolationHour += options.clockSize
                            clickTimeItem(interpolationHour)
                        }
                    }
                    currentDir == 1 -> {
                        var interpolationHour = prevHour
                        while (interpolationHour != hour) {
                            interpolationHour += 1
                            if (interpolationHour >= options.clockSize) interpolationHour -=  options.clockSize
                            clickTimeItem(interpolationHour)
                        }
                    }
                    else -> clickTimeItem(hour)
                }
                prevHour = hour
            }
            return true
        } else {
            initPrevHour()
//            LogDebug.d(LOG_CANVAS, "too close to the Origin")
            return false
        }
    }

    private fun clickTimeItem(pos: Int) {
        timeItemArray[pos].selected = !timeItemArray[pos].selected;
    }

    private fun initPrevHour() {
        isCancel = true
        prevHour = -1
        currentDir = 0
    }

    private fun samplingHour(touchX: Float, touchY: Float): Int {
        var angle =
            calc.calculateAngle(mCenterPosX, mCenterPosY, touchX, touchY) - 90 + options.offsetAngle
        if (angle < 0) {
            angle += 360
        }
        return calc.angleToHour(angle, options.clockSize)
    }

//    private fun drawSearchIcon(canvas: Canvas) {
//        val vectorDrawableSearch =
//            VectorDrawableCompat.create(resources, R.drawable.ic_search, null)
//        val startX = startPos(mWidth / 2, WIDTH_SEARCH_IMAGE.toFloat()).toInt()
//        val startY = startPos(mHeight / 2, HEIGHT_SEARCH_IMAGE.toFloat()).toInt()
//        vectorDrawableSearch?.setBounds(
//            startX,
//            startY,
//            startX + WIDTH_SEARCH_IMAGE,
//            startY + HEIGHT_SEARCH_IMAGE
//        )
//        vectorDrawableSearch?.draw(canvas)
//    }

    private fun drawClock(canvas: Canvas) {
        mPaint.apply {
            reset()
            color = options.clockColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(mCenterPosX, mCenterPosY, mRadius, mPaint)
        mPaint.apply {
            reset()
            color = options.clockBorderColor
            style = Paint.Style.STROKE
            strokeWidth = 4.0f
            isAntiAlias = true
        }
        canvas.drawCircle(mCenterPosX, mCenterPosY, mRadius, mPaint)
    }

    private fun drawTimeBackground(canvas: Canvas) {
        mPaint.apply {
            reset()
            style = Paint.Style.FILL
            color = options.timeBackgroundColor
            isAntiAlias = true
        }
        for (hour in options.mClockHours) {
            if (timeItemArray[hour].selected) {
                canvas.drawCircle(
                    options.timePosX[hour], options.timePosY[hour],
                    RADIUS_TIME_BACKGROUND, mPaint
                )
            }
        }
    }

    private fun drawTime(canvas: Canvas) {
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            options.timeTextSize, resources.displayMetrics
        )
        mPaint.apply {
            reset()
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3.0f
            textSize = fontSize
        }
        for (hourInt in options.mClockHours) {
            val hour = hourInt.toString()
            mPaint.color = if (timeItemArray[hourInt].selected) options.timeSelectedTextColor else options.timeTextColor
            mPaint.getTextBounds(hour, 0, hour.length, mRect)
            val x = options.timePosX[hourInt] - mRect.exactCenterX()
            val y = options.timePosY[hourInt] - mRect.exactCenterY()
            canvas.drawText(hour, x, y, mPaint)
        }
    }

    companion object {
        private const val MIN_DISTANCE_FROM_CENTER = 141.4
        private const val RADIUS_TIME_BACKGROUND = 50.0f
        const val WIDTH_SEARCH_IMAGE = 200
        const val HEIGHT_SEARCH_IMAGE = 200
    }
}
