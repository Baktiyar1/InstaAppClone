package com.back4app.kotlin.back4appexample.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.back4app.kotlin.back4appexample.databinding.ActivityLogInBinding
import com.back4app.kotlin.back4appexample.utils.toast
import com.parse.ParseUser

class LogInActivity : AppCompatActivity() {

    private val binding: ActivityLogInBinding by lazy {
        ActivityLogInBinding.inflate(layoutInflater)
    }

    override fun onStart() {
        super.onStart()
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            logInButton.setOnClickListener {
                val userName = editTextName.text.toString().trim()
                val email = editTextName.text.toString().trim()
                val userPassword = editTextPassword.text.toString().trim()
                login(userName, email, userPassword)
            }

            registrationTv.setOnClickListener {
                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            }
        }

    }

    private fun login(userName: String, email: String, userPassword: String) {
        ParseUser.logInInBackground(userName, userPassword) { parseUser, e ->
            if (parseUser != null) {
                toast("User is LOGGED in!")
                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            } else {
                ParseUser.logOut()
                toast(e.message.toString())
            }
        }
        ParseUser.logInInBackground(email, userPassword) { parseUser, e ->
            if (parseUser != null) {
                toast("User is LOGGED in!")
                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            } else {
                ParseUser.logOut()
                toast(e.message.toString())
            }
        }
    }
}