package com.danda.danda.ui.detailrecipe

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.ActivityDetailRecipeBinding
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.editprofile.EditProfileActivity
import com.danda.danda.ui.editprofile.EditProfileViewModel
import com.danda.danda.ui.favorite.FavoriteActivity
import com.danda.danda.ui.favorite.FavoriteViewModel
import com.danda.danda.ui.login.LoginActivity
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private val commentListAdapter: DetailRecipeAdapter by lazy(::DetailRecipeAdapter)
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private val detailViewModel by viewModels<DetailRecipeViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAction()
    }

    private fun setAction() {
        val recipeData = intent.getParcelableExtra<Recipe>(DATA_RECIPE) as Recipe

        setRecyclerView()
        checkStatusAddFavorite()
        moveToDetailComment(recipeData)
        getDetailRecipe(recipeData)
        getListComment(recipeData.nameRecipe)
        checkStatusComment(recipeData)
        checkUser(recipeData, recipeData.nameRecipe)
        shareRecipeData(recipeData)

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun getDetailRecipe(recipe: Recipe) = binding.apply {
        Glide.with(applicationContext)
            .load(recipe.imgUrl)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(ivFoodDetail)

        tvNameRecipe.text = recipe.nameRecipe
        tvDescription.text = recipe.description
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
                    if (status.data.isNotEmpty()) {
                        commentListAdapter.setListComment(status.data)
                        commentListAdapter.notifyDataSetChanged()
                    }
                }
                else -> {}
            }
        }

        detailViewModel.getListComment(nameRecipe)
    }

    private fun checkUser(recipe: Recipe, nameRecipe: String) {
        profileViewModel.getUser.observe(this) { status ->
            when (status) {
                is Result.Success -> {
                    if (status.data?.email != null) {
                        editProfileViewModel.getDataFromUser(status.data.email.toString())
                        getFavoriteByNameRecipe(recipe, status.data.email.toString(), nameRecipe)
                        if (status.data.photoUrl != null) {
                            getUser(nameRecipe, status.data.photoUrl.toString(), status.data.email.toString())
                        } else {
                            binding.submitButton.setOnClickListener {
                                statusUpdateProfile()
                            }
                        }
                    } else {
                        getUser(nameRecipe, "","")
                        getFavoriteByNameRecipe(recipe, null, nameRecipe)
                        binding.submitButton.setOnClickListener {
                            loginHere()
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun getUser(nameRecipe: String, imgUrl: String, emailUser: String) {
        editProfileViewModel.getFromUser.observe(this) { status ->
            when(status) {
                is Result.Success -> {
                    addComment(nameRecipe, imgUrl, emailUser, status.data?.username.toString())
                }
                else -> {}
            }
        }
    }

    private fun checkStatusComment(recipe: Recipe) {
        detailViewModel.comment.observe(this) { status ->
            when(status) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBarDetail)
                }
                is Result.Failure -> {
                    showToast(status.error.toString())
                    showLoading(false, binding.progressBarDetail)
                }
                is Result.Success -> {
                    showLoading(false, binding.progressBarDetail)
                    statusAddComment(recipe)
                }
            }
        }
    }

    private fun addComment(nameRecipe: String, imgUrl: String, emailUser: String, username: String) = binding.apply {
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
                    emailUser,
                    username
                ))

                etComment.setText("")
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
                    statusAddFavorite()
                }
                is Result.Failure -> {
                    showToast(status.error.toString())
                }
                else -> {}
            }
        }
    }

    private fun addFavorite(emailUser: String?, recipe: Recipe, fav: List<Favorite>?) {
        if (emailUser.isNullOrEmpty()) {
            if (fav.isNullOrEmpty()) {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_heart_white)

                    setOnClickListener {
                        loginHere()
                    }
                }
            } else {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_heart_red)

                    setOnClickListener {
                        loginHere()
                    }
                }
            }
        } else {
            if (fav.isNullOrEmpty()) {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_heart_white)

                    setOnClickListener {
                        favoriteViewModel.addFavorite(Favorite(
                            "",
                            recipe.nameRecipe,
                            recipe.ingredients,
                            recipe.description,
                            recipe.howToCook,
                            recipe.imgUrl,
                            emailUser,
                            recipe.username
                        ))

                        setImageResource(R.drawable.ic_heart_red)
                    }
                }
            } else {
                binding.btnFavorite.apply {
                    setImageResource(R.drawable.ic_heart_red)

                    setOnClickListener {
                        showToast("Resep ini sudah ada di favoritmu")
                    }
                }
            }
        }
    }

    private fun statusAddComment(recipe: Recipe) {
        AlertDialog.Builder(this)
            .setTitle("KONFIRMASI KOMENTAR")
            .setMessage("Berhasil menambahkan komentar, Ingin melihat daftar komentar anda?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                val intent = Intent(this, DetailCommentActivity::class.java)
                intent.putExtra(DetailCommentActivity.DATA_NAME_RECIPE, recipe)
                startActivity(intent)
                dialogInterface.dismiss()
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()

            }
            .show()
    }

    private fun statusAddFavorite() {
        AlertDialog.Builder(this)
            .setTitle("KONFIRMASI FAVORITE")
            .setMessage("Berhasil menambahkan ke favorite, Ingin melihat daftar favorite anda?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                startActivity(Intent(this, FavoriteActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                dialogInterface.dismiss()
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()

            }
            .show()
    }

    private fun statusUpdateProfile() {
        AlertDialog.Builder(this)
            .setTitle("KONFIRMASI LENGKAPI PROFILE")
            .setMessage("Anda belum melengkapi profile, Ingin lengkapi profile sekarang?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                startActivity(Intent(this, EditProfileActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
                dialogInterface.dismiss()
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()

            }
            .show()
    }

    private fun loginHere() {
        AlertDialog.Builder(this)
            .setTitle("KONFIRMASI LOGIN")
            .setMessage("Anda belum login, mau login sekarang?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun moveToDetailComment(recipe: Recipe) = binding.tvNextPage.setOnClickListener {
        val intent = Intent(this, DetailCommentActivity::class.java)
        intent.putExtra(DetailCommentActivity.DATA_NAME_RECIPE, recipe)
        startActivity(intent)
    }

    private fun shareRecipeData(recipe: Recipe) = binding.btnShare.setOnClickListener {
        val shareUserData = "Name Recipe: ${recipe.nameRecipe}\n" +
                "Description: ${recipe.description}\n" +
                "Image Recipe: ${recipe.imgUrl}"
        val share: Intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareUserData)
                type = "text/plain"
            }
        Intent.createChooser(share, "Share to").apply {
            startActivity(this)
        }
    }

    companion object {
        const val DATA_RECIPE = "data_recipe"
    }

    override fun onResume() {
        super.onResume()

        val recipeData = intent.getParcelableExtra<Recipe>(DATA_RECIPE) as Recipe
        getListComment(recipeData.nameRecipe)
        getFavoriteByNameRecipe(recipeData, recipeData.emailUser, recipeData.nameRecipe)
        binding.btnFavorite
    }
}