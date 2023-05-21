package com.android.photosapp.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.android.photosapp.databinding.AdapterItemImageBinding

/**
 * Created by Aashis on 5/21/2023.
 */
class ImageVH(
    val binding: AdapterItemImageBinding,
    private val onUserClicked: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root)