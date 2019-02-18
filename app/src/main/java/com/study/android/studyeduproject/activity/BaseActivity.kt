package com.study.android.studyeduproject.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.study.android.studyeduproject.MomentoApplication
import com.study.android.studyeduproject.utils.Utils

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(MomentoApplication.getGlobalApplicationContext())

    }


    override fun onResume() {
        super.onResume()

        // 네트워크 연결 상태 좋지 않음
        if(!Utils.isUseNetwork(this)){

        }

    }
}