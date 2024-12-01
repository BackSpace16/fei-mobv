package com.example.cv2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cv2.databinding.FragmentUserdetailBinding

class UserDetail : Fragment(R.layout.fragment_userdetail) {
    private var binding: FragmentUserdetailBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserdetailBinding.bind(view)

        // Získaj údaje odoslané ako argumenty
        val userName = arguments?.getString("userName") ?: "Unknown"
        val profileUrl = arguments?.getString("profileUrl") ?: ""

        // Nastav údaje do UI
        binding?.apply {
            userNameText.text = userName
            // Nastav profilovku, napr. cez Glide alebo Picasso
            Glide.with(this@UserDetail)
                .load(profileUrl)
                .placeholder(R.drawable.profile) // Defaultný obrázok
                .into(userProfileImage)
        }

        val feedAdapter = FeedAdapter { user ->
            val bundle = Bundle().apply {
                putString("userName", user.name)
                putString("profileUrl", "https://upload.mcomputing.eu/${user.photo}") // Kompletná URL obrázka
            }

            findNavController().navigate(R.id.action_feed_to_userDetail, bundle)
        }
    }
}
