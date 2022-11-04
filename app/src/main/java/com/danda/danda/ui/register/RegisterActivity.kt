package com.danda.danda.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.danda.danda.databinding.ActivityRegisterBinding
import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private var objUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val nama = binding.etNameRegister.text.toString()
        val username = binding.etUsernameRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()
        val ulangiPassword = binding.etUlangiPasswordRegister.text.toString()
        val email = binding.etEmailRegister.text.toString()

        // SUDAH BISA REGISTER, TINGGAL NAMBAHIN LOGIKA JIKA EDIT TEXT, IMPROVE LAGI
        // JIKA TEXT KURANG 6 KARAKTER

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            nama.isNotEmpty() &&
            username.isNotEmpty() &&
            password.isNotEmpty() &&
            ulangiPassword.isNotEmpty() &&
            ulangiPassword == password
        ) {
            if (ulangiPassword != password) Toast.makeText(this, "Ulangi Password dan Password harus sama", Toast.LENGTH_SHORT).show()
            else {
                registerViewModel.registerUser(
                    User(
                        id = objUser?.id ?: "",
                        nama,
                        username,
                        password,
                        email
                    )
                )

                registerViewModel.registerUser.observe(this) { status ->
                    when (status) {
                        is Result.Loading -> {
                            // loading
                        }
                        is Result.Failure -> {
                            Toast.makeText(this, status.error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            Toast.makeText(this, status.data, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        } else {
            if (username.isEmpty()) Toast.makeText(this, "username harus di isi", Toast.LENGTH_SHORT).show()
            else if (email.isEmpty()) Toast.makeText(this, "Email harus di isi", Toast.LENGTH_SHORT).show()
            else if (password.isEmpty()) Toast.makeText(this, "Password harus di isi", Toast.LENGTH_SHORT).show()
            else if (ulangiPassword.isEmpty()) Toast.makeText(this, "Ulangi Password harus di isi", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Email harus valid", Toast.LENGTH_SHORT).show()
        }
    }

}