package com.techk.pixar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.techk.pixar.databinding.ItemCategoryBinding
import com.techk.pixar.model.Category

class CategoryAdapter(
    private val list: List<Category>,
    private val context: Context,
    private val listener: CategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding
                .inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = list[position]

        Glide.with(context)
            .load(item.image)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.item.itemCategoryImage)

        holder.item.textCategoryName.text = item.title

        holder.item.root.setOnClickListener {
            listener.onCategoryCLicked(item)
        }

    }

    override fun getItemCount(): Int = list.size

    inner class CategoryViewHolder(val item: ItemCategoryBinding) :
        RecyclerView.ViewHolder(item.root)

    interface CategoryClickListener {
        fun onCategoryCLicked(item: Category)
    }

}