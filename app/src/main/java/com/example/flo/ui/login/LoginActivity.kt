package com.example.flo.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.flo.ui.signup.SignUpActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.data.remote.Result
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.ui.main.MainActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginRegisterTv.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginBtn.setOnClickListener {
            login()
        }
        binding.kakao.setOnClickListener{

            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
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
                        startMainActivity()
                    }
                }
            }
            else {
                kakaoLogin()
            }

        }

    }

    private fun kakaoLogin(){
        UserApiClient().loginWithKakaoAccount(this@LoginActivity){ token, error ->
            if (error != null) {
                Log.d("kakao", "로그인 실패", error)
            }
            else if (token != null) {
                Log.d("kakao", "로그인 성공 ${token.accessToken}")
                Toast.makeText(this, "유저 토큰: " + token.accessToken, Toast.LENGTH_SHORT).show()
                saveJwt2("token", 1234)
                startMainActivity()
            }
        }
//        Toast.makeText(this@LoginActivity, "카카오톡 설치 필요.",Toast.LENGTH_SHORT).show()
    }

    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(User(email, pwd, ""))

    }

//    private fun saveJwt(jwt: Int){
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }

    private fun saveJwt2(jwt: String, userIdx: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("jwt", jwt)
        editor.putInt("userIdx", userIdx)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when(code){
            1000 -> {
                saveJwt2(result.jwt, result.userIdx)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        //실패 처리
        Toast.makeText(this, "회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }
}