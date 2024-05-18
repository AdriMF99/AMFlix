package com.amf.amflix.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Cast.Cast
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.bumptech.glide.Glide

class CastAdapter(private val castList: List<Cast>) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.bind(castMember)
    }

    override fun getItemCount() = castList.size

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val personName: TextView = itemView.findViewById(R.id.personName)
        private val personRole: TextView = itemView.findViewById(R.id.personRole)
        private val personImg: ImageView = itemView.findViewById(R.id.personImg)

        fun bind(castMember: Cast) {
            personName.text = castMember.name
            personRole.text = castMember.character
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL + castMember.profile_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.error_image)
                .into(personImg)
        }
    }
}
