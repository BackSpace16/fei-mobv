package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cv2.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment(R.layout.fragment_login) {

    private lateinit var viewModel: AuthViewModel
    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->
            viewModel.loginResult.observe(viewLifecycleOwner) {
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
                    requireView().findNavController().navigate(R.id.login_to_feed)
                } ?: PreferenceData.getInstance().putUser(requireContext(), null)
            }
        }

            /*viewModel.userResult.observe(viewLifecycleOwner){
                if (it.second != null){
                    requireView().findNavController().navigate(R.id.)
                } else{
                    Snackbar.make(
                        view.findViewById(R.id.submitButton),
                        it.first,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }*/


        /*viewModel.userResult.observe(viewLifecycleOwner) {
            it?.let { user ->
                PreferenceData.getInstance().putUser(requireContext(), user)
                requireView().findNavController().navigate(R.id.to_feed)
            } ?: PreferenceData.getInstance().putUser(requireContext(), null)
        }*/

        /*view.findViewById<MaterialButton>(R.id.submitButton).apply {
            setOnClickListener {
                viewModel.loginUser(
                    view.findViewById<EditText>(R.id.editText1).text.toString(),
                    view.findViewById<EditText>(R.id.editText2).text.toString()
                )
            }
        }*/

        view.findViewById<TextInputLayout>(R.id.input1).setHint(getString(R.string.username).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input2).setHint(getString(R.string.password).replaceFirstChar{it.titlecase()})
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
