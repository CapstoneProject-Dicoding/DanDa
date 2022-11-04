package com.danda.danda.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.danda.danda.R
import com.danda.danda.databinding.ActivityLoginBinding
import com.danda.danda.ui.register.RegisterActivity
import com.danda.danda.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val password = binding.etPasswordLogin.text.toString()
        val email = binding.etEmailLogin.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginViewModel.loginUser.observe(this) { status ->
                    when (status) {
                        is Result.Loading -> {
                            // loading
                        }
                        is Result.Failure -> {
                            Toast.makeText(this, status.error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            Toast.makeText(this, status.data, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, RegisterActivity::class.java))
                        }
                    }
                }
                loginViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Email harus valid", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (email.isEmpty()) {
                Toast.makeText(this, "Email harus di isi", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password harus di isi", Toast.LENGTH_SHORT).show()
            }
        }

    }
}