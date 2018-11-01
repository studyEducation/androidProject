package com.study.android.studyeduproject.network

import android.content.Context

import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Retrofit 라이브러리 싱글톤 클래스
 */
class RetrofitClient {


    private var apiService: RetrofitApiService? = null

    /**
     * 싱글톤 객체 홀더 설정
     */
    class SingletonHolder {
        internal val INSTANCE = RetrofitClient(mContext)
    }

    companion object {
        private var retrofit: Retrofit? = null
        private var mContext: Context? = null


        /**
         * 클라이언트 싱글톤 객체 반환
         * @param context
         * @return
         */
        fun getInstance(context: Context?): RetrofitClient {

            if (context != null) {
                mContext = context
            }
            return SingletonHolder().INSTANCE
        }
    }

    /**
     * 생성자
     * @param context
     */
    constructor(context: Context?) {

        // retrofit 네트워크 로그를 보기 위한 객체 생성
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        // gson lenient 에러 방지용 객체 생성
        val gson = GsonBuilder()
                .setLenient()
                .create()

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://13.209.214.110:8080/yourMentor/")
                .client(client)
                .build()
    }


    /**
     * 클라이언트 api 서비스 객체를 생성한 뒤
     * 클라이언트 객체를 반환한다
     * @return
     */
    fun createBaseApi(): RetrofitClient {

        apiService = create(RetrofitApiService::class.java)

        return this

    }

    /**
     * retrofit api 서비스를 생성하기 위한 함수
     * @param service
     * @param <T>
     * @return
    </T> */
    private fun <T> create(service: Class<T>?): T {

        if (service == null) {
            throw RuntimeException("Api service is null!")
        }

        return retrofit!!.create(service)

    }


    /**
     * Test 용 api 호출
     * 방식 : GET
     * @param callback
     */
    fun callGetTest(callback: RetrofitApiCallback<Any?>) {

        apiService!!.test.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body())
                } else {
                    callback.onFailed(response.code())
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onError(t)
            }
        })

    }


}
