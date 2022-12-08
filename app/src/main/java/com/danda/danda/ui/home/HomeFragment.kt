package com.danda.danda.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
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
    private var doubleBackToExit = false

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
        setUpRecyclerView()
        setUpSearchView()
        doubleBackExit()

    }

    private fun setUpSearchView() {
        binding.apply {
            searchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    homeViewModel.searchListRecipe(query)
                    closedKeyboard()
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
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    private fun setUpRecyclerView() = binding.rvHome.apply {
        layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = homeListAdapter
        setHasFixedSize(true)
    }

    private fun getListRecipe() {
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
                is Result.Failure -> {}
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

    private fun closedKeyboard() {
        val view: View? = activity?.currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun doubleBackExit() = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            when {
                doubleBackToExit -> {
                    activity?.finish()
                }
                else -> {
                    getListRecipe()
                    doubleBackToExit = true
                    requireContext().showToast("Tekan sekali lagi untuk keluar")
                    Handler(Looper.getMainLooper()).postDelayed({
                        run {
                            doubleBackToExit = false
                        }
                    }, 2000)
                }
            }
        }
    })

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}