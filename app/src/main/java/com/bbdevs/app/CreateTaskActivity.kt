package com.bbdevs.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import com.bbdevs.app.entity.Task
import com.bbdevs.app.service.TaskService
import kotlinx.android.synthetic.main.activity_create_task.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class CreateTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var pickedDeadline: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_create_task)
        val db = CatAppDB(this, null)
        val taskService = TaskService(db)
        setListeners(taskService)
        addDeadline.setOnClickListener {
            pickDate()
        }
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            create_task_background_container.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
        create_task_border.alpha = 0f
        create_task_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
        cancel_button.setOnClickListener {
            colorAnimation.duration = 500 // milliseconds
            colorAnimation.addUpdateListener { animator ->
                create_task_background_container.setBackgroundColor(
                    animator.animatedValue as Int
                )
            }

            create_task_border.animate().alpha(0f).setDuration(500).setInterpolator(
                DecelerateInterpolator()
            ).start()

            colorAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    finish()
                    overridePendingTransition(0, 0)
                }
            })
            colorAnimation.start()
        }
        create_button.setOnClickListener {
            colorAnimation.duration = 500 // milliseconds
            colorAnimation.addUpdateListener { animator ->
                create_task_background_container.setBackgroundColor(
                    animator.animatedValue as Int
                )
            }

            create_task_border.animate().alpha(0f).setDuration(500).setInterpolator(
                DecelerateInterpolator()
            ).start()

            colorAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    finish()
                    overridePendingTransition(0, 0)
                }
            })
            colorAnimation.start()

        }
    }

    @SuppressLint("Range")
    private fun setListeners(taskService: TaskService) {
        create_button.setOnClickListener {
            val name = enterName.text.toString()
            val description = enterDescription.text.toString()
            val reward = enterReward.text.toString().toInt()
            val deadline = pickedDeadline
            if (name.isEmpty()) {
                Toast.makeText(this, "Task name or reward cannot be empty!", Toast.LENGTH_LONG)
                    .show()
            }
            taskService.add(Task(null, name, description, reward, deadline, isCompleted = false))
            Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
            enterName.text.clear()
            enterDescription.text.clear()
            enterReward.text.clear()
        }
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        pickedDeadline = LocalDateTime.of(LocalDate.of(year, month, dayOfMonth), LocalTime.now())
        val cal = Calendar.getInstance()
        TimePickerDialog(this, this,
            cal.get(Calendar.HOUR),
            cal.get(Calendar.MINUTE),
            true).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        pickedDeadline = LocalDateTime.of(pickedDeadline.toLocalDate(), LocalTime.of(hourOfDay, minute))
    }
}