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
import com.amf.amflix.retrofit.models.Crew.Crew
import com.bumptech.glide.Glide

class CrewAdapter(private val crewList: List<Crew>,
                  private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrewAdapter.CrewViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val crewMember = crewList[position]
        holder.bind(crewMember)
    }

    override fun getItemCount() = crewList.size

    class CrewViewHolder(
        private val binding: ItemCastBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crewMember: Crew) {
            binding.personName.text = crewMember.name
            binding.personRole.text = crewMember.job
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL + crewMember.profile_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.noperson)
                .into(binding.personImg)

            binding.root.setOnClickListener {
                onItemClick(crewMember.id)
            }
        }
    }
}
