package com.example.pixar.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pixar.databinding.FragmentWallpaperBinding
import com.example.pixar.utils.Constants.Companion.UNSPLASH_URL
import com.example.pixar.viewmodel.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class WallpaperFragment : Fragment() {

    private var _binding: FragmentWallpaperBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<WallpaperFragmentArgs>()

    private val viewModel by viewModels<WallpaperViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val photo = args.photo

            Glide.with(this@WallpaperFragment)
                .load(photo.urls.regular)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageError.isVisible = true
                        textError.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageError.isVisible = false
                        textError.isVisible = false
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageWallpaper)

            textLikes.text = photo.likes.toString().plus(" Likes")

            textUsername.paint.isUnderlineText = true
            textUsername.text = photo.user.username
            textUsername.setOnClickListener {
                val uri = Uri.parse(photo.user.attributionUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            textUnSplash.paint.isUnderlineText = true
            textUnSplash.setOnClickListener {
                val uri = Uri.parse(UNSPLASH_URL)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            // TODO : download/save to gallery
            binding.buttonDownload.setOnClickListener {
                viewModel.trackDownloads(photo.links.download_location)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}