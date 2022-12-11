package com.danda.danda.ui.favorite

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.danda.danda.databinding.ActivityFavoriteBinding
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteAdapter: FavoriteAdapter by lazy(::FavoriteAdapter)
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setAction()
    }

    private fun setAction() {
        checkUser()

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun checkUser() {
        profileViewModel.getUser.observe(this) { status ->
            when(status) {
                is Result.Success -> {
                    getFavoriteList(status.data?.email.toString())
                }
                else -> {}
            }
        }
    }

    private fun getFavoriteList(emailUser: String) {
        binding.rvFavoriteList.apply {
            showRecyclerView()
            adapter = favoriteAdapter
            setHasFixedSize(true)
        }

        favoriteViewModel.listFavorite.observe(this) { status ->
            when(status) {
                is Result.Loading -> showLoading(true, binding.progressBarFavorite)
                is Result.Success -> {
                    showLoading(false, binding.progressBarFavorite)
                    favoriteAdapter.setListFavorite(status.data)
                }
                is Result.Failure -> {
                    showLoading(false, binding.progressBarFavorite)
                    showToast(status.error.toString())
                }
            }
        }
        favoriteViewModel.getFavoriteList(emailUser)
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvFavoriteList.layoutManager = GridLayoutManager(applicationContext, 2)
            } else {
                rvFavoriteList.layoutManager = layoutManager
            }
            val itemDecoration = DividerItemDecoration(applicationContext, layoutManager.orientation)
            rvFavoriteList.addItemDecoration(itemDecoration)
        }
    }
}