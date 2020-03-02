package com.github.heesung6701.timepicker.library.options

data class Option(val clockSize: Int,
                  val clockPadding: Float,
                  var timeTextColor: Int,
                  var timeSelectedTextColor: Int,
                  var timeBackgroundColor: Int,
                  var clockBorderColor: Int,
                  var clockColor: Int,
                  var centerImage: Int,
                  var centerImageWidth: Int,
                  var centerImageHeight: Int
) {
    val mClockHours = IntArray(clockSize) { it }
    val timePosX = FloatArray(clockSize)
    val timePosY = FloatArray(clockSize)
    val timeAngle = FloatArray(clockSize)
    private val allowArc = 0.5f
    val touchMinRadius = (allowArc * 6 * clockSize / Math.PI).toFloat()
}