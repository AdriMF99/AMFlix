package com.amf.amflix.ui.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.series.TVSeries
import com.bumptech.glide.Glide

class SearchAdaptertv(private val onClick: (TVSeries) -> Unit) : RecyclerView.Adapter<SearchAdaptertv.SearchtvViewHolder>() {

    private var itemList: List<TVSeries> = listOf()

    fun setItems(items: List<TVSeries>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchtvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return SearchtvViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SearchtvViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchtvViewHolder(itemView: View, private val onClick: (TVSeries) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        private val poster: ImageView = itemView.findViewById(R.id.movie_poster)

        fun bind(tvSeries: TVSeries) {
            title.text = tvSeries.name
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL + tvSeries.poster_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.no_img)
                .into(poster)

            itemView.rootView.setOnClickListener {
                onClick(tvSeries)
            }
        }
    }
}
