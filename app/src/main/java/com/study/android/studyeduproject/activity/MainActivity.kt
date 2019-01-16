package com.study.android.studyeduproject.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.login.LoginManager
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.study.android.studyeduproject.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kakao_logout.setOnClickListener {

            UserManagement.getInstance().requestLogout(object : LogoutResponseCallback(){

                override fun onCompleteLogout() {
                    Log.d("Main", "Kakao Logout")
                }

            })

            Toast.makeText(this@MainActivity, "Kakao logout Complete", Toast.LENGTH_SHORT).show()
        }


        facebook_logout.setOnClickListener {
            LoginManager.getInstance().logOut()

            Toast.makeText(this@MainActivity, "Facebook logout Complete", Toast.LENGTH_SHORT).show()
        }
    }
}
