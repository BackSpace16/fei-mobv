package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class Registration : Fragment(R.layout.fragment_registration) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText1 = view.findViewById<EditText>(R.id.editText1)
        val editText2 = view.findViewById<EditText>(R.id.editText2)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val email = editText1?.text.toString()  // Bezpečné volanie s ?
            val username = editText2?.text.toString()

            Log.d("RegistrationFragment", "Zadaný text: $email, $username")
        }
    }
}