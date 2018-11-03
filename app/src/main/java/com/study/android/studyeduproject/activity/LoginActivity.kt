package com.study.android.studyeduproject.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.study.android.studyeduproject.MainActivity
import com.study.android.studyeduproject.R

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)

    }


    @OnClick(R.id.btn_kakao)
    fun loginKakao(){
        val intent : Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.btn_facebook)
    fun loginFacebook(){

    }

}