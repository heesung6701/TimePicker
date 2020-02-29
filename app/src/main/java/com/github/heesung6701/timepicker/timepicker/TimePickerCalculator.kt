package com.github.heesung6701.timepicker.timepicker

class TimePickerCalculator {
    val startPos: (pos: Float, size: Float) -> Float =
        { pos: Float, size: Float -> pos - size / 2 }
    val calculateDistanceSquare: (x1: Float, y1: Float, x2: Float, y2: Float) -> Double =
        { x1, y1, x2, y2 -> ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble() }
    val angleToHour: (angle: Float, size: Int) -> Int = { angle: Float, size: Int ->
        (angle / (360 / size)).toInt()
        // angle to hour, scale to 0 hour is 0 degree
    }
    val calculateAngle: (x1: Float, y1: Float, x2: Float, y2: Float) -> Float =
        { x1: Float, y1: Float, x2: Float, y2: Float ->
            ((kotlin.math.atan2(
                (y2 - y1).toDouble(),
                (x2 - x1).toDouble()
            ) + Math.PI) * 180 / Math.PI).toFloat()
            // angle of two point is between -Pi and Pi
            // by add 2 Pi, It always become on positive
            // Because it is not point but area, add some offset that is half of one hour area
            // radian (pi) -> Degree(360)
        }
}