package com.bbdevs.app
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bbdevs.app.service.UserInfoService
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = CatAppDB(this, null)
        val userInfoService = UserInfoService(db)

        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        addTask.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        updateFoodBar(userInfoService)

        feedMeBtn.setOnClickListener {
            val userInfo = userInfoService.get()
            if (userInfo.balance > 0) {
                if (userInfo.catHealth < 35) {
                    feedCat(userInfoService)
                } else {
                    Toast.makeText(this, "Your cat is filled :)\nCome feed it next week!", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this, "You have no cat food :(\nComplete some tasks!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        newCatPicBtn.setOnClickListener {
            val api_key = "b74b2180-8fae-4296-a89d-cf00fdb1abaa"
            val response = URL("https://api.thecatapi.com/v1/images/search?api_key=$api_key").readText()
            val imgUrl = Klaxon().parse<ApiResponse>(response.subSequence(1, response.length - 1).toString())?.url
            println(imgUrl)
            val inStream: InputStream = URL(imgUrl).openStream()
            val bmp = BitmapFactory.decodeStream(inStream)
            apiCatImg.setImageBitmap(bmp)
        }

        removeCatPicBtn.setOnClickListener {
            apiCatImg.setImageBitmap(null)
        }
//        addUserInfo.setOnClickListener{
//            val balance = enterBalance.text.toString().toInt()
//            val catHealth = enterCatHealth.text.toString().toInt()
//            userInfoService.update(UserInfo(1, balance, catHealth))
//            Toast.makeText(this, "userInfo added to database", Toast.LENGTH_LONG).show()
//            enterBalance.text.clear()
//            enterCatHealth.text.clear()
//        }

//        printUserInfo.setOnClickListener{
//            val userInfo = userInfoService.get()
//            Balance.append("${userInfo.balance}\n")
//            CatHealth.append("${userInfo.catHealth}\n")
//        }
    }

    private fun feedCat(userInfoService: UserInfoService) {
        val info = userInfoService.get()
        info.catHealth++
        info.balance--
        progressTxt.text = "${info.catHealth}/35"
        balanceTxt.text = "You have ${info.balance} food points"
        userInfoService.update(info)
        updateFoodBar(userInfoService)
    }

    private fun updateFoodBar(userInfoService: UserInfoService) {
        val info = userInfoService.get()
        foodBar.max = 35
        foodBar.progress = info.catHealth
        progressTxt.text = "${info.catHealth}/35"
        balanceTxt.text = "You have ${info.balance} food points"
    }
}