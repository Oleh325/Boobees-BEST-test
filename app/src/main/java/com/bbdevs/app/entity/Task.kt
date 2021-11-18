package com.bbdevs.app.entity

import java.time.LocalDateTime

data class Task(
    val name: String,
    val description: String,
    val reward: Int,
    val deadline: LocalDateTime,
    val isCompleted: Boolean
)
