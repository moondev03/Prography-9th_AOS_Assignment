package com.weave.project.data.remote

import com.weave.project.util.Constant
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {

    // 모든 요청에 Authorization 이 필요하기에 Intercepter를 사용함
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().run {
            addHeader("Accept-Version", "v1")
            addHeader("Authorization", "Client-ID ${Constant.ACCESS_KEY}")
            build()
        }

        return chain.proceed(request)
    }
}