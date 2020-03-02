package com.github.heesung6701.timepicker.library

import android.content.Context
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.github.heesung6701.timepicker.library.model.TimeItem
import com.github.heesung6701.timepicker.publisher.TimeItemArrayPublisher
import com.github.heesung6701.timepicker.library.interfaces.Observer
import com.github.heesung6701.timepicker.library.options.Option
import com.github.heesung6701.timepicker.library.options.OptionBuilder
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.properties.Delegates

class TimePickerView : View, Observer<Array<TimeItem>> {
    val options: Option
    private val mRect = Rect()
    private val mPaint = Paint()
    private val calc = TimePickerCalculator()
    var timeItemArray by Delegates.notNull<TimeItemArrayPublisher>()
    private var currentDir = 0
    private var mCenterPosX: Float = 0.0f
    private var mCenterPosY: Float = 0.0f
    private var mRadius: Float = 0.0f
    private var timeTextSize: Float = 0.0f
    private var timeBackgroundRadius: Float = 0.0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        with(context.obtainStyledAttributes(attrs, R.styleable.TimePickerView)) {
            val clockSize = getInt(R.styleable.TimePickerView_clockSize, 24)
            val clockPadding = getFloat(R.styleable.TimePickerView_clockPadding, 50.0f)
            val timeTextColor = getColor(R.styleable.TimePickerView_timeTextColor, Color.GRAY)
            val timeSelectedTextColor =
                getColor(R.styleable.TimePickerView_timeSelectedTextColor, Color.WHITE)
            val timeBackgroundColor =
                getColor(R.styleable.TimePickerView_timeBackgroundColor, Color.GRAY)
            val clockBorderColor = getColor(R.styleable.TimePickerView_clockBorderColor, Color.GRAY)
            val clockColor = getColor(R.styleable.TimePickerView_clockColor, Color.WHITE)
            val centerImage = getResourceId(R.styleable.TimePickerView_centerImage, 0)
            val centerImageHeight =
                getDimension(R.styleable.TimePickerView_centerImageHeight, 200.0f)
            val centerImageWidth = getDimension(R.styleable.TimePickerView_centerImageWidth, 200.0f)
            options = OptionBuilder().clockSize(clockSize).clockPadding(clockPadding)
                .timeTextColor(timeTextColor).timeSelectedTextColor(timeSelectedTextColor)
                .timeBackgroundColor(timeBackgroundColor).clockBorderColor(clockBorderColor)
                .clockColor(clockColor).centerImage(centerImage)
                .centerImageHeight(centerImageHeight).centerImageWidth(centerImageWidth).build()
        }
        timeItemArray = TimeItemArrayPublisher(
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
        drawCenterImage(canvas)
        drawTimeBackground(canvas)
        drawTime(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initCalculate(height = height.toFloat(), width = width.toFloat())
    }

    private fun initCalculate(height: Float, width: Float) {
        timeTextSize = height / 60
        timeBackgroundRadius = timeTextSize * 1.5f
        val timePadding = timeBackgroundRadius * 1.5f

        mCenterPosY = height / 2
        mCenterPosX = width / 2
        val minAttr = min(height / 2, width / 2)
        mRadius = minAttr - options.clockPadding
        for (hour in options.mClockHours) {
            val angle = 2 * Math.PI / options.clockSize * (hour - options.clockSize / 4.0f)
            options.timeAngle[hour] = angle.toFloat()
            val x = (mCenterPosX + cos(angle) * (mRadius - timePadding)).toFloat()
            options.timePosX[hour] = x
            val y = (mCenterPosY + sin(angle) * (mRadius - timePadding)).toFloat()
            options.timePosY[hour] = y
        }
    }

    private var prevHour = -1
    private fun checkWhatItemClicked(touchX: Float, touchY: Float): Boolean {
        val touchRadius = calc.distance(mCenterPosX, mCenterPosY, touchX, touchY)
        if (touchRadius >= mRadius) {
            initPrevHour()
            return false
        }
        if (touchRadius <= options.touchMinRadius) {
            initPrevHour()
            return false
        }
        val hour = calc.samplingHour(
            touchX = touchX,
            touchY = touchY,
            mCenterPosX = mCenterPosX,
            mCenterPosY = mCenterPosY,
            clockSize = options.clockSize
        )
        if (prevHour == hour) return true
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
                    interpolationHour = (interpolationHour + 1) % options.clockSize
                    clickTimeItem(interpolationHour)
                }
            }
            else -> clickTimeItem(hour)
        }
        prevHour = hour
        return true
    }

    private fun clickTimeItem(pos: Int) {
        timeItemArray[pos].selected = !timeItemArray[pos].selected
    }

    private fun initPrevHour() {
        isCancel = true
        prevHour = -1
        currentDir = 0
    }

    private fun drawCenterImage(canvas: Canvas) {
        if (options.centerImage == 0) return
        val vectorDrawableSearch = VectorDrawableCompat.create(resources, options.centerImage, null)
        val startX = calc.startPos(mCenterPosX, options.centerImageWidth).toInt()
        val startY = calc.startPos(mCenterPosY, options.centerImageHeight).toInt()
        vectorDrawableSearch?.setBounds(
            startX,
            startY,
            startX + options.centerImageWidth.toInt(),
            startY + options.centerImageHeight.toInt()
        )
        vectorDrawableSearch?.draw(canvas)
    }

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
            if (!timeItemArray[hour].selected) continue
            canvas.drawCircle(
                options.timePosX[hour], options.timePosY[hour], timeBackgroundRadius, mPaint
            )
        }
    }

    private fun drawTime(canvas: Canvas) {
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, timeTextSize, resources.displayMetrics
        )
        mPaint.apply {
            reset()
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3.0f
            textSize = fontSize
        }
        for (hourInt in options.mClockHours) {
            val hour = hourInt.toString()
            mPaint.color =
                if (timeItemArray[hourInt].selected) options.timeSelectedTextColor else options.timeTextColor
            mPaint.getTextBounds(hour, 0, hour.length, mRect)
            val x = options.timePosX[hourInt] - mRect.exactCenterX()
            val y = options.timePosY[hourInt] - mRect.exactCenterY()
            canvas.drawText(hour, x, y, mPaint)
        }
    }
}
