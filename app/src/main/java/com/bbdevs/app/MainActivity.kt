package com.bbdevs.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.bbdevs.app.entity.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
            val reward = enterReward.text.toString()
            val deadline = enterDeadline.text.toString()
            db.addTask(Task(name, description, reward, deadline))
            Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
            enterName.text.clear()
            enterDescription.text.clear()
            enterReward.text.clear()
            enterDeadline.text.clear()
        }

        printTask.setOnClickListener{
            val tasks = db.getTasks()
            for (task in tasks) {
                Name.append("${task.name}\n")
                Description.append("${task.description}\n")
                Reward.append("${task.reward}\n")
                Deadline.append("${task.deadline}\n")
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
}