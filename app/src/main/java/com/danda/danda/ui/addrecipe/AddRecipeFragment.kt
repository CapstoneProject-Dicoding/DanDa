package com.danda.danda.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.danda.danda.databinding.FragmentAddRecipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addRecipeViewModel =
            ViewModelProvider(this).get(AddRecipeViewModel::class.java)

        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        addRecipeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}