package com.amf.amflix.ui.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ItemMovieBinding
import com.amf.amflix.retrofit.models.movies.Movie
import com.bumptech.glide.Glide

class MovieAdapter(private val onClick: (Movie) -> Unit) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    class MovieViewHolder(private val binding: ItemMovieBinding, private val onClick: (Movie) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title

            val posterPath = movie.poster_path
            if (posterPath != null) {
                Glide.with(binding.moviePoster.context)
                    .load(Constants.IMAGE_BASE_URL + posterPath)
                    .placeholder(R.drawable.placeholder_load)
                    .error(R.drawable.error_image)
                    .into(binding.moviePoster)
            }

            binding.root.setOnClickListener {
                onClick(movie)
            }
        }
    }
}
