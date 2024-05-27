package com.amf.amflix.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PosterAdapter(private val posterUrls: List<String>) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val posterUrl = posterUrls[position]
        Glide.with(holder.itemView)
            .load(posterUrl)
            .transform(RoundedCorners(40))
            .into(holder.posterImageView)
    }

    override fun getItemCount() = posterUrls.size

    class PosterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterImageView: ImageView = view.findViewById(R.id.posterImageView)
    }
}
