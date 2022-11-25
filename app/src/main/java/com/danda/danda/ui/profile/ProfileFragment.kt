package com.danda.danda.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.danda.danda.databinding.FragmentProfileBinding
import com.danda.danda.ui.editprofile.EditProfileActivity
import com.danda.danda.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.danda.danda.util.Result
import com.danda.danda.util.showToast

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLogoutTv.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.imageButton.setOnClickListener {
            startActivity((Intent(requireContext(),EditProfileActivity::class.java)))
        }
        viewModel.getUser.observe(viewLifecycleOwner){user->
            when(user){
                is Result.Success->{
                    binding.profileUsernameTv.text = user.data?.displayName
                }is Result.Failure ->{
                binding.profileUsernameTv.text = user.error.toString()
                }else->{
                    requireActivity().showToast("bang jago")
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}