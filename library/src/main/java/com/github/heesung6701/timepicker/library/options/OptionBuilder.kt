package com.github.heesung6701.timepicker.library.options

class OptionBuilder {
    private var clockSize: Int = 0
    private var clockPadding: Float = 0.0f
    private var timeTextColor: Int = 0
    private var timeSelectedTextColor: Int = 0
    private var timeBackgroundColor: Int = 0
    private var clockBorderColor: Int = 0
    private var clockBorderWidth: Float = 0.0f
    private var clockColor: Int = 0
    private var centerImage: Int = 0
    private var centerImageWidth: Float = 0.0f
    private var centerImageHeight: Float = 0.0f

    fun clockSize(clockSize: Int): OptionBuilder {
        this.clockSize = clockSize
        return this
    }

    fun clockPadding(clockPadding: Float): OptionBuilder {
        this.clockPadding = clockPadding
        return this
    }

    fun timeTextColor(color: Int): OptionBuilder {
        this.timeTextColor = color
        return this
    }

    fun timeSelectedTextColor(color: Int): OptionBuilder {
        this.timeSelectedTextColor = color
        return this
    }

    fun timeBackgroundColor(color: Int): OptionBuilder {
        this.timeBackgroundColor = color
        return this
    }

    fun clockBorderColor(color: Int): OptionBuilder {
        this.clockBorderColor = color
        return this
    }

    fun clockBorderWidth(width: Float): OptionBuilder {
        this.clockBorderWidth = width
        return this
    }

    fun clockColor(color: Int): OptionBuilder {
        this.clockColor = color
        return this
    }

    fun centerImage(image: Int): OptionBuilder {
        this.centerImage = image
        return this
    }

    fun centerImageWidth(width: Float): OptionBuilder {
        this.centerImageWidth = width
        return this
    }

    fun centerImageHeight(height: Float): OptionBuilder {
        this.centerImageHeight = height
        return this
    }

    fun build(): Option {
        return Option(
            clockSize = clockSize,
            clockPadding = clockPadding,
            timeTextColor = timeTextColor,
            timeSelectedTextColor = timeSelectedTextColor,
            timeBackgroundColor = timeBackgroundColor,
            clockBorderColor = clockBorderColor,
            clockBorderWidth = clockBorderWidth,
            clockColor = clockColor,
            centerImage = centerImage,
            centerImageHeight = centerImageHeight,
            centerImageWidth = centerImageWidth
        )
    }
}