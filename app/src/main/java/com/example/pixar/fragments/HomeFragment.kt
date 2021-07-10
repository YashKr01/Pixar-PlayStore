package com.example.pixar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixar.R
import com.example.pixar.adapters.CategoryAdapter
import com.example.pixar.databinding.FragmentHomeBinding
import com.example.pixar.model.Category

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

    private fun initList() {
        list.clear()
        list.add(Category("Nature", R.drawable.ic_nature))
        list.add(Category("Wildlife", R.drawable.ic_wildlife))
        list.add(Category("Sports", R.drawable.ic_sports))
        list.add(Category("Food", R.drawable.ic_food))
        list.add(Category("Cities", R.drawable.ic_cities))
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryCLicked(item: Category) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(item.title)
        findNavController().navigate(action)
    }

}