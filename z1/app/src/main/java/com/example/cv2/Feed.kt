package com.example.cv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Feed : Fragment(R.layout.fragment_feed) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.feed_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val feedAdapter = FeedAdapter()
        recyclerView.adapter = feedAdapter
        feedAdapter.updateItems(listOf(
            MyItem(R.drawable.img1,"Prečo je Android lepší ako Windows Phone - povedali nám odborníci z STU"),
            MyItem(R.drawable.img2,"ŠOKUJÚCE! Ako RÝCHLO tento mladík vie rozoznať drozda od sýkorky!"),
            MyItem(R.drawable.img3,"Nová pracovná pozícia Vrátnik / Cvičiaci - profesia"),
            MyItem(R.drawable.img4,"Významná slovenská univerzita sa umiestnila na chvoste svetového rebríčka!"),
            MyItem(R.drawable.img5,"ZOSTALA ZHROZENÁ! Pozrite čo objavila mladá Prešovčanka na balkóne!"),
            MyItem(R.drawable.img6,"Top 10 dôvodov prečo by ste práve VY mali prestať v tom čo robíte!"),
            MyItem(R.drawable.img7,"Doscrollovali ste na koniec? Poradíme vám čo ďalej!"),
        ))
    }
}