package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

class Intro : Fragment(R.layout.fragment_intro) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.button1).setOnClickListener { view ->
            view.findNavController().navigate(R.id.intro_to_register)
        }

        view.findViewById<MaterialButton>(R.id.button2).setOnClickListener { view ->
            view.findNavController().navigate(R.id.intro_to_login)
        }
    }

}
