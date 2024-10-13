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
            MyItem(R.drawable.list,"Prečo je Android lepší ako Windows Phone - povedali nám odborníci z STU"),
            MyItem(R.drawable.list,"ŠOKUJÚCE! Ako RÝCHLO tento mladík vie rozoznať drozda od sýkorky!"),
            MyItem(R.drawable.list,"Nová pracovná pozícia Vrátnik / Cvičiaci - profesia"),
            MyItem(R.drawable.list,"Významná slovenská univerzita sa umiestnila na chvoste svetového rebríčka!"),
            MyItem(R.drawable.list,"ZOSTALA ZHROZENÁ! Pozrite čo objavila mladá Prešovčanka na balkóne!"),
            MyItem(R.drawable.list,"Top 10 dôvodov prečo by ste práve VY mali prestať v tom čo robíte!"),
        ))
    }
}