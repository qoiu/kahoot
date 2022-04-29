package com.example.kahoot

interface Mapper {
    interface Base<Input,Output>: Mapper{
        fun map(data: Input) : Output
    }
}