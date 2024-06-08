package com.amf.amflix.ui.signin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.amf.amflix.R

class ImageAdapter(private val context: Context, private val images: List<Int>) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        imageView.setImageResource(images[position])
        return view
    }
}
