package com.bbdevs.app.service

import com.bbdevs.app.CatAppDB
import com.bbdevs.app.entity.Task

class TaskService(private val db: CatAppDB) {

    fun get(): List<Task> {
        return db.getTasks()
    }

    fun add(task: Task) {
        db.addTask(task)
    }

    fun update(task: Task) {
        db.updateTask(task)
    }

}