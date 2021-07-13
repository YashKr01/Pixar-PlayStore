package com.example.pixar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.pixar.databinding.ItemViewPagerBinding
import com.example.pixar.model.ViewPagerModel

class ViewPagerAdapter(
    private val list: ArrayList<ViewPagerModel>,
    private val viewPager: ViewPager2
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
    }

    override fun getItemCount(): Int = list.size

    class ViewPagerViewHolder(private val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(item: ViewPagerModel) = binding.viewpagerImage.setImageResource(item.drawable)

    }

}