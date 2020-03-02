package com.github.heesung6701.timepicker.library.model

import com.github.heesung6701.timepicker.library.interfaces.Observer
import com.github.heesung6701.timepicker.library.interfaces.Publisher

class TimeItem(selected: Boolean) : Publisher<TimeItem> {
    var selected: Boolean = selected
        set(value) {
            field = value
            notifyObserver()
        }
    private val observers = ArrayList<Observer<TimeItem>>()

    override fun add(observer: Observer<TimeItem>) {
        observers.add(observer)
    }

    override fun delete(observer: Observer<TimeItem>) {
        observers.remove(observer)
    }

    override fun notifyObserver() {
        observers.forEach {
            it.update(this)
        }
    }

    companion object {
        fun make(selected: Boolean): TimeItem {
            return TimeItem(selected)
        }

        fun makeDefault(): TimeItem {
            return TimeItem(false)
        }
    }
}