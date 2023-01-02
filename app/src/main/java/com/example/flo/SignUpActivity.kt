package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.Database.SongDatabase
import com.example.flo.Database.User
import com.example.flo.Network.*
import com.example.flo.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerSignupBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()
        val name: String = binding.loginNameEt.text.toString()
        return User(email, pwd, name)
    }

//    private fun signUp(){
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if(binding.loginPasswordEt.text.toString() !=  binding.loginCheckpasswordEt.text.toString()){
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.UserDao().insert(getUser())
//
//        val user = userDB.UserDao().getUsers()
//        Log.d("SIGNUPACT", user.toString())
//        finish()
//    }

    private fun signUp() {
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginNameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString() !=  binding.loginCheckpasswordEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)
        authService.signUp(getUser())

    }

    override fun onSignUpFailure() {
        finish()
    }

    override fun onSignUpSuccess() {

    }

}