package com.example.cv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

class Profile : Fragment(R.layout.fragment_profile) {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

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
        }
    }
}