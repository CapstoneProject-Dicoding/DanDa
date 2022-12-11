package com.danda.danda.ui.change

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.ActivityChangePasswordBinding
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val changePasswordViewModel by viewModels<ChangePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAction()
    }

    private fun setAction() {
        changePassword()
        checkStatus()
    }

    private fun checkStatus() {
        changePasswordViewModel.changePassword.observe(this) { status ->
            when (status) {
                is Result.Failure -> {
                    showLoading(false, binding.progressBarChangePassword)
                    showToast(status.error.toString())
                }
                is Result.Loading -> {
                    showLoading(true, binding.progressBarChangePassword)
                }
                is Result.Success -> {
                    showLoading(false, binding.progressBarChangePassword)
                    showToast("Change Password Successfully")
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                    finish()
                }
            }
        }
    }

    private fun changePassword() = binding.apply {
        btnChange.setOnClickListener {
            val passwordLama = etOldPassword.text.toString()
            val passwordBaru = etNewPassword.text.toString()
            val konfirmasiPassword = etConfirmationPassword.text.toString()

            if (passwordLama.isEmpty()) {
                showToast("Masukan password lama dahulu")
            } else if (passwordBaru.isEmpty()) {
                showToast("Masukan password baru dahulu")
            } else {
                if (konfirmasiPassword != passwordBaru) {
                    showToast("Konfirmasi password dan password baru harus sama")
                } else {
                    changePasswordViewModel.changePassword(passwordBaru)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}