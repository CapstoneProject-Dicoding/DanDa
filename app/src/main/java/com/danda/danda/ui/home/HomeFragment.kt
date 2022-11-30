package com.danda.danda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.danda.danda.databinding.FragmentHomeBinding
import com.danda.danda.util.Result
import com.danda.danda.util.showLoading
import com.danda.danda.util.showToast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeListAdapter: HomeListAdapter by lazy(::HomeListAdapter)
    private val homeViewModel by viewModels<HomeViewModel>()

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

        getBanner()
        getListRecipe()

    }

    private fun getListRecipe() {
        binding.rvHome.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = homeListAdapter
            setHasFixedSize(true)
        }

        homeViewModel.recipe.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Result.Failure -> {
                    showLoading(false, binding.progressBarHome)
                    requireActivity().showToast(status.error.toString())
                }
                is Result.Loading -> showLoading(true, binding.progressBarHome)
                is Result.Success -> {
                    showLoading(false, binding.progressBarHome)
                    homeListAdapter.setListRecipe(status.data)
                }
            }
        }

        homeViewModel.getListRecipe()
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