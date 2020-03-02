package com.github.heesung6701.timepicker.publisher

import com.github.heesung6701.timepicker.library.interfaces.Observer
import com.github.heesung6701.timepicker.library.interfaces.Publisher
import com.github.heesung6701.timepicker.library.model.TimeItem

class TimeItemArrayPublisher(size: Int, init: (Int) -> TimeItem) : Publisher<Array<TimeItem>>,
    Observer<TimeItem> {
    private val array = Array<TimeItem>(size) { init(it) }
    private val observers = ArrayList<Observer<Array<TimeItem>>>()

    init {
        array.forEach {
            it.add(this)
        }
    }

    override fun update(item: TimeItem) {
        notifyObserver()
    }

    override fun notifyObserver() {
        observers.forEach { it ->
            it.update(array)
        }
    }

    override fun add(observer: Observer<Array<TimeItem>>) {
        observers.add(observer)
    }

    override fun delete(observer: Observer<Array<TimeItem>>) {
        observers.remove(observer)
    }

    fun toList(): List<TimeItem> {
        return array.toList()
    }

    operator fun set(index: Int, element: TimeItem) {
        array[index].delete(this)
        array[index] = element
        array[index].add(this)
        notifyObserver()
    }

    operator fun get(index: Int): TimeItem {
        return array[index]
    }

}