package com.zekierciyas.fancyfilterapp.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop

@BindingAdapter("imageAsBitmap")
fun loadImage(view: ImageView, bitmap: Uri) {
    Glide.with(view.context)
        .load(bitmap)
        //.error(R.drawable.ic_thumb_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(view)
}