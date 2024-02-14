package com.example.flo

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

val NATIVE_APP_KEY = "d162987a9d67a07785fdae2ec6c9dea8"

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}