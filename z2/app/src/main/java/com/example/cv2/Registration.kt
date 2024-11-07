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
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class Registration : Fragment(R.layout.fragment_registration) {

    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel>create(modelClass: Class<T>): T {
                return AuthViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[AuthViewModel::class.java]

        viewModel.userResult.observe(viewLifecycleOwner){
            if (it.second != null){
                requireView().findNavController().navigate(R.id.to_feed)
            } else{
                Snackbar.make(
                    view.findViewById(R.id.submitButton),
                    it.first,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        view.findViewById<TextView>(R.id.submitButton).apply {
            setOnClickListener {
                viewModel.registerUser(
                    view.findViewById<EditText>(R.id.editText1).text.toString(),
                    view.findViewById<EditText>(R.id.editText2).text.toString(),
                    view.findViewById<EditText>(R.id.editText3).text.toString(),
                    view.findViewById<EditText>(R.id.editText4).text.toString()
                )
            }
        }

        view.findViewById<TextInputLayout>(R.id.input2).setHint(getString(R.string.username).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input1).setHint("e-mail".replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input3).setHint(getString(R.string.password).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input4).setHint(getString(R.string.confirm) + " " + getString(R.string.password))
    }
}