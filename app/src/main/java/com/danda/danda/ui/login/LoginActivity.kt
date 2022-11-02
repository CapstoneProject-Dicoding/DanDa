package com.danda.danda.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danda.danda.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}