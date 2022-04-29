package com.example.kahoot.data.executors

import javax.validation.constraints.Null

interface DbExecutor<Input, Output> {

    interface Base<Input, Output> : DbExecutor<Input, Output> {
        fun execute(objects: Input): Output

    }

    interface All<Output> : DbExecutor<Null, Output> {
        fun execute(): Output
    }

    interface Get<Output> : DbExecutor<Long, Output> {
        fun execute(id: Long): Output
    }
}