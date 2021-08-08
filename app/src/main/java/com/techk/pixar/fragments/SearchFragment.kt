package com.techk.pixar.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.techk.pixar.adapters.UnsplashPhotoAdapter
import com.techk.pixar.databinding.FragmentSearchBinding
import com.techk.pixar.model.UnsplashPhoto
import com.techk.pixar.paging.UnsplashLoadStateAdapter
import com.techk.pixar.utils.Constants
import com.techk.pixar.viewmodel.ImagesViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), UnsplashPhotoAdapter.OnItemClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagesViewModel>()

    private val searchQuery by navArgs<SearchFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val adRequest = AdRequest.Builder().build()
        binding.bannerAd.loadAd(adRequest)

        binding.bannerAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                if (!Constants.isOnline(requireContext())) binding.bannerAd.isVisible = false
                else binding.bannerAd.loadAd(adRequest)
            }
        }

        // paging and footer adapter
        val adapter = UnsplashPhotoAdapter(this)
        val footerAdapter = UnsplashLoadStateAdapter { adapter.retry() }

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) 2
                else 1
            }
        }

        binding.apply {
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateFooter(footer = footerAdapter)
            buttonError.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        binding.toolbar.title = searchQuery.query
        viewModel.searchPhotos(searchQuery.query)

        adapter.addLoadStateListener { loadState ->
            binding.apply {

                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                imageError.isVisible = loadState.source.refresh is LoadState.Error

                // if view is empty
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    textViewEmpty.isVisible = true
                    recyclerView.isVisible = false
                } else {
                    textViewEmpty.isVisible = false
                }

            }
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(photo: UnsplashPhoto) {
        val action = SearchFragmentDirections.actionSearchFragmentToWallpaperFragment(photo)
        findNavController().navigate(action)
    }

}