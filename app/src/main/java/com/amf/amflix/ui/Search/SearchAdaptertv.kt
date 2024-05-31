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
import com.amf.amflix.retrofit.models.series.TVSeries
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class SearchAdaptertv(private val onClick: (TVSeries) -> Unit) : RecyclerView.Adapter<SearchAdaptertv.SearchtvViewHolder>() {

    private var itemList: List<TVSeries> = listOf()

    fun setItems(items: List<TVSeries>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchtvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchtvViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SearchtvViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchtvViewHolder(itemView: View, private val onClick: (TVSeries) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_name)
        private val poster: ImageView = itemView.findViewById(R.id.item_poster)
        private val titleBackground: View = itemView.findViewById(R.id.title_background)

        fun bind(tvSeries: TVSeries) {
            title.text = tvSeries.name
            Glide.with(itemView.context)
                .asBitmap()
                .load(Constants.IMAGE_BASE_URL + tvSeries.poster_path)
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
                onClick(tvSeries)
            }
        }
    }
}
