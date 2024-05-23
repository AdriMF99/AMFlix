package com.amf.amflix.ui.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import com.bumptech.glide.Glide

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var itemList: List<Movie> = listOf()

    fun setItems(items: List<Movie>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        private val poster: ImageView = itemView.findViewById(R.id.movie_poster)

        fun bind(movieOrSeries: Movie) {
            title.text = movieOrSeries.title

            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL + movieOrSeries.poster_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.no_img)
                .into(poster)
        }
    }
}
