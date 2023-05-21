package com.android.photosapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.photosapp.R
import com.android.photosapp.databinding.AdapterItemImageBinding
import com.android.photosapp.viewHolder.ImageVH
import com.bumptech.glide.Glide

/**
 * Created by Aashis on 5/20/2023.
 */
class ImageAdapter(var images: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageVH(
        AdapterItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) {}


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ImageVH).binding
        val imageUri = images[position]
        Glide.with(holder.itemView).load(imageUri)
            .into(binding.imageView)
        binding.indexTextView.text = (position).toString()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}

