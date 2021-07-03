package com.example.pixar.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pixar.databinding.ItemSearchImageBinding
import com.example.pixar.model.PixabayPhoto

class PixabayPhotoAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<PixabayPhoto, PixabayPhotoAdapter.PixabayPhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayPhotoViewHolder {
        val binding =
            ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PixabayPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PixabayPhotoViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)

    }

    inner class PixabayPhotoViewHolder(private val binding: ItemSearchImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.onItemClicked(item)
                }
            }
        }

        fun bind(photo: PixabayPhoto) {

            Glide.with(itemView).load(photo.webformatURL)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.username.isVisible = false
                        binding.imageError.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageError.isVisible = false
                        binding.username.isVisible = true
                        binding.username.text = photo.user
                        return false
                    }
                })
                .into(binding.image)

        }

    }

    interface OnItemClickListener {
        fun onItemClicked(photo: PixabayPhoto)
    }

    companion object {

        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PixabayPhoto>() {

            override fun areItemsTheSame(oldItem: PixabayPhoto, newItem: PixabayPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PixabayPhoto,
                newItem: PixabayPhoto
            ) = oldItem == newItem
        }

    }

}