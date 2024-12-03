package com.example.cv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FeedAdapter (
    val onItemClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var items: List<UserEntity> = listOf()

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val user = items[position]
        holder.itemView.findViewById<TextView>(R.id.item_text).text = items[position].name

        val imageView: ImageView = holder.itemView.findViewById(R.id.item_image)

        Glide.with(holder.itemView.context)
            .load("https://upload.mcomputing.eu/${user.photo}")
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(imageView)

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<UserEntity>) {
        val diffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}