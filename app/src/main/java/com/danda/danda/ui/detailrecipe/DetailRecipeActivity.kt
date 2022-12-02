package com.danda.danda.ui.detailrecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ActivityDetailRecipeBinding
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.Result
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private val commentListAdapter: DetailRecipeAdapter by lazy(::DetailRecipeAdapter)
    private val detailViewModel by viewModels<DetailRecipeViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeData = intent.getParcelableExtra<Recipe>(DATA_RECIPE) as Recipe

        moveToDetailComment(recipeData)
        getDetailRecipe(recipeData)
        getListComment(recipeData.nameRecipe)
        checkStatus()
        checkUser(recipeData.nameRecipe, recipeData.imgUrl)

    }

    private fun getDetailRecipe(recipe: Recipe) = binding.apply {
        Glide.with(applicationContext)
            .load(recipe.imgUrl)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(ivFoodDetail)

        tvNameRecipe.text = recipe.nameRecipe
        tvBahan.text = recipe.ingredients
        tvAlat.text = recipe.tools
        tvCaraMasak.text = recipe.howToCook
    }

    private fun getListComment(nameRecipe: String) {
        binding.rvComment.apply {
            layoutManager = LinearLayoutManager(this@DetailRecipeActivity)
            adapter = commentListAdapter
            setHasFixedSize(true)
        }

        detailViewModel.listComment.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    commentListAdapter.setListComment(status.data)
                }
                else -> {}
            }
        }

        detailViewModel.getListComment(nameRecipe)
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

    private fun checkUser(nameRecipe: String, imgUrl: String) {
        profileViewModel.getUser.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    addComment(nameRecipe, imgUrl, status.data?.email.toString())
                }
                else -> {}
            }
        }
    }

    private fun addComment(nameRecipe: String, imgUrl: String, emailUser: String) = binding.apply{
        submitButton.setOnClickListener {
            val comment = etComment.text.toString()

            if (emailUser.isEmpty()) {
                showToast("Anda belum login")
            } else {
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

    private fun moveToDetailComment(recipe: Recipe) = binding.tvNextPage.setOnClickListener {
        val intent = Intent(this, DetailCommentActivity::class.java)
        intent.putExtra(DetailCommentActivity.DATA_NAME_RECIPE, recipe)
        startActivity(intent)
    }

    companion object {
        const val DATA_RECIPE = "data_recipe"
    }
}