package com.jwilder.alamo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jwilder.alamo.remote.Venue

class VenueAdapter() : RecyclerView.Adapter<VenueAdapter.ViewHolder>() {

    private var dataSet = listOf<Venue>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val distanceTextView: TextView
        val nameTextView: TextView
        val categoryTextView: TextView
        val iconImageView: ImageView
        val favoriteImageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            distanceTextView = view.findViewById(R.id.venueDistance)
            nameTextView = view.findViewById(R.id.venueName)
            categoryTextView = view.findViewById(R.id.venueCategory)
            iconImageView = view.findViewById(R.id.venueIcon)
            favoriteImageView = view.findViewById(R.id.favoriteIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.venue_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(dataSet[position]) {
            holder.nameTextView.text = this.name
            holder.distanceTextView.text = this.location.distance.toString()
            this.categories.firstOrNull()?.let {
                holder.categoryTextView.text = it.name
                // TODO: Resolve image loading issue
                Glide.with(holder.iconImageView).load("${it.icon.prefix}${it.icon.suffix}")
                    .into(holder.iconImageView)
            }
            // TODO: Add selection/deselection onClick
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateData(data: List<Venue>) {
        this.dataSet = data
        notifyDataSetChanged()
    }
}