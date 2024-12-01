package com.example.cv2

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cv2.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

class Profile : Fragment(R.layout.fragment_profile) {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var authViewModel: AuthViewModel
    private var binding: FragmentProfileBinding? = null

    private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val PHOTO_PERMISSIONS = if (Build.VERSION.SDK_INT < 33) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    }

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

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { uploadPhoto(it) } ?: run {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { viewModel.uploadPhoto(it) }
        }

        binding?.apply {
            uploadPhotoBtn.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            deletePhotoBtn.setOnClickListener {
                viewModel.deletePhoto()
            }
        }


        binding = FragmentProfileBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->
            val user = PreferenceData.getInstance().getUser(requireContext())
            setupProfile(bnd)
            user?.let {
                viewModel.loadUser(it.id)
            }

            bnd.logoutBtn.setOnClickListener {
                PreferenceData.getInstance().clearData(requireContext())
                authViewModel.logoutUser()
                it.findNavController().navigate(R.id.logout)
            }

            viewModel.profileResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        bnd.root,
                        it,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

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
                    }
                    else {
                        PreferenceData.getInstance().putSharing(requireContext(), false)
                    }
                }
            }
        }
    }

    private fun setupProfile(bnd: FragmentProfileBinding) {
        bnd.uploadPhotoBtn.setOnClickListener {
            if (checkPermissions(PHOTO_PERMISSIONS)) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                requestPermissions(PHOTO_PERMISSIONS)
            }
        }

        bnd.deletePhotoBtn.setOnClickListener {
            viewModel.deletePhoto()
        }

        bnd.logoutBtn.setOnClickListener {
            PreferenceData.getInstance().clearData(requireContext())
            authViewModel.logoutUser()
            it.findNavController().navigate(R.id.logout)
        }

        observeProfileResult(bnd)
        handleLocationSharing(bnd)
    }

    private fun observeProfileResult(bnd: FragmentProfileBinding) {
        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                Snackbar.make(bnd.root, result, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLocationSharing(bnd: FragmentProfileBinding) {
        viewModel.sharingLocation.postValue(
            PreferenceData.getInstance().getSharing(requireContext())
        )

        viewModel.sharingLocation.observe(viewLifecycleOwner) { isSharing ->
            isSharing?.let {
                if (it) {
                    if (!checkPermissions(PERMISSIONS_REQUIRED)) {
                        viewModel.sharingLocation.postValue(false)
                        requestPermissions(PERMISSIONS_REQUIRED)
                    } else {
                        PreferenceData.getInstance().putSharing(requireContext(), true)
                    }
                } else {
                    PreferenceData.getInstance().putSharing(requireContext(), false)
                }
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions(permissions: Array<String>) {
        permissions.forEach {
            requestPermissionLauncher.launch(it)
        }
    }

    private fun uploadPhoto(uri: Uri) {
        val file = inputStreamToFile(uri)
        file?.let {
            Log.d("PhotoUpload", "Photo saved: $it")

            val fileUri = Uri.fromFile(it)
            viewModel.uploadPhoto(fileUri) // Teraz pošleš správny typ Uri
        }
    }

    private fun inputStreamToFile(uri: Uri): File? {
        val resolver = requireContext().contentResolver
        resolver.openInputStream(uri).use { inputStream ->
            val file = File(requireContext().filesDir, "photo_uploaded.jpg")
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
                return file
            }
        }
    }
}