package com.amf.amflix.ui.Favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ItemMovieBinding
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
import com.bumptech.glide.Glide

class FavoriteMoviesAdapter(private var movies: List<Movie>,
                            private val onClick: (Movie) -> Unit) : RecyclerView.Adapter<FavoriteMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(private val binding: ItemMovieBinding, private val onClick: (Movie) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            Glide.with(binding.moviePoster.context)
                .load(Constants.IMAGE_ORIGINAL_BASE_URL + movie.poster_path)
                .error(R.drawable.no_img)
                .into(binding.moviePoster)

            binding.root.setOnClickListener {
                onClick(movie)
            }
        }
    }
}
