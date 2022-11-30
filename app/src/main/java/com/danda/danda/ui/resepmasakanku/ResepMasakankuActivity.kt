package com.danda.danda.ui.resepmasakanku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danda.danda.databinding.ActivityResepMasakankuBinding
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResepMasakankuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResepMasakankuBinding
    private val resepListAdapter: ResepMasakankuAdapter by lazy(::ResepMasakankuAdapter)
    private val resepMasakankuViewModel by viewModels<ResepMasakankuViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResepMasakankuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUser()

    }

    private fun checkUser() {
        profileViewModel.getUser.observe(this) { status ->
            when(status) {
                is Result.Success -> {
                    getListRecipe(status.data?.email.toString())
                }
                else -> {}
            }
        }
    }

    private fun getListRecipe(emailUser: String) {
        binding.rvResepMasakanku.apply {
            layoutManager = LinearLayoutManager(this@ResepMasakankuActivity)
            adapter = resepListAdapter
            setHasFixedSize(true)
        }

        resepMasakankuViewModel.recipe.observe(this) { status ->
            when (status) {
                is Result.Failure -> {
                    showLoading(false, binding.progressBarResepMasakanku)
                    showToast(status.error.toString())
                }
                is Result.Loading -> showLoading(true, binding.progressBarResepMasakanku)
                is Result.Success -> {
                    showLoading(false, binding.progressBarResepMasakanku)
                    resepListAdapter.setListRecipe(status.data)
                }
            }
        }

        resepMasakankuViewModel.getListRecipe(emailUser)
    }
}