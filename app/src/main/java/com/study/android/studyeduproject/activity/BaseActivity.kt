package com.study.android.studyeduproject.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.study.android.studyeduproject.MomentoApplication

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(MomentoApplication.getGlobalApplicationContext())

    }



}