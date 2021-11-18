package com.bbdevs.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addTask.setOnClickListener{
            val db = CatAppDB(this, null)
            val name = enterName.text.toString()
            val description = enterDescription.text.toString()
            val reward = enterReward.text.toString()
            val deadline = enterDeadline.text.toString()
            db.addTask(name, description, reward, deadline)
            Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
            enterName.text.clear()
            enterDescription.text.clear()
            enterReward.text.clear()
            enterDeadline.text.clear()
        }

        printTask.setOnClickListener{
            val db = CatAppDB(this, null)
            val cursor = db.getTask()
            cursor!!.moveToFirst()
            Name.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.NAME))}\n")
            Description.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.DESCRIPTION))}\n")
            Reward.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.REWARD))}\n")
            Deadline.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.DEADLINE))}\n")
            while(cursor.moveToNext()){
                Name.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.NAME))}\n")
                Description.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.DESCRIPTION))}\n")
                Reward.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.REWARD))}\n")
                Deadline.append("${cursor.getString(cursor.getColumnIndex(CatAppDB.DEADLINE))}\n")
            }
            cursor.close()
        }

        addUserInfo.setOnClickListener{
            val db = CatAppDB(this, null)
            val balance = enterBalance.text.toString()
            val catHealth = enterCatHealth.text.toString()
            db.addUserInfo(balance, catHealth)
            Toast.makeText(this, "userInfo added to database", Toast.LENGTH_LONG).show()
            enterBalance.text.clear()
            enterCatHealth.text.clear()
        }

        printUserInfo.setOnClickListener{
            val db = CatAppDB(this, null)
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