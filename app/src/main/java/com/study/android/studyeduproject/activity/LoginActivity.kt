package com.study.android.studyeduproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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
    private var kakaoCallback : SessionCallback? = null

    // 페이스북 세션 콜백 객체
    private var facebookCallback : CallbackManager? = null


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
                    Log.d("Login", "Kakao name > " + result!!.nickname)
                    Log.d("Login", "Kakao profile Image > " + result!!.profileImagePath)
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

        // 페이스북으로 로그인하기 버튼을 누루면
        btn_facebook.setOnClickListener {
            // 페이스북 로그인 버튼이 눌린것 처럼 한다
            com_facebook_login.performClick()
        }

        // 카카오 콜백 초기화
        kakaoCallback = SessionCallback()

        // 카카오 세션에 콜백 등록
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

        // 페이스북 콜백 초기화
        facebookCallback = CallbackManager.Factory.create()

        // 페이스북 로그인 매니저에 콜백 등록
        LoginManager.getInstance().registerCallback(facebookCallback, object : FacebookCallback<LoginResult>{

            // 세션 오픈 성공
            override fun onSuccess(result: LoginResult?) {
                Log.d("Login", "facebook Token > " + result!!.accessToken.token)
                Log.d("Login", "facebook name > " + Profile.getCurrentProfile().name)
                Log.d("Login", "facebook profileImage > " + Profile.getCurrentProfile().getProfilePictureUri(200, 200))
                loginFacebook()
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            // 세션 오픈 실패
            override fun onError(error: FacebookException?) {
                Log.e("Login", "facebook Session Failed > " + error.toString())
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){


            return;
        }

        super.onActivityResult(requestCode, resultCode, data)
        facebookCallback!!.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 카카오톡 로그인 성공 후 메인 이동
     */
    fun loginKakao(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * 페이스북 로그인 성공 후 메인 이동
     */
    fun loginFacebook(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

}