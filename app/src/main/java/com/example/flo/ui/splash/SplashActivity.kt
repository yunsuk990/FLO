package com.example.flo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.login.LoginActivity
import com.example.flo.ui.main.MainActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class SplashActivity: AppCompatActivity(), autoLoginView {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autoLogin()
    }

    fun autoLogin(){
        val service = AuthService()
        service.setAutoLoginView(this)
        service.autoLogin(getJwt())
    }



    private fun getJwt(): String{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt", "")!!
    }

    override fun onAutoLoginSuccess() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)

    }

    override fun onAutoLoginFailure() {

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
                        kakaoLogin()

                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
        else {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
            }, 1000)
        }
    }

    private fun kakaoLogin(){
        UserApiClient().loginWithKakaoAccount(this@SplashActivity){ token, error ->
            if (error != null) {
                Log.d("kakao", "로그인 실패", error)
            }
            else if (token != null) {
                Log.d("kakao", "로그인 성공 ${token.accessToken}")
                saveJwt2("token", 1234)
            }
        }
//        Toast.makeText(this@SplashActivity, "카카오톡 설치 필요.", Toast.LENGTH_SHORT).show()
    }
    private fun saveJwt2(jwt: String, userIdx: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("jwt", jwt)
        editor.putInt("userIdx", userIdx)
        editor.apply()
    }

}