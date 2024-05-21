package com.amf.amflix.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ItemTvShowBinding
import com.amf.amflix.retrofit.models.series.TVSeries
import com.bumptech.glide.Glide

class TvShowAdapter(private val onClick: (TVSeries) -> Unit) : ListAdapter<TVSeries, TvShowAdapter.TvShowViewHolder>(TvShowDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding = ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TvShowViewHolder(private val binding: ItemTvShowBinding, private val onClick: (TVSeries) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TVSeries) {
            binding.tvShowTitle.text = tvShow.name
            Glide.with(binding.tvShowPoster.context)
                .load(Constants.IMAGE_BASE_URL + tvShow.poster_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.error_image)
                .into(binding.tvShowPoster)

            binding.root.setOnClickListener {
                onClick(tvShow)
            }
        }
    }
}

class TvShowDiffCallback : DiffUtil.ItemCallback<TVSeries>() {
    override fun areItemsTheSame(oldItem: TVSeries, newItem: TVSeries): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TVSeries, newItem: TVSeries): Boolean {
        return oldItem == newItem
    }
}