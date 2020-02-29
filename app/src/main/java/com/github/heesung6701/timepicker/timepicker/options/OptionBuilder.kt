package com.github.heesung6701.timepicker.timepicker.options

class OptionBuilder {
    var clockSize: Int = 0
    var clockPadding: Float = 0.0f
    var timePadding: Float = 0.0f
    var timeTextColor: Int = 0
    var timeSelectedTextColor: Int = 0
    var timeBackgroundColor: Int = 0
    var timeTextSize: Float = 0.0f
    var clockBorderColor: Int = 0
    var clockColor:Int = 0

    fun clockSize(clockSize: Int): OptionBuilder {
        this.clockSize = clockSize
        return this
    }

    fun clockPadding(clockPadding: Float): OptionBuilder {
        this.clockPadding = clockPadding
        return this
    }

    fun timePadding(timePadding: Float): OptionBuilder {
        this.timePadding = timePadding
        return this
    }

    fun timeTextColor(color: Int) : OptionBuilder {
        this.timeTextColor = color
        return this
    }

    fun timeSelectedTextColor(color: Int) : OptionBuilder {
        this.timeSelectedTextColor = color
        return this
    }

    fun timeBackgroundColor(color: Int) : OptionBuilder {
        this.timeBackgroundColor = color
        return this
    }

    fun timeTextSize(size: Float) : OptionBuilder {
        this.timeTextSize = size
        return this
    }

    fun clockBorderColor(color: Int) : OptionBuilder {
        this.clockBorderColor = color
        return this
    }

    fun clockColor(color:Int) : OptionBuilder {
        this.clockColor = color
        return this
    }

    fun build(): Option {
        return Option(
            clockSize = clockSize,
            clockPadding = clockPadding,
            timePadding = timePadding,
            timeTextColor = timeTextColor,
            timeSelectedTextColor = timeSelectedTextColor,
            timeBackgroundColor = timeBackgroundColor,
            timeTextSize = timeTextSize,
            clockBorderColor = clockBorderColor,
            clockColor = clockColor
        )
    }
}