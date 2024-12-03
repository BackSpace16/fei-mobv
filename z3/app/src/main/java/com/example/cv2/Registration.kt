package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cv2.databinding.FragmentRegistrationBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class Registration : Fragment(R.layout.fragment_registration) {
    private lateinit var viewModel: AuthViewModel
    private var binding: FragmentRegistrationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel>create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegistrationBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->

            viewModel.registrationResult.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        bnd.submitButton,
                        it,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.userResult.observe(viewLifecycleOwner) {
                it?.let { user ->
                    PreferenceData.getInstance().putUser(requireContext(), user)
                    requireView().findNavController().navigate(R.id.register_to_feed)
                } ?: PreferenceData.getInstance().putUser(requireContext(), null)
            }
        }

        view.findViewById<TextInputLayout>(R.id.input2).setHint(getString(R.string.username).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input1).setHint("e-mail".replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input3).setHint(getString(R.string.password).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input4).setHint(getString(R.string.confirm) + " " + getString(R.string.password))
    }
}