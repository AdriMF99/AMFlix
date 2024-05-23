package com.amf.amflix.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Crew.Crew
import com.bumptech.glide.Glide

class CrewAdapter(private val crewList: List<Crew>) : RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return CrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val crewMember = crewList[position]
        holder.bind(crewMember)
    }

    override fun getItemCount() = crewList.size

    class CrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val personName: TextView = itemView.findViewById(R.id.personName)
        private val personRole: TextView = itemView.findViewById(R.id.personRole)
        private val personImg: ImageView = itemView.findViewById(R.id.personImg)

        fun bind(crewMember: Crew) {
            personName.text = crewMember.name
            personRole.text = crewMember.job
            Glide.with(itemView.context)
                .load(Constants.IMAGE_BASE_URL + crewMember.profile_path)
                .placeholder(R.drawable.placeholder_load)
                .error(R.drawable.error_image)
                .into(personImg)
        }
    }
}
