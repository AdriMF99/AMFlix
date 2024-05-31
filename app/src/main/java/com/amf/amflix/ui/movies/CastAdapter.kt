package com.amf.amflix.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ItemCastBinding
import com.amf.amflix.retrofit.models.Cast.Cast
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.bumptech.glide.Glide

class CastAdapter(
    private val castList: List<Cast>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.bind(castMember)
    }

    override fun getItemCount() = castList.size

    class CastViewHolder(
        private val binding: ItemCastBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(castMember: Cast) {
            binding.personName.text = castMember.name
            binding.personRole.text = castMember.character
            Glide.with(binding.root.context)
                .load(Constants.IMAGE_BASE_URL + castMember.profile_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.error_image)
                .into(binding.personImg)

            binding.root.setOnClickListener {
                onItemClick(castMember.id)
            }
        }
    }
}
