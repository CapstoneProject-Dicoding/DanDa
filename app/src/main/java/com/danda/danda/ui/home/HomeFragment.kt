package com.danda.danda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.danda.danda.databinding.FragmentHomeBinding
import com.danda.danda.util.Result
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    // firestore
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            binding.textHome.text = user.email
        } else {
            binding.textHome.text = "Anda belum login"
        }

//        apiViewModel.getSellerBanner().observe(viewLifecycleOwner) {
//            when (it.status) {
//                SUCCESS -> {
//                    val data = it.data!!
//                    val list = ArrayList<SlideModel>()
//
//                    for (img in data) {
//                        list.add(SlideModel(img.imageUrl, ScaleTypes.FIT))
//                    }
//
//                    binding.imgSlider.setImageList(list)
//                }
//                ERROR -> {
//
//                }
//                LOADING -> {
//
//                }
//            }
//        }



//        homeViewModel.getBanner()
//        homeViewModel.banner.observe(viewLifecycleOwner) { status ->
//            when (status) {
//                is Result.Loading -> {}
//                is Result.Failure -> {
//                }
//                is Result.Success -> {
//                    val data = status.data
//                    val imageList = ArrayList<SlideModel>()
//                    for (img in data) {
//                        imageList.add(SlideModel(img.url, ScaleTypes.FIT))
//                    }
//                    binding.imageSliderHome.setImageList(imageList)
//                }
//            }
//        }

        val imageList = ArrayList<SlideModel>()

        firestore = FirebaseFirestore.getInstance()
        firestore.collection("image_slider")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    imageList.add(SlideModel(document.getString("url"), ScaleTypes.FIT))
                    binding.imageSliderHome.setImageList(
                        imageList
                    )
                }
            }
            .addOnFailureListener {

            }

//        firestore.collection("image_slider").get()
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    for ()
//                }
//            }
//
//            .addOnFailureListener {
//
//            }


//        val imageList = ArrayList<SlideModel>()

//        imageList.add(SlideModel("https://asset-a.grid.id/crop/0x0:0x0/x/photo/2020/01/30/1299509524.jpg", ScaleTypes.FIT))
//        imageList.add(SlideModel("https://assets.promediateknologi.com/crop/0x0:0x0/x/photo/2021/10/23/975567698.jpg", ScaleTypes.FIT))
//        imageList.add(SlideModel("https://i3.wp.com/any.web.id/wp-content/uploads/2017/05/Pegunungan.jpg", ScaleTypes.FIT))
//        imageList.add(SlideModel("https://blue.kumparan.com/image/upload/fl_progressive,fl_lossy,c_fill,q_auto:best,w_640/v1631074757/photo-1530878902700-5ad4f9e4c318_wqnmpp.jpg", ScaleTypes.FIT))
//
//        binding.imageSliderHome.setImageList(
//            imageList
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}