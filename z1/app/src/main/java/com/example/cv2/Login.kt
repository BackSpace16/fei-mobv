package com.example.cv2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

class Login : Fragment(R.layout.fragment_login) {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val usernameField = view.findViewById<EditText>(R.id.editText1)

        view.findViewById<Button>(R.id.submitButton).setOnClickListener { view ->
            val username = usernameField?.text.toString()
            sharedViewModel.username.value = username

            view.findNavController().navigate(R.id.to_feed)
        }
    }
}
