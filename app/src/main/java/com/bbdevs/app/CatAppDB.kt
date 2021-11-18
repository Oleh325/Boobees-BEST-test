package com.bbdevs.app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TASK_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $STATISTICS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USERINFO_TABLE")
        onCreate(db)
    }

    fun addTask(name : String, description : String,
                reward : String, deadline : String ){
        val values = ContentValues()
        values.put(NAME, name)
        values.put(DESCRIPTION, description)
        values.put(REWARD, reward)
        values.put(DEADLINE, deadline)
        values.put(IS_COMPLETED, FALSE)
        val db = this.writableDatabase
        db.insert(TASK_TABLE, null, values)
        db.close()
    }

    fun getTask(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TASK_TABLE", null)
    }

    fun addStatistics(day_date : String, food_count : String,
                      worktime : String ){
        val values = ContentValues()
        values.put(DAY_DATE, day_date)
        values.put(FOOD_COUNT, food_count)
        values.put(WORKTIME, worktime)
        val db = this.writableDatabase
        db.insert(STATISTICS_TABLE, null, values)
        db.close()
    }

    fun getStatistics(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $STATISTICS_TABLE", null)
    }

    fun addUserInfo(balance : String, cat_health : String ){
        val values = ContentValues()
        values.put(BALANCE, balance)
        values.put(CAT_HEALTH, cat_health)
        val db = this.writableDatabase
        db.insert(USERINFO_TABLE, null, values)
        db.close()
    }

    fun getUserInfo(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $USERINFO_TABLE", null)
    }

    companion object Tasks{
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