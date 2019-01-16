package com.study.android.studyeduproject

import android.app.Application
import android.content.Context
import com.kakao.auth.*
import com.study.android.studyeduproject.utils.KakaoSDKAdapter

class MomentoApplication : Application() {

    companion object {

        var context: MomentoApplication? = null

        /**
         * Context 반환 함수
         * @return Application Context
         */
        fun getGlobalApplicationContext() : MomentoApplication{

            if(context == null)
                context = MomentoApplication()

            return context as MomentoApplication
        }

    }



    override fun onCreate() {
        super.onCreate()

        context = this

        KakaoSDK.init(KakaoSDKAdapter())

    }


    override fun onTerminate() {
        super.onTerminate()

        context = null
    }
}