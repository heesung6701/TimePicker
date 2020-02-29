package com.github.heesung6701.timepicker.timepicker._deprecated

class TimePick {
    private val hourToBit: (hour: Int) -> Int = { 1 shl it }
    private var selectData: Int = 0

    private fun getData(): Int = selectData

    fun toggleTime(hour: Int, onResult: (data: Int) -> Unit) {
        selectData = selectData xor hourToBit(hour)
        if (countSelectTime(
                getData()
            ) > MAX_SELECT_TIME
        ) {
            selectData = selectData xor hourToBit(hour)
            onResult(-1)
        } else {
            onResult(getData())
        }
    }

    override fun toString(): String = "TimePick{ selectData : $selectData},"

    companion object {

        const val CNT_CLOCK_TIME = 24
        const val TIME_LUNCH = 12
        const val TIME_DINNER = 18
        const val TIME_NIGHT = 20

        const val MAX_SELECT_TIME = 10

        fun countSelectTime(data: Int): Int {
            var cnt = 0
            var bit = 1
            for (i in 0 until CNT_CLOCK_TIME) {
                if ((bit and data) > 0) {
                    cnt++
                }
                bit = bit shl 1
            }
            return cnt
        }
    }
}
