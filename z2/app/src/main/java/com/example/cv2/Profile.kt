package com.example.cv2

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cv2.databinding.FragmentProfileBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class Profile : Fragment(R.layout.fragment_profile) {


    private lateinit var viewModel: ProfileViewModel
    private lateinit var authViewModel: AuthViewModel
    private var binding: FragmentProfileBinding? = null

    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                viewModel.sharingLocation.postValue(false)
            }
        }

    fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[ProfileViewModel::class.java]

        authViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val usernameText = view.findViewById<TextView>(R.id.username)
        val emailText = view.findViewById<TextView>(R.id.email)
        sharedViewModel.username.observe(viewLifecycleOwner) { userInput ->
            usernameText.text = userInput
        }
        sharedViewModel.email.observe(viewLifecycleOwner) { userInput ->
            emailText.text = userInput
        }

        view.findViewById<MaterialButton>(R.id.logout).setOnClickListener { view ->
            view.findNavController().navigate(R.id.logout)
        }*/

        binding = FragmentProfileBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->

            bnd.loadProfileBtn.setOnClickListener {
                val user = PreferenceData.getInstance().getUser(requireContext())
                user?.let {
                    viewModel.loadUser(it.id)
                }
            }

            bnd.logoutBtn.setOnClickListener {
                PreferenceData.getInstance().clearData(requireContext())
                authViewModel.logoutUser()
                it.findNavController().navigate(R.id.logout)
            }

            viewModel.profileResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        bnd.loadProfileBtn,
                        it,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.sharingLocation.postValue(
                PreferenceData.getInstance().getSharing(requireContext())
            )

            viewModel.sharingLocation.postValue(
                PreferenceData.getInstance().getSharing(requireContext())
            )

            viewModel.sharingLocation.observe(viewLifecycleOwner) {
                it?.let {
                    if (it) {
                        if (!hasPermissions(requireContext())) {
                            viewModel.sharingLocation.postValue(false)
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        } else {
                            PreferenceData.getInstance().putSharing(requireContext(), true)
                        }
                    } else {
                        PreferenceData.getInstance().putSharing(requireContext(), false)
                    }
                }
            }
        }
    }
}