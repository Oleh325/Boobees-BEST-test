package com.bbdevs.app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bbdevs.app.entity.UserInfo
import com.bbdevs.app.service.UserInfoService
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = CatAppDB(this, null)
        val userInfoService = UserInfoService(db)

        addTask.setOnClickListener(){
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        addUserInfo.setOnClickListener{
            val balance = enterBalance.text.toString().toInt()
            val catHealth = enterCatHealth.text.toString().toInt()
            userInfoService.update(UserInfo(1, balance, catHealth))
            Toast.makeText(this, "userInfo added to database", Toast.LENGTH_LONG).show()
            enterBalance.text.clear()
            enterCatHealth.text.clear()
        }

        printUserInfo.setOnClickListener{
            val userInfo = userInfoService.get()
            Balance.append("${userInfo.balance}\n")
            CatHealth.append("${userInfo.catHealth}\n")
        }
    }
}