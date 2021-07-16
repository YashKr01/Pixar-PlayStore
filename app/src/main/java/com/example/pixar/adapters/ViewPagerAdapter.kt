package com.example.pixar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.pixar.databinding.ItemViewPagerBinding
import com.example.pixar.model.ViewPagerModel

class ViewPagerAdapter(
    private val list: ArrayList<ViewPagerModel>,
    private val viewPager: ViewPager2,
    private val listener: ViewPagerClickListener
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
        ViewPagerViewHolder(
            ItemViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setImage(list[position])

        val item = list[position]
        holder.binding.root.setOnClickListener {
            listener.onViewPagerClick(item)
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewPagerViewHolder(val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(item: ViewPagerModel) {
            Glide.with(itemView)
                .load(item.drawable)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.viewpagerImage)

            binding.viewPagerTitle.text = item.title
        }

    }

    interface ViewPagerClickListener {
        fun onViewPagerClick(item: ViewPagerModel)
    }

}