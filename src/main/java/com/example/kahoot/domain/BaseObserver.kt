package com.example.kahoot.domain

interface SimpleObserver<T>{
    fun subscribe(observer: T)
    fun unsubscribe(observer: T)
}

abstract class BaseObserver<T>: SimpleObserver<T> {

    protected val receiverList = mutableListOf<T>()

    override fun subscribe(observer: T){
        receiverList.add(observer)
    }

    override fun unsubscribe(observer: T){
        receiverList.remove(observer)
    }
}