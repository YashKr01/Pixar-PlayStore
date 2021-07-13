package com.example.pixar.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.pixar.R
import com.example.pixar.adapters.CategoryAdapter
import com.example.pixar.adapters.ViewPagerAdapter
import com.example.pixar.databinding.FragmentHomeBinding
import com.example.pixar.model.Category
import com.example.pixar.model.ViewPagerModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.abs

class HomeFragment : Fragment(), CategoryAdapter.CategoryClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val list = ArrayList<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter(list, requireContext(), this)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }

        val viewPagerList = ArrayList<ViewPagerModel>()
        viewPagerList.add(ViewPagerModel(R.drawable.ic_cities, ""))
        viewPagerList.add(ViewPagerModel(R.drawable.ic_food, ""))
        viewPagerList.add(ViewPagerModel(R.drawable.ic_wildlife, ""))
        viewPagerList.add(ViewPagerModel(R.drawable.ic_sports, ""))
        viewPagerList.add(ViewPagerModel(R.drawable.ic_launcher_background, ""))

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(viewPagerList, this)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        viewPager.setPageTransformer(transformer)

    }

    private fun initList() {
        list.clear()
        list.add(Category("Nature", R.drawable.ic_nature))
        list.add(Category("Wildlife", R.drawable.ic_wildlife))
        list.add(Category("Sports", R.drawable.ic_sports))
        list.add(Category("Food", R.drawable.ic_food))
        list.add(Category("Cities", R.drawable.ic_cities))
    }

    override fun onCategoryCLicked(item: Category) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(item.title)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}