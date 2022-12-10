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

        binding.imageButton.setOnClickListener {
            startActivity((Intent(requireContext(), FavoriteActivity::class.java)))
        }

        getUser()
        checkLogout()
        listNavigation()

    }

    private fun getUser() {
        viewModel.getUser.observe(viewLifecycleOwner){user->
            when(user){
                is Result.Success->{
                    binding.profileNameTv.text = user.data?.displayName
                    binding.emailTv.text=user.data?.email.toString()
                    Glide.with(requireContext())
                        .load(user.data?.photoUrl)
                        .into(binding.profileIv)
                    viewModel.getUserFromFireStore(user.data?.email.toString())
                    resultFireStore()
                }
                is Result.Failure -> {
                    binding.profileNameTv.text = user.error.toString()
                }
                else->{
                    requireActivity().showToast("bang jago")
                }
            }
        }
    }
    private fun resultFireStore(){
        viewModel.getFromUser.observe(requireActivity()){fireStore->
            when(fireStore){
                is Result.Success->{
                    binding.profileUsernameTv.text = fireStore.data?.username
                }
                is Result.Failure -> {

                }
                else->{
                    requireActivity().showToast("bang jago")
                }
            }
        }
    }

    private fun checkLogout() = binding.apply {
        viewModel.getUser.observe(viewLifecycleOwner) {
            when(it){
                is Result.Success -> {
                    if (it.data?.email == null) {
                        loginLogoutTv.text = "Login"
                        loginLogoutBt.setImageResource(R.drawable.ic_baseline_login_24)
                        login()
                    } else {
                        binding.loginLogoutTv.text = "Logout"
                        loginLogoutBt.setImageResource(R.drawable.ic_baseline_logout_24)
                        logout()
                    }
                }
                else -> {}
            }
        }
    }

    private fun listNavigation(){
        val listView = binding.buttonList
        val listButton: ArrayList<ListButton> = ArrayList()
        listButton.add(ListButton("edit profile", R.drawable.ic_profile))
        listButton.add(ListButton("edit password",R.drawable.ic_baseline_login_24))
        //add lagi kalau mau nambah
        listView.adapter = CustomProfileAdapter(requireContext(),listButton)
        listView.setOnItemClickListener { _, _, position, _ ->
            when(position) {
                0 -> {
                    startActivity(Intent(requireContext(), EditProfileActivity::class.java))
                }
                1 -> {
                    val intentToEditPassword = Intent(requireContext(),ChangePasswordActivity::class.java)
                    startActivity(intentToEditPassword)
                }
            }
        }
    }

    private fun login() {
        binding.loginLogoutTv.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    private fun logout() {
        binding.loginLogoutTv.setOnClickListener {
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