package com.techk.pixar.fragments

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.techk.pixar.R
import com.techk.pixar.databinding.FragmentWallpaperBinding
import com.techk.pixar.model.UnsplashPhoto
import com.techk.pixar.utils.Constants.Companion.UNSPLASH_URL
import com.techk.pixar.utils.Constants.Companion.imageToBitmap
import com.techk.pixar.utils.Constants.Companion.isOnline
import com.techk.pixar.utils.Constants.Companion.showSnackBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class WallpaperFragment : Fragment() {

    // binding
    private var _binding: FragmentWallpaperBinding? = null
    private val binding get() = _binding!!

    // nav args
    private val args by navArgs<WallpaperFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = args.photo

        // load wallpaper
        loadImage(photo)

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

        // wallpaper button on click
        binding.fabWallpaper.setOnClickListener {

            // if device version is enough
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val action =
                    WallpaperFragmentDirections
                        .actionWallpaperFragmentToWallpaperBottomSheetFragment(photo)
                findNavController().navigate(action)
            } else {
                // else set wallpaper on home screen
                if (isOnline(requireContext())) {
                    lifecycleScope.launch(Dispatchers.IO) {

                        val bitmap: Bitmap? = imageToBitmap(photo.urls.regular)

                        if (bitmap != null) {
                            val e = setWallpaperOnHomeScreen(bitmap)
                            if (e != null) withContext(Dispatchers.Main) {
                                snackBar(e)
                            } else {
                                withContext(Dispatchers.Main) {
                                    snackBar(getString(R.string.wallpaper_has_been_set))
                                    withContext(Dispatchers.IO) { delay(1000L) }
                                }
                            }
                        }

                    }
                } else showSnackBar(
                    requireContext(),
                    binding.root,
                    getString(R.string.no_connection),
                    Snackbar.LENGTH_SHORT
                )

            }

        }

    }

    private fun setWallpaperOnHomeScreen(bitmap: Bitmap): String? {
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        return try {
            wallpaperManager.setBitmap(bitmap)
            null
        } catch (e: IOException) {
            e.message.toString()
        }
    }

    private fun snackBar(message: String) =
        showSnackBar(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)

    private fun loadImage(source: UnsplashPhoto) {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}