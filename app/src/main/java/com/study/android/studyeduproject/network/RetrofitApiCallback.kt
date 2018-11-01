package com.study.android.studyeduproject.network

interface RetrofitApiCallback<Any> {

    fun onError(t: Throwable)

    fun onSuccess(code: Int, resultData: Any?)

    fun onFailed(code: Int)

}
