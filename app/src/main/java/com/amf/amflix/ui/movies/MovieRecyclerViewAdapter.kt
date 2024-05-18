package com.amf.amflix.ui.movies

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.FragmentMovieItemBinding
import com.amf.amflix.retrofit.models.movies.Movie

class MovieRecyclerViewAdapter(private val values: List<Movie>) : RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>() {

    private var movies: List<Movie> = ArrayList()
    var click: ((Int, Movie) -> Unit)? = null

    init {
        movies = values
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movies[position]
        // Establece el los elementos de texto de la pelicula
        holder.tvTitle.text = item.title
        holder.tvVotes.text = item.vote_count.toString()
        holder.tvSynopsis.text = item.overview
        holder.tvRelease.text = item.release_date
        holder.tvRating.text = item.vote_average.toString()
        // Carga las imagenes de la película
        holder.ivPoster.load(Constants.IMAGE_ORIGINAL_BASE_URL + item.backdrop_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }
        holder.ivPosterMini.load(Constants.IMAGE_BASE_URL + item.poster_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }
        holder.ivBackdrop.load(Constants.IMAGE_BASE_URL + item.backdrop_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }

        holder.itemView.setOnClickListener {
            click?.invoke(position, item)
        }
    }
    override fun getItemCount(): Int = movies.size

    /**
     * Actualiza los datos del adaptador.
     *
     * @param popularMovies La nueva lista de películas.
     */
    fun setData(popularMovies: List<Movie>?) {
        movies = popularMovies ?: listOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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