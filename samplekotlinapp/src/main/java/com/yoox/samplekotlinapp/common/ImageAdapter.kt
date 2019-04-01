package com.yoox.samplekotlinapp.common

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class ImageAdapter(
    private val urls: List<String>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val frameLayout = FrameLayout(context)
        val progressBar = ProgressBar(context)
        val loaderSize = 64f.toPX(context)
        progressBar.layoutParams = FrameLayout.LayoutParams(loaderSize, loaderSize, Gravity.CENTER)
        val imageView = AppCompatImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        frameLayout.addView(progressBar)
        frameLayout.addView(imageView)

        container.addView(frameLayout)
        Glide.with(context).load(urls[position]).into(imageView)
        return frameLayout
    }

    override fun getCount(): Int = urls.size

    override fun isViewFromObject(view: View, any: Any): Boolean = view === any

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) = container.removeView(view as View)

    private infix fun Float.toPX(context: Context): Int {
        val floatValue = this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(floatValue)
    }
}
