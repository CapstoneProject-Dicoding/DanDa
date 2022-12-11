package com.danda.danda.ui.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.FragmentProfileBinding
import com.danda.danda.ui.change.ChangePasswordActivity
import com.danda.danda.ui.editprofile.EditProfileActivity
import com.danda.danda.ui.favorite.FavoriteActivity
import com.danda.danda.ui.login.LoginActivity
import com.danda.danda.ui.resepmasakanku.ResepMasakankuActivity
import dagger.hilt.android.AndroidEntryPoint
import com.danda.danda.util.Result
import com.danda.danda.util.showToast

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        checkLogout()

    }

    private fun getUser() {
        viewModel.getUser.observe(viewLifecycleOwner){user->
            when(user){
                is Result.Success->{
                    if (user.data?.email != null) {
                        binding.profileNameTv.text = user.data.displayName
                        binding.emailTv.text=user.data.email.toString()
                        Glide.with(requireContext())
                            .load(user.data.photoUrl)
                            .into(binding.profileIv)
                        viewModel.getUserFromFireStore(user.data.email.toString())
                        resultFireStore()
                        setUpAction(user.data.email.toString())
                    } else {
                        binding.emailTv.text = "-"
                        binding.profileNameTv.text = "-"
                        setUpAction(null)
                    }
                }
                is Result.Failure -> {
                    binding.profileNameTv.text = user.error.toString()
                }
                else->{}
            }
        }
    }

    private fun resultFireStore(){
        viewModel.getFromUser.observe(requireActivity()){fireStore->
            when(fireStore){
                is Result.Success->{
                    if (fireStore.data?.username != null) {
                        binding.profileUsernameTv.text = fireStore.data.username
                    } else {
                        binding.profileUsernameTv.text = "-"
                    }
                }
                is Result.Failure -> {}
                else->{}
            }
        }
    }

    private fun checkLogout() = binding.apply {
        viewModel.getUser.observe(viewLifecycleOwner) {
            when(it){
                is Result.Success -> {
                    if (it.data?.email == null) {
                        tvLogoutLogin.text = "Login"
                        imageView7.setImageResource(R.drawable.ic_baseline_login_24)
                        login()
                    } else {
                        binding.tvLogoutLogin.text = "Logout"
                        imageView7.setImageResource(R.drawable.ic_baseline_logout_24)
                        logout()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setUpAction (email: String?) {
        if (email.isNullOrEmpty()) {
            binding.tvEditProfile.setOnClickListener {
                loginHere()
            }

            binding.tvChangePassword.setOnClickListener {
                loginHere()
            }

            binding.tvResepMasakanku.setOnClickListener {
                loginHere()
            }

            binding.tvInformasi.setOnClickListener {
                //
            }

            binding.imageButtonFavorite.setOnClickListener {
                loginHere()
            }
        } else {
            binding.tvEditProfile.setOnClickListener {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }

            binding.tvChangePassword.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }

            binding.tvResepMasakanku.setOnClickListener {
                startActivity(Intent(requireContext(), ResepMasakankuActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }

            binding.tvInformasi.setOnClickListener {
                //
            }

            binding.imageButtonFavorite.setOnClickListener {
                startActivity((Intent(requireContext(), FavoriteActivity::class.java)))
            }
        }
    }

    private fun loginHere() {
        AlertDialog.Builder(requireContext())
            .setTitle("KONFIRMASI LOGIN")
            .setMessage("Anda belum login, mau login sekarang?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun login() {
        binding.tvLogoutLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    private fun logout() {
        binding.tvLogoutLogin.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("KONFIRMASI LOGOUT")
                .setMessage("Apakah kamu yakin logout?")

                .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                    viewModel.logout()
                    requireActivity().showToast("Berhasil logout")
                    dialogInterface.dismiss()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    requireActivity().finish()
                }

                .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                    requireActivity().showToast("Tidak jadi logout")
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        checkLogout()
        getUser()
    }
    companion object{
        private var email = ""
    }
}