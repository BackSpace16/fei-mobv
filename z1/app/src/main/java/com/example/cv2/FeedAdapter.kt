package com.example.cv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class MyItem(val imageResource: Int, val text: String)

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private var items: List<MyItem> = listOf()

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.item_image).setImageResource(items[position].imageResource)
        holder.itemView.findViewById<TextView>(R.id.item_text).text = items[position].text
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<MyItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}