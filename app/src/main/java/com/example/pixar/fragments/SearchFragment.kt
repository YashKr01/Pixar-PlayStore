package com.example.pixar.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixar.R
import com.example.pixar.adapters.UnsplashPhotoAdapter
import com.example.pixar.databinding.FragmentSearchBinding
import com.example.pixar.paging.UnsplashLoadStateAdapter
import com.example.pixar.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // paging and footer adapter
        val adapter = UnsplashPhotoAdapter()
        val footerAdapter = UnsplashLoadStateAdapter { adapter.retry() }

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount - 1 && footerAdapter.itemCount > 0) 2
                else 1
            }
        }

        binding.apply {
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.adapter = adapter.withLoadStateFooter(footer = footerAdapter)
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.recyclerView.scrollToPosition(0)

                if (query != null) viewModel.searchPhotos(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}