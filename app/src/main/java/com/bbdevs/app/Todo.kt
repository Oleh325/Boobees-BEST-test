package com.bbdevs.app

data class Todo(
    val name: String,
    val reward: Int,
    var isChecked: Boolean = false
)