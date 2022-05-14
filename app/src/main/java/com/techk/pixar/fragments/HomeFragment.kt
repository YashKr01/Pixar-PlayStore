package com.techk.pixar.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.techk.pixar.R
import com.techk.pixar.adapters.CategoryAdapter
import com.techk.pixar.adapters.ViewPagerAdapter
import com.techk.pixar.databinding.FragmentHomeBinding
import com.techk.pixar.model.Category
import com.techk.pixar.model.ViewPagerModel
import com.techk.pixar.utils.Constants.Companion.initList
import com.techk.pixar.utils.Constants.Companion.initViewPagerList
import com.techk.pixar.utils.Constants.Companion.isOnline
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.techk.pixar.utils.Constants
import kotlin.math.abs

class HomeFragment : Fragment(), CategoryAdapter.CategoryClickListener,
    ViewPagerAdapter.ViewPagerClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerList = ArrayList<ViewPagerModel>()
        initViewPagerList(viewPagerList, requireContext())

        val adRequest = AdRequest.Builder().build()
        binding.bannerAd.loadAd(adRequest)

        binding.bannerAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                if (!isOnline(requireContext())) binding.bannerAd.isVisible = false
                else binding.bannerAd.loadAd(adRequest)
            }
        }

        InterstitialAd.load(
            requireContext(),
            getString(R.string.interstitial_ad_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    if (mInterstitialAd != null) mInterstitialAd!!.show(requireActivity())
                }
            })

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(viewPagerList, this, this@HomeFragment)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(transformer)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        val list = ArrayList<Category>()
        initList(list, requireContext())
        binding.recyclerView.apply {
            adapter = CategoryAdapter(list, requireContext(), this@HomeFragment)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    override fun onCategoryCLicked(item: Category) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(item.title)
        findNavController().navigate(action)
    }

    override fun onViewPagerClick(item: ViewPagerModel) {
        when (item.title) {
            getString(R.string.vp_search) -> {
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(null)
                findNavController().navigate(action)
            }
            getString(R.string.vp_edit) -> {
                val action = HomeFragmentDirections.actionHomeFragmentToImageBottomSheetFragment()
                findNavController().navigate(action)
            }
            getString(R.string.vp_rate) -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_URL)))
            }
            else -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, Constants.PLAY_STORE_URL)
                startActivity(Intent.createChooser(intent, "choose one"))
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}