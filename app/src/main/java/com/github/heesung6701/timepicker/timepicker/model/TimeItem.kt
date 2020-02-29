package com.github.heesung6701.timepicker.timepicker.model

import com.github.heesung6701.timepicker.timepicker.interfaces.Observer
import com.github.heesung6701.timepicker.timepicker.interfaces.Publisher

class TimeItem :
    Publisher<TimeItem> {
    var selected: Boolean
        set(value) {
            field = value
            notifyObserver()
        }

    private val observers = ArrayList<Observer<TimeItem>>()

    constructor(selected: Boolean){
        this.selected = selected
    }


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
        fun makeDefault() : TimeItem{
            return TimeItem(false)
        }
    }
}