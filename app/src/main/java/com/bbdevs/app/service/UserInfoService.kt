package com.bbdevs.app.service

import com.bbdevs.app.CatAppDB
import com.bbdevs.app.entity.Task
import com.bbdevs.app.entity.UserInfo

class UserInfoService(private val db: CatAppDB) {

    fun get(): UserInfo {
        return db.getUserInfo()
    }

    fun update(userInfo: UserInfo) {
        db.updateUserInfo(userInfo)
    }

}