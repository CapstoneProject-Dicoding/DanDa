package com.danda.danda.ui.detailrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danda.danda.R
import com.danda.danda.databinding.ActivityDetailRecipeBinding
import com.danda.danda.model.dataclass.User

class DetailRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.DanDa_Recipe)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}