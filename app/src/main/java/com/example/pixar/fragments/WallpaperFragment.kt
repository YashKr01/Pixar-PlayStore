package com.example.pixar.fragments

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
import com.example.pixar.model.UnsplashPhoto
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.UNSPLASH_URL
import com.example.pixar.utils.Constants.Companion.imageToBitmap
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_wallpaper.*
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

            // if device version is enough
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val action =
                    WallpaperFragmentDirections.actionWallpaperFragmentToWallpaperBottomSheetFragment(
                        photo
                    )
                findNavController().navigate(action)
            } else {
                // else set wallpaper on home screen
                lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap = imageToBitmap(photo.urls.regular)

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

    private fun snackBar(message: String) {
        Constants.showSnackBar(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)
    }

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

    private fun openButtonCLicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            fab_download.isClickable = true
            fab_save.isClickable = true
            fab_wallpaper.isClickable = true
            fab_save.startAnimation(fromBottom)
            fab_wallpaper.startAnimation(fromBottom)
            fab_download.startAnimation(fromBottom)
            fab_open.startAnimation(rotateOpen)
        } else {
            fab_download.isClickable = false
            fab_save.isClickable = false
            fab_wallpaper.isClickable = false
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