package com.yoox.samplekotlinapp.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.yoox.net.models.outbound.Color
import com.yoox.net.models.outbound.Image
import com.yoox.net.models.outbound.Item
import com.yoox.net.models.outbound.Size
import com.yoox.samplekotlinapp.R
import com.yoox.samplekotlinapp.common.ImageAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.item.observe(this, Observer {
            bindUI(it)
        })

        detailViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        val id = intent.getStringExtra(KEY_ITEM_ID)
        detailViewModel.loadItem(id)
    }

    private fun bindUI(item: Item) {
        val transition = TransitionSet().apply {
            addTransition(Slide())
            addTransition(Fade())
        }
        TransitionManager.beginDelayedTransition(root, transition)

        val hasValidColor = !item.colors.isEmpty()

        val (imagesUrl, sizes) = if (hasValidColor) {
            val color = item.colors[0]
            Pair(color.images.getThumbnails(), color.sizeList)
        } else {
            Pair(listOf(item.previewImage), emptyList())
        }

        pager.adapter = ImageAdapter(imagesUrl)

        brand.text = item.brand.name
        brand.visibility = View.VISIBLE
        category.text = item.category.name.name
        category.visibility = View.VISIBLE
        price.text = item.discountedPrice.rawPrice
        price.visibility = View.VISIBLE
        composition.text = item.composition
        composition.visibility = View.VISIBLE

        val colorAdapter = ColorAdapter(item.colors, this::onColorSelected)
        color_recyclerView.adapter = colorAdapter

        val sizeAdapter = SizeAdapter(sizes, this::onSizeSelected)
        size_recyclerView.adapter = sizeAdapter
    }

    private fun List<Image>.getThumbnails(): List<String> = map {
        it.thumbnailUrl
    }

    private fun onColorSelected(item: Color) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun onSizeSelected(item: Size) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val KEY_ITEM_ID = "ITEM_ID"

        fun start(context: Context, itemID: String) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra(KEY_ITEM_ID, itemID)
            context.startActivity(starter)
        }
    }
}
