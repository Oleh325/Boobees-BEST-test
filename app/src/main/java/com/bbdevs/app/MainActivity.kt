package com.bbdevs.app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bbdevs.app.entity.Task
import com.bbdevs.app.util.DateTimeUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var pickedDeadline: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = CatAppDB(this, null)
        setListeners(db, addTask, printTask, addUserInfo, printUserInfo)
    }

    @SuppressLint("Range")
    private fun setListeners(db: CatAppDB, addTask: View, printTask: View, addUserInfo: View, printUserInfo: View) {
        addTask.setOnClickListener {
            val name = enterName.text.toString()
            val description = enterDescription.text.toString()
            val reward = enterReward.text.toString().toInt()
            val deadline = pickedDeadline
            if (name.isEmpty()) {
                Toast.makeText(this, "Task name or reward cannot be empty!", Toast.LENGTH_LONG).show()
            }
            db.addTask(Task(name, description, reward, deadline, isCompleted = false))
            Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
            enterName.text.clear()
            enterDescription.text.clear()
            enterReward.text.clear()
        }

        addDeadline.setOnClickListener {
            pickDate()
        }

        printTask.setOnClickListener{
            val tasks = db.getTasks()
            for (task in tasks) {
                Name.append("${task.name}\n")
                Description.append("${task.description}\n")
                Reward.append("${task.reward}\n")
                Deadline.append("${DateTimeUtil.toHumanReadableFormat(task.deadline)}\n")
            }
        }

        addUserInfo.setOnClickListener{
            val balance = enterBalance.text.toString()
            val catHealth = enterCatHealth.text.toString()
            db.addUserInfo(balance, catHealth)
            Toast.makeText(this, "userInfo added to database", Toast.LENGTH_LONG).show()
            enterBalance.text.clear()
            enterCatHealth.text.clear()
        }

        printUserInfo.setOnClickListener{
            val cursor = db.getUserInfo()
            cursor!!.moveToFirst()
            Balance.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.BALANCE))}\n")
            CatHealth.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.CAT_HEALTH))}\n")
            while(cursor.moveToNext()){
                Balance.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.BALANCE))}\n")
                CatHealth.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.CAT_HEALTH))}\n")
            }
            cursor.close()
        }
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        pickedDeadline = LocalDateTime.of(LocalDate.of(year, month, dayOfMonth), LocalTime.now())
        val cal = Calendar.getInstance()
        TimePickerDialog(this, this,
            cal.get(Calendar.HOUR),
            cal.get(Calendar.MINUTE),
            true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        pickedDeadline = LocalDateTime.of(pickedDeadline.toLocalDate(), LocalTime.of(hourOfDay, minute))
    }
}