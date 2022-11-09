package com.danda.danda.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.danda.danda.R
import com.danda.danda.databinding.ActivityRegisterBinding
import com.danda.danda.ui.login.LoginActivity
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        goToLogin()

        binding.btnRegister.setOnClickListener {
            registerUser()
            closedKeyboard()
        }

    }

    private fun registerUser() {
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()
        val ulangiPassword = binding.etUlangiPasswordRegister.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()) {
            if (ulangiPassword == password) {
                registerViewModel.registerUser.observe(this) { status ->
                    when (status) {
                        is Result.Loading -> {
                            showLoading(true, binding.progressBarRegister)
                        }
                        is Result.Failure -> {
                            showLoading(false, binding.progressBarRegister)
                            showToast(status.error.toString())
                        }
                        is Result.Success -> {
                            showLoading(false, binding.progressBarRegister)
                            showToast("Created User")
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
                registerViewModel.registerUser(email, password)
            } else if (password.length <= 6) {
                showToast("Password harus 6 karakter")
            } else {
                showToast("Ulangi password dan password harus sama")
            }
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Format email harus valid")
            } else if (password.isEmpty()) {
                showToast("Password tidak boleh kosong")
            }
        }
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun goToLogin() = binding.loginHere.setOnClickListener {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}