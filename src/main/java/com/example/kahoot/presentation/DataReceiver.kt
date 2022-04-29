package com.example.kahoot.presentation

interface DataReceiver<T> {
    fun getData(data: T)
}