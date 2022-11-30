package com.danda.danda.ui.detailrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ActivityDetailRecipeBinding
import com.danda.danda.model.dataclass.Recipe

class DetailRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeData = intent.getParcelableExtra<Recipe>(DATA_RECIPE) as Recipe

        binding.apply {
            Glide.with(applicationContext)
                .load(recipeData.imgUrl)
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(ivFoodDetail)

            tvNameRecipe.text = recipeData.nameRecipe
            tvBahan.text = recipeData.ingredients
            tvAlat.text = recipeData.tools
            tvCaraMasak.text = recipeData.howToCook
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val DATA_RECIPE = "data_recipe"
    }
}