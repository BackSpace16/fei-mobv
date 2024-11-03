package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment(R.layout.fragment_login) {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val usernameField = view.findViewById<TextInputEditText>(R.id.editText1)


        view.findViewById<TextInputLayout>(R.id.input1).setHint(getString(R.string.username).replaceFirstChar{it.titlecase()})
        view.findViewById<TextInputLayout>(R.id.input2).setHint(getString(R.string.password).replaceFirstChar{it.titlecase()})

        view.findViewById<MaterialButton>(R.id.submitButton).setOnClickListener { view ->
            val username = usernameField?.text.toString()
            sharedViewModel.username.value = username

            view.findNavController().navigate(R.id.to_feed)
        }
    }
}
