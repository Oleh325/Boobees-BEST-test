package com.bbdevs.app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bbdevs.app.entity.Task
import com.bbdevs.app.entity.UserInfo
import java.time.LocalDateTime

class CatAppDB(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val tasks = ("CREATE TABLE IF NOT EXISTS $TASK_TABLE" +
                "($TASK_ID INTEGER PRIMARY KEY, $NAME TEXT NOT NULL, " +
                "$DESCRIPTION TEXT, $REWARD INTEGER NOT NULL, $DEADLINE DATE, " +
                "$IS_COMPLETED TINYINT(1) NOT NULL)")
        db.execSQL(tasks)
        val statistics = ("CREATE TABLE IF NOT EXISTS $STATISTICS_TABLE" +
                "($STATISTICS_ID INTEGER PRIMARY KEY, $DAY_DATE DATE NOT NULL, " +
                "$FOOD_COUNT INTEGER NOT NULL, $WORKTIME INTEGER NOT NULL)")
        db.execSQL(statistics)
        val userInfo = ("CREATE TABLE IF NOT EXISTS $USERINFO_TABLE" +
                "($USERINFO_ID INTEGER PRIMARY KEY, $BALANCE INTEGER NOT NULL, " +
                "$CAT_HEALTH INTEGER NOT NULL)")
        db.execSQL(userInfo)
        val initUserInfo = ContentValues()
        initUserInfo.put(BALANCE, 10)
        initUserInfo.put(CAT_HEALTH, 15)
        db.insert(USERINFO_TABLE, null, initUserInfo)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TASK_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $STATISTICS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USERINFO_TABLE")
        onCreate(db)
    }

    fun addTask(task: Task) {
        val values = ContentValues()
        values.put(NAME, task.name)
        values.put(DESCRIPTION, task.description)
        values.put(REWARD, task.reward)
        values.put(DEADLINE, task.deadline.toString())
        values.put(IS_COMPLETED, FALSE)
        val db = this.writableDatabase
        db.insert(TASK_TABLE, null, values)
    }

    @SuppressLint("Range")
    fun getTasks(): List<Task> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TASK_TABLE", null)
        val list = ArrayList<Task>()
        if (cursor == null || cursor.getCount() < 1) return list
        cursor.moveToFirst()
        list.add(Task(
            cursor.getInt((cursor.getColumnIndex(TASK_ID))),
            cursor.getString(cursor.getColumnIndex(NAME)),
            cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
            cursor.getInt(cursor.getColumnIndex(REWARD)),
            LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DEADLINE))),
            cursor.getInt(cursor.getColumnIndex(IS_COMPLETED)) == 1
        ))
        while(cursor.moveToNext()) {
            list.add(Task(
                cursor.getInt((cursor.getColumnIndex(TASK_ID))),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(REWARD)),
                LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DEADLINE))),
                cursor.getInt(cursor.getColumnIndex(IS_COMPLETED)) == 1
            ))
        }
        cursor.close()
        return list
    }

    fun updateUserInfo(userInfo: UserInfo) {
        val values = ContentValues()
        values.put(TASK_ID, userInfo.id)
        values.put(BALANCE, userInfo.balance)
        values.put(CAT_HEALTH, userInfo.catHealth)
        val db = this.writableDatabase
        db.update(USERINFO_TABLE, values, null, null)
    }

    @SuppressLint("Range")
    fun getUserInfo(): UserInfo {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $USERINFO_TABLE", null)
        val userInfo = UserInfo(1, 0, 0)
        if (cursor == null || cursor.getCount() < 1) return userInfo
        cursor.moveToFirst()
        userInfo.balance = cursor.getInt(cursor.getColumnIndex(BALANCE))
        userInfo.catHealth = cursor.getInt(cursor.getColumnIndex(CAT_HEALTH))
        cursor.close()
        return userInfo
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_ID, task.id)
        values.put(NAME, task.name)
        values.put(DESCRIPTION, task.description)
        values.put(REWARD, task.reward)
        values.put(DEADLINE, task.deadline.toString())
        values.put(IS_COMPLETED, task.isCompleted)
        db.update(TASK_TABLE, values, null, null)
    }

    fun addStatistics(day_date : String, food_count : String,
                      worktime : String ){
        val values = ContentValues()
        values.put(DAY_DATE, day_date)
        values.put(FOOD_COUNT, food_count)
        values.put(WORKTIME, worktime)
        val db = this.writableDatabase
        db.insert(STATISTICS_TABLE, null, values)
    }

    fun getStatistics(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $STATISTICS_TABLE", null)
    }

    companion object Tasks {
        // all database's variables
        private val DATABASE_NAME = "cat_app_database"
        private val DATABASE_VERSION = 1
        val TASK_TABLE = "task_table"
        val STATISTICS_TABLE = "statisticsPerDay_table"
        val USERINFO_TABLE = "userInfo_table"
        val TASK_ID = "id"
        val STATISTICS_ID = "id"
        val USERINFO_ID = "id"
        val NAME = "name"
        val DESCRIPTION = "description"
        val REWARD = "reward"
        val DEADLINE = "deadline_date"
        val IS_COMPLETED = "is_completed"
        val DAY_DATE = "day_date"
        val FOOD_COUNT = "food_count"
        val WORKTIME = "worktime"
        val BALANCE = "balance"
        val CAT_HEALTH = "cat_health"

        val FALSE = "0"
    }
}