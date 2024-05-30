package com.amf.amflix.ui.Search

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class SearchAdapter(private val onClick: (Movie) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var itemList: List<Movie> = listOf()

    fun setItems(items: List<Movie>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchViewHolder(itemView: View, private val onClick: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_name)
        private val poster: ImageView = itemView.findViewById(R.id.item_poster)
        private val titleBackground: View = itemView.findViewById(R.id.title_background)

        fun bind(movieOrSeries: Movie) {
            title.text = movieOrSeries.title

            Glide.with(itemView.context)
                .asBitmap()
                .load(Constants.IMAGE_BASE_URL + movieOrSeries.poster_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.no_img)
                .into(object : BitmapImageViewTarget(poster) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        Palette.from(resource).generate { palette ->
                            val dominantColor = palette?.getDominantColor(0x99000000.toInt()) ?: 0x99000000.toInt()
                            titleBackground.setBackgroundColor(dominantColor)
                        }
                    }
                })

            itemView.rootView.setOnClickListener {
                onClick(movieOrSeries)
            }
        }
    }
}