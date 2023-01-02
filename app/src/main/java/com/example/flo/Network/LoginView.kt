package com.example.flo.Network

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}