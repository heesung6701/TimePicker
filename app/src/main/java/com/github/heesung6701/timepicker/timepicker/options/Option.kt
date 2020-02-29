package com.github.heesung6701.timepicker.timepicker.options

data class Option(
    val clockSize: Int,
    val clockPadding: Float,
    val timePadding: Float,
    val timeTextColor: Int,
    val timeSelectedTextColor: Int,
    val timeBackgroundColor: Int,
    val timeTextSize: Float,
    val clockBorderColor: Int,
    val clockColor: Int
) {
    val offsetAngle
        get() = 180.0f / clockSize.toFloat()
    val mClockHours = IntArray(clockSize) { it }
    val timePosX = FloatArray(clockSize)
    val timePosY = FloatArray(clockSize)
    val timeAngle = FloatArray(clockSize)
}