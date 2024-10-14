package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

class Registration : Fragment(R.layout.fragment_registration) {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val editText1 = view.findViewById<EditText>(R.id.editText1)
        val editText2 = view.findViewById<EditText>(R.id.editText2)

        view.findViewById<MaterialButton>(R.id.submitButton).setOnClickListener { view ->
            val email = editText1?.text.toString()
            val username = editText2?.text.toString()
            sharedViewModel.email.value = email

            Log.d("RegistrationFragment", "Zadaný text: $email, $username")

            view.findNavController().navigate(R.id.register_to_login)
        }
    }
}