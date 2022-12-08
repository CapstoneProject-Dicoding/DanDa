package com.danda.danda.ui.detailrecipe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ActivityDetailRecipeBinding
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.favorite.FavoriteViewModel
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.Result
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private val commentListAdapter: DetailRecipeAdapter by lazy(::DetailRecipeAdapter)
    private val detailViewModel by viewModels<DetailRecipeViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recipeData = intent.getParcelableExtra<Recipe>(DATA_RECIPE) as Recipe

        setRecyclerView()
        checkStatusAddFavorite()
        moveToDetailComment(recipeData)
        getDetailRecipe(recipeData)
        getListComment(recipeData.nameRecipe)
        checkStatus()
        checkUser(recipeData, recipeData.nameRecipe, recipeData.imgUrl)

    }

    private fun getDetailRecipe(recipe: Recipe) = binding.apply {
        Glide.with(applicationContext)
            .load(recipe.imgUrl)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(ivFoodDetail)

        tvNameRecipe.text = recipe.nameRecipe
        tvBahan.text = recipe.ingredients
        tvCaraMasak.text = recipe.howToCook
    }

    private fun setRecyclerView() = binding.rvComment.apply {
        layoutManager = LinearLayoutManager(this@DetailRecipeActivity)
        adapter = commentListAdapter
        setHasFixedSize(true)
    }

    private fun getListComment(nameRecipe: String) {
        detailViewModel.listComment.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    commentListAdapter.setListComment(status.data)
                    commentListAdapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }

        detailViewModel.getListComment(nameRecipe)
    }

    private fun checkUser(recipe: Recipe, nameRecipe: String, imgUrl: String) {
        profileViewModel.getUser.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    if (status.data != null) {
                        addComment(nameRecipe, imgUrl, status.data.email.toString())
                        getFavoriteByNameRecipe(recipe, status.data.email.toString(), nameRecipe)
                    } else {
                        addComment(nameRecipe, imgUrl, null)
                        getFavoriteByNameRecipe(recipe, null, nameRecipe)
                    }
                }
                else -> {}
            }
        }
    }

    private fun checkStatus() {
        detailViewModel.comment.observe(this) { status ->
            when(status) {
                is Result.Loading -> {}
                is Result.Failure -> {}
                is Result.Success -> {
                    showToast(status.data)
                }
            }
        }
    }

    private fun addComment(nameRecipe: String, imgUrl: String, emailUser: String?) = binding.apply {
        if (emailUser.isNullOrEmpty()) {
            submitButton.setOnClickListener {
                showToast("Anda belum login")
            }
        } else {
            submitButton.setOnClickListener {
                val comment = etComment.text.toString()

                if (comment.isEmpty()) {
                    showToast("Masukan ulasanmu dahulu")
                } else {
                    detailViewModel.addComment(Comment(
                        "",
                        nameRecipe,
                        comment,
                        imgUrl,
                        emailUser
                    ))
                }
            }
        }
    }

    private fun getFavoriteByNameRecipe(recipe: Recipe, emailUser: String?, nameRecipe: String) {
        favoriteViewModel.getFavoriteByNameRecipe(emailUser, nameRecipe)

        favoriteViewModel.getFavorite.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    if (status.data.isNotEmpty()) {
                        addFavorite(emailUser, recipe, status.data)
                    } else {
                        addFavorite(emailUser, recipe, null)
                    }
                }
                else -> {}
            }
        }
    }

    private fun checkStatusAddFavorite() {
        favoriteViewModel.addFavorite.observe(this) {status ->
            when(status) {
                is Result.Success -> {
                    showToast(status.data)
                }
                else -> {}
            }
        }
    }

    private fun addFavorite(emailUser: String?, recipe: Recipe, fav: List<Favorite>?) {
        if (emailUser.isNullOrEmpty()) {
            if (fav.isNullOrEmpty()) {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_baseline_favorite_border_24)

                    setOnClickListener {
                        showToast("Anda belum login")
                    }
                }
            } else {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_baseline_favorite_24)

                    setOnClickListener {
                        showToast("Anda belum login")
                    }
                }
            }
        } else {
            if (fav.isNullOrEmpty()) {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_baseline_favorite_border_24)

                    setOnClickListener {
                        favoriteViewModel.addFavorite(Favorite(
                            "",
                            recipe.nameRecipe,
                            recipe.ingredients,
                            recipe.tools,
                            recipe.howToCook,
                            recipe.imgUrl,
                            recipe.emailUser,
                            recipe.username
                        ))
                    }
                }
            } else {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_baseline_favorite_24)

                    setOnClickListener {
                        showToast("Resep ini sudah ada di favoritmu")
                    }
                }
            }
        }
    }

    private fun moveToDetailComment(recipe: Recipe) = binding.tvNextPage.setOnClickListener {
        val intent = Intent(this, DetailCommentActivity::class.java)
        intent.putExtra(DetailCommentActivity.DATA_NAME_RECIPE, recipe)
        startActivity(intent)
    }

    companion object {
        const val DATA_RECIPE = "data_recipe"
    }
}