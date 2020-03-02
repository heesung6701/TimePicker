package com.github.heesung6701.timepicker.library.interfaces

interface Publisher<T> {
    fun add(observer: Observer<T>)
    fun delete(observer: Observer<T>)
    fun notifyObserver()
}