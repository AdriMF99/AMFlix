package com.amf.amflix.ui.series

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.FragmentTvSeriesItemBinding
import com.amf.amflix.retrofit.models.series.TVSeries

class TVSeriesRecyclerViewAdapter() : RecyclerView.Adapter<TVSeriesRecyclerViewAdapter.ViewHolder>() {

    private var tvSeries: List<TVSeries> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTvSeriesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tvSeries[position]
        holder.tvTitle.text = item.name
        holder.tvVotes.text = item.vote_count.toString()
        holder.tvSynopsis.text = item.overview
        holder.tvRelease.text = item.first_air_date
        holder.tvRating.text = item.vote_average.toString()
        holder.ivPoster.load(Constants.IMAGE_ORIGINAL_BASE_URL + item.backdrop_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }
        holder.ivPosterMini.load(Constants.IMAGE_BASE_URL + item.poster_path){
            crossfade(true)
            placeholder(com.amf.amflix.R.drawable.placeholder_load)
        }
        holder.ivBackdrop.load(Constants.IMAGE_BASE_URL + item.backdrop_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }
        //val blur = RenderEffect.createBlurEffect(50F, 50F, Shader.TileMode.CLAMP)
        //holder.ivBackdrop.setRenderEffect(blur)

    }

    override fun getItemCount(): Int = tvSeries.size
    fun setData(topTVSeries: List<TVSeries>?){
        tvSeries = topTVSeries!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentTvSeriesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivBackdrop: ImageView = binding.backPoster
        val ivPoster: ImageView = binding.poster
        val ivPosterMini: ImageView = binding.posterMini
        val tvTitle: TextView = binding.title
        val tvVotes: TextView = binding.voteCount
        val tvSynopsis: TextView = binding.description
        val tvRelease: TextView = binding.releaseDate
        val tvRating : TextView = binding.rating

    }

}