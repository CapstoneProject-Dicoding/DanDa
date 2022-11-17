package com.danda.danda.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.danda.danda.R
import com.danda.danda.databinding.ActivityLoginBinding
import com.danda.danda.ui.home.HomeFragment
import com.danda.danda.ui.register.RegisterActivity
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        checkStatus()
        loginUser()
        goToRegister()

    }

//    private fun logoutUser() {
//        loginViewModel.logoutUser.observe(this) { status ->
//            when (status) {
//                is Result.Loading -> {
//                    // loading
//                }
//                is Result.Failure -> {
//                    showToast(status.error.toString())
//                }
//                is Result.Success -> {
//                    showToast("Success")
//                }
//            }
//        }
//        loginViewModel.logout()
//    }

    private fun loginUser() = binding.btnLogin.setOnClickListener {
        checkUser()
        closedKeyboard()
    }

    private fun checkStatus() = loginViewModel.loginUser.observe(this) { status ->
        when (status) {
            is Result.Loading -> {
                showLoading(true, binding.progressBarLogin)
            }
            is Result.Failure -> {
                showToast(status.error.toString())
                showLoading(false, binding.progressBarLogin)
            }
            is Result.Success -> {
                showLoading(false, binding.progressBarLogin)
                showToast("Success")
                val mFragmentManager = supportFragmentManager
                val mFragmentTransaction = mFragmentManager.beginTransaction()
                val mFragment = HomeFragment()
                mFragmentTransaction.add(R.id.nav_host_fragment_activity_main, mFragment).commit()
                finish()
            }
        }
    }

    private fun checkUser() {
        val password = binding.etPasswordLogin.text.toString()
        val email = binding.etEmailLogin.text.toString()

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()) {
            if (password.length >= 6) {
                loginViewModel.loginUser(email, password)
            } else {
                showToast("Password harus 6 karakter")
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

    private fun goToRegister() = binding.registerHere.setOnClickListener {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}