package com.github.heesung6701.timepicker.library

import com.github.heesung6701.timepicker.library.model.TimeItem
import com.github.heesung6701.timepicker.library.interfaces.Observer
import com.github.heesung6701.timepicker.publisher.TimeItemArrayPublisher
import org.junit.Assert.assertEquals
import org.junit.Test

class ObservableArrayTest {
    @Test
    fun initTest() {
        val array = TimeItemArrayPublisher(
            5
        ) { TimeItem.makeDefault() }
        assertEquals(
            BooleanArray(5) { false }.toList().toString(),
            array.toList().map { it -> it.selected }.toString()
        )
    }

    @Test
    fun setTest() {
        val array = TimeItemArrayPublisher(
            5
        ) { TimeItem.makeDefault() }
        array[0].selected = true
        assertEquals(
            listOf(true, false, false, false, false).toString(),
            array.toList().map { it -> it.selected }.toString()
        )
    }

    @Test
    fun getTest() {
        val array = TimeItemArrayPublisher(
            5
        ) { TimeItem.makeDefault() }
        array[1].selected = true
        assertEquals(true, array[1].selected)
    }

    @Test
    fun observeItemTest() {
        var test = false
        val observer = object : Observer<Array<TimeItem>> {
            override fun update(item: Array<TimeItem>) {
                test = true
            }
        }
        val array = TimeItemArrayPublisher(
            5
        ) { TimeItem.makeDefault() }
        array.add(observer)
        array[1].selected = true
        assertEquals(true, test)
    }
}