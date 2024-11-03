package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
        editText1.hint = getString(R.string.enter) + " email"
        editText2.hint = getString(R.string.enter) + " " + getString(R.string.username)
        val editText3 = view.findViewById<EditText>(R.id.editText3)
        editText3.hint = getString(R.string.enter) + " " + getString(R.string.password)
        val editText4 = view.findViewById<EditText>(R.id.editText4)
        editText4.hint = getString(R.string.confirm) + " " + getString(R.string.password)

        view.findViewById<TextView>(R.id.label2).setText(getString(R.string.username).replaceFirstChar{it.titlecase()} + ":")
        view.findViewById<TextView>(R.id.label3).setText(getString(R.string.password).replaceFirstChar{it.titlecase()} + ":")
        view.findViewById<TextView>(R.id.label4).setText(getString(R.string.confirm) + " " + getString(R.string.password) + ":")

        view.findViewById<MaterialButton>(R.id.submitButton).setOnClickListener { view ->
            val email = editText1?.text.toString()
            val username = editText2?.text.toString()
            sharedViewModel.email.value = email

            Log.d("RegistrationFragment", "Zadaný text: $email, $username")

            view.findNavController().navigate(R.id.register_to_login)
        }
    }
}