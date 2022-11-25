package com.danda.danda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.danda.danda.databinding.FragmentHomeBinding
import com.danda.danda.ui.addrecipe.AddRecipeFragment
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            binding.cobacoba.text = Constants.DATA_URL_IMAGE
        } else {
            binding.cobacoba.text = "belum login"
        }

        getBanner()

    }

    private fun getBanner() {
        homeViewModel.banner.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Result.Loading -> {}
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val data = status.data
                    val imageList = ArrayList<SlideModel>()
                    for (img in data) {
                        imageList.add(SlideModel(img, ScaleTypes.FIT))
                    }
                    binding.imageSliderHome.setImageList(imageList)
                }
            }
        }

        homeViewModel.getBanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}