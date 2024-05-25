package com.amf.amflix.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.Review.Review
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorTextView: TextView = itemView.findViewById(R.id.author)
        private val contentTextView: TextView = itemView.findViewById(R.id.content)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatar)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating)
        private val ratingnumber: TextView = itemView.findViewById(R.id.ratingNumber)

        fun bind(review: Review) {
            authorTextView.text = review.author_details.name
            contentTextView.text = review.content
            ratingBar.rating = review.author_details.rating ?: 0f
            ratingnumber.text = review.author_details.rating.toString()

            val avatarPath = review.author_details.avatar_path?.let {
                if (it.startsWith("/")) "https://image.tmdb.org/t/p/w500$it" else it
            }
            Glide.with(itemView.context)
                .load(avatarPath)
                .transform(CircleCrop())
                .placeholder(R.drawable.no_img)
                .into(avatarImageView)

            itemView.setOnClickListener {
                if (contentTextView.visibility == View.GONE) {
                    contentTextView.visibility = View.VISIBLE
                } else {
                    contentTextView.visibility = View.GONE
                }
            }
        }
    }
}