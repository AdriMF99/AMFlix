package com.amf.amflix.ui.people

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.FragmentPeopleItemBinding
import com.amf.amflix.retrofit.models.people.KnownFor
import com.amf.amflix.retrofit.models.people.People
import com.amf.amflix.retrofit.models.people.PopularPeopleResponse

class PeopleListRecyclerViewAdapter() : RecyclerView.Adapter<PeopleListRecyclerViewAdapter.ViewHolder>() {

    private var people: List<People> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(
        FragmentPeopleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = people[position]
        holder.tvTitle.text = item.name
        val knownForText = formatKnownFor(item.known_for)
        holder.tvKnownFor.text = knownForText
        holder.ivPoster.load(Constants.IMAGE_BASE_URL + item.profile_path){
            crossfade(true)
            placeholder(R.drawable.placeholder_load)
        }
    }

    private fun formatKnownFor(knownForList: List<KnownFor>): String {
        val nonNullKnownForList = knownForList.filter { knownFor ->
            knownFor.name != null || knownFor.title != null
        }

        val knownForTextList = nonNullKnownForList.mapNotNull { knownFor ->
            val title = knownFor.title
            val name = knownFor.name
            if (title != null && name != null) {
                "$title, $name"
            } else {
                title ?: name
            }
        }

        return knownForTextList.joinToString(", ")
    }
    override fun getItemCount(): Int = people.size

    fun setData(popularPeople: List<People>?){
        people = popularPeople!!
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentPeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivPoster: ImageView = binding.poster
        val tvTitle: TextView = binding.title
        val tvKnownFor: TextView = binding.description
    }

}