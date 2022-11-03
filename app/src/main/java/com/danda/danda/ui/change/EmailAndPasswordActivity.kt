package com.danda.danda.ui.change

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danda.danda.R
import com.danda.danda.databinding.ActivityEmailAndPasswordBinding

class EmailAndPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailAndPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAndPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_change_email_password)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}