package com.study.android.studyeduproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.study.android.studyeduproject.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    // 카카오 세션 콜백 객체
    private var callback : SessionCallback? = null


    /**
     * 카카오 세션에 대한 콜백 클래스
     */
    inner class SessionCallback : ISessionCallback{

        // 세션 오픈 실패
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("Login", "Kakao Session Failed > " + exception.toString())
        }

        // 세션 오픈 성공
        override fun onSessionOpened() {

            var key = ArrayList<String>()
            key.add("properties.profile_image")

            // 세션 오픈 성공시 (로그인 요청 성공)
            // 사용자 정보를 얻어온다
            UserManagement.getInstance().me(key, object: MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    Log.d("Login", "Kakao > " + result.toString())



                    loginKakao()
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("Login", "Kakao Session Failed > " + errorResult.toString())
                }

            })
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // 카카오톡으로 로그인하기 버튼을 누루면
        btn_kakao.setOnClickListener{
            // 카카오톡 로그인 버튼이 눌린것 처럼 한다
            com_kakao_login.performClick()
        }

        // 콜백 초기화
        callback = SessionCallback()

        // 카카오 세션에 콜백 등록
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){


            return;
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 카카오톡 로그인 성공 후 메인 이동
     */
    fun loginKakao(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun loginFacebook(){

    }

}