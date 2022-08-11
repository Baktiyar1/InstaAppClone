package com.back4app.kotlin.back4appexample.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.back4app.kotlin.back4appexample.databinding.ActivitySignUpBinding
import com.back4app.kotlin.back4appexample.utils.toast
import com.parse.ParseUser

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onStart() {
        super.onStart()
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            signUpButton.setOnClickListener {
                val userName = editTextName.text.toString().trim()
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                val password2 = editTextPassword2.text.toString().trim()
                signUp(userName, email, password, password2)
            }

            loginTv.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
            }
        }
    }

    private fun signUp(userName: String?, email: String?, password: String?, password2: String?) =
        if (password == password2 && password != null) {
            val user = ParseUser()
            user.username = userName?.lowercase()
            user.email = email
            user.setPassword(password)
            user.signUpInBackground {
                if (it == null) {
                    toast("User is registered!")
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                } else {
                    ParseUser.logOut()
                    toast(it.message.toString())
                }
            }
        } else {
            toast("Fill in a password or check if your passwords match!")
        }
}