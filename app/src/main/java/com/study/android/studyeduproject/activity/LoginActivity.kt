package com.study.android.studyeduproject.activity

import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.study.android.studyeduproject.R

class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)
    }


    @OnClick(R.id.btn_kakao)
    fun loginKakao(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.btn_facebook)
    fun loginFacebook(){

    }

}