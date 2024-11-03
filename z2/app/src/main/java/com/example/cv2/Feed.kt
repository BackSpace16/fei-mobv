package com.example.cv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class Feed : Fragment(R.layout.fragment_feed) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.feed_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val feedAdapter = FeedAdapter()
        recyclerView.adapter = feedAdapter

        feedAdapter.run {
            recyclerView.adapter = this

            updateItems(listOf(
                MyItem("1 osoba"),
                MyItem("2 osoba"),
                MyItem("3 osoba"),
                MyItem("4 osoba"),
                MyItem("5 osoba"),
                MyItem("7 osoba"),
                MyItem("6 osoba"),
            ))
        }

        val snackbar = Snackbar.make(requireActivity().findViewById(R.id.feed), "VYHRAJTE IPHONE 17!!!", Snackbar.LENGTH_LONG)
        snackbar.setAction("IHNEĎ!!!") {
            performAction()
        }
        snackbar.show()
    }

    private fun performAction() {
        Toast.makeText(context, "smola, skúste štastie nabudúce", Toast.LENGTH_SHORT).show()
    }
}