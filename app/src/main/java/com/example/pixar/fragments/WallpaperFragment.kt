package com.example.pixar.fragments

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pixar.R
import com.example.pixar.databinding.FragmentWallpaperBinding
import com.example.pixar.model.PixabayPhoto
import com.example.pixar.model.UnsplashPhoto
import com.example.pixar.utils.Constants.Companion.IMAGE_DOWNLOAD_FOLDER_NAME
import com.example.pixar.utils.Constants.Companion.PIXABAY_URL
import com.example.pixar.utils.Constants.Companion.UNSPLASH_URL
import com.example.pixar.utils.Constants.Companion.imageToBitmap
import com.example.pixar.utils.Constants.Companion.isOnline
import com.example.pixar.utils.Constants.Companion.saveImage
import com.example.pixar.utils.Constants.Companion.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WallpaperFragment : Fragment() {

    // binding
    private var _binding: FragmentWallpaperBinding? = null
    private val binding get() = _binding!!

    // nav args
    private val args by navArgs<WallpaperFragmentArgs>()

    // animations
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_open.setOnClickListener {
            openButtonCLicked()
        }

        val photo = args.photo

        // load wallpaper
        loadLargeImage(photo)

        binding.apply {

            textUnsplash.paint.isUnderlineText = true

            textUnsplash.setOnClickListener {
                val uri = Uri.parse(UNSPLASH_URL)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            textUsername.text = photo.user.username
            textUsername.paint.isUnderlineText = true

            textUsername.setOnClickListener {
                val uri = Uri.parse(photo.user.attributionUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

        }

        // download button on click
        binding.fabDownload.setOnClickListener {

            val action =
                WallpaperFragmentDirections.actionWallpaperFragmentToDownloadBottomSheetFragment(
                    photo
                )

            findNavController().navigate(action)
        }

        // wallpaper button on click
        binding.fabWallpaper.setOnClickListener {

            val action =
                WallpaperFragmentDirections.actionWallpaperFragmentToWallpaperBottomSheetFragment(
                    photo
                )

            findNavController().navigate(action)
        }

    }

    private fun showNoConnectionSnackBar() {
        showSnackBar(
            requireContext(),
            binding.root,
            "No Connection...",
            Snackbar.LENGTH_SHORT
        )
    }


    private fun loadLargeImage(source: UnsplashPhoto) {

        Glide.with(requireContext())
            .load(source.urls.regular)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.isVisible = false
                    binding.textLoading.isVisible = false
                    binding.imageError.isVisible = true
                    binding.textError.isVisible = true
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.isVisible = false
                    binding.textLoading.isVisible = false
                    binding.imageError.isVisible = false
                    binding.textError.isVisible = false
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageWallpaper)

    }

    private fun openButtonCLicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            fab_save.startAnimation(fromBottom)
            fab_wallpaper.startAnimation(fromBottom)
            fab_download.startAnimation(fromBottom)
            fab_open.startAnimation(rotateOpen)
        } else {
            fab_save.startAnimation(toBottom)
            fab_wallpaper.startAnimation(toBottom)
            fab_download.startAnimation(toBottom)
            fab_open.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            fab_download.visibility = View.VISIBLE
            fab_save.visibility = View.VISIBLE
            fab_save.visibility = View.VISIBLE
        } else {
            fab_download.visibility = View.INVISIBLE
            fab_save.visibility = View.INVISIBLE
            fab_save.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}