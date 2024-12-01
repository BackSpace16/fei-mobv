package com.example.cv2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController

class Menu(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private val rootView: View

    init {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_menu, this, true)

        rootView.findViewById<ImageView>(R.id.map).setOnClickListener { view ->
            view.findNavController().navigate(R.id.to_map)
        }
        rootView.findViewById<ImageView>(R.id.feed).setOnClickListener { view ->
            view.findNavController().navigate(R.id.to_feed)
        }
        rootView.findViewById<ImageView>(R.id.profile).setOnClickListener { view ->
            view.findNavController().navigate(R.id.to_profile)
        }
    }
}