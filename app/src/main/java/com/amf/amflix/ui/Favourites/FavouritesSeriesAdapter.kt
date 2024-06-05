package com.amf.amflix.ui.Favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ItemTvShowBinding
import com.amf.amflix.retrofit.models.series.TVSeries
import com.bumptech.glide.Glide

class FavoriteSeriesAdapter(private var series: List<TVSeries>,
                            private val onClick: (TVSeries) -> Unit) : RecyclerView.Adapter<FavoriteSeriesAdapter.SerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
        val binding = ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SerieViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
        holder.bind(series[position])
    }

    override fun getItemCount() = series.size

    fun updateData(newSeries: List<TVSeries>) {
        series = newSeries
        notifyDataSetChanged()
    }

    class SerieViewHolder(private val binding: ItemTvShowBinding, private val onClick: (TVSeries) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(serie: TVSeries) {
            binding.tvShowTitle.text = serie.name
            Glide.with(binding.tvShowPoster.context)
                .load(Constants.IMAGE_ORIGINAL_BASE_URL + serie.poster_path)
                .error(R.drawable.no_img)
                .into(binding.tvShowPoster)

            binding.root.setOnClickListener {
                onClick(serie)
            }
        }
    }
}
