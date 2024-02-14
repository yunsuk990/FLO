package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.login.LoginView
import com.example.flo.ui.signup.SignUpView
import com.example.flo.data.entities.User
import com.example.flo.ui.splash.autoLoginView
import com.example.flo.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: autoLoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: autoLoginView){
        this.autoLoginView = autoLoginView
    }

    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp = response.body()!!
                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })
        Log.d("SIGNUP", "HELLO")
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp = response.body()!!
                when(val code = resp.code){
                    1000 -> loginView.onLoginSuccess(code, resp.result!!)
                    else -> loginView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }
        })
        Log.d("LOGIN", "HELLO")
    }

    fun autoLogin(jwt: String){
        Log.d("autoLogin", jwt.toString())
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.autoLogin(jwt).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resp = response.body()!!
                when(resp.code){
                    1000 ->{
                        autoLoginView.onAutoLoginSuccess()
                        Log.d("autoLogin", resp.message)
                    }
                    2002 -> {
                        autoLoginView.onAutoLoginFailure()
                        Log.d("autoLogin", resp.message)
                    }

                    2001 -> {
                        autoLoginView.onAutoLoginFailure()
                        Log.d("autoLogin", resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                autoLoginView.onAutoLoginFailure()
                Log.d("autoLogin", "FAILURE")
            }
        })
    }
}