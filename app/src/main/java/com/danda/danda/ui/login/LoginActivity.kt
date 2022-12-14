package com.danda.danda.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.ActivityLoginBinding
import com.danda.danda.ui.register.RegisterActivity
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAction()

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

    }

    private fun setAction() {
        checkStatus()
        loginUser()
        goToRegister()
    }

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
                showToast(status.data)
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}