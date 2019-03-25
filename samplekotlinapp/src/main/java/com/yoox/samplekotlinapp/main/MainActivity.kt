package com.yoox.samplekotlinapp.main

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.samplekotlinapp.R
import com.yoox.samplekotlinapp.common.Glide4Engine
import com.yoox.samplekotlinapp.common.SearchResultItemAdapter
import com.yoox.samplekotlinapp.common.askPermission
import com.yoox.samplekotlinapp.detail.DetailActivity
import com.yoox.samplekotlinapp.detail.DetailActivity.Companion.ID
import com.yoox.samplekotlinapp.visual.VisualActivity
import com.yoox.samplekotlinapp.visual.VisualActivity.Companion.LOCAL_IMAGE_URI
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val adapter = SearchResultItemAdapter(this::onItemSelected)
        recyclerView.adapter = adapter

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.state
            .observe(this, Observer { newData ->

                return@Observer when (newData) {
                    MainState.Loading -> {
                        renderLoading()
                    }
                    is MainState.Result -> {
                        renderResult(newData, adapter, mainViewModel)
                    }
                }
            })

        mainViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
        mainViewModel.loadItems()
    }

    private fun renderLoading() {
        loader.visibility = View.VISIBLE
    }

    private fun renderResult(
        newData: MainState.Result,
        adapter: SearchResultItemAdapter,
        mainViewModel: MainViewModel
    ) {
        loader.visibility = View.INVISIBLE

        val request = newData.request

        previous_page.isEnabled = newData.isPreviousEnabled
        next_page.isEnabled = newData.isNextEnabled
        filter_color.isEnabled = newData.isFilterColorEnabled
        filter_designer.isEnabled = newData.isFilterDesignerEnabled
        filter_category.isEnabled = newData.isFilterCategoryEnabled
        filter_prices.isEnabled = newData.isFilterPriceEnabled
        adapter.replaceItems(newData.items)

        previous_page.setOnClickListener { mainViewModel.previousPage(request, newData) }
        next_page.setOnClickListener { mainViewModel.nextPage(request, newData) }
        filter_color.setOnClickListener { mainViewModel.colorFilter(request, newData) }
        filter_designer.setOnClickListener { mainViewModel.designerFilter(request, newData) }
        filter_category.setOnClickListener { mainViewModel.categoryFilter(request, newData) }
        filter_prices.setOnClickListener { mainViewModel.priceFilter(request, newData) }
        photo.setOnClickListener { askPermissionsAndRequestPhoto() }
        reset.setOnClickListener { mainViewModel.loadItems() }
    }


    private fun onItemSelected(item: SearchResultItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(ID, item.id)
        startActivity(intent)
    }

    private fun askPermissionsAndRequestPhoto() {
        if (askPermission(
                READ_EXTERNAL_STORAGE,
                READ_REQUEST
            ) && askPermission(
                WRITE_EXTERNAL_STORAGE,
                WRITE_REQUEST
            )
        ) {
            requestPhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_REQUEST -> if (grantResults[0] == PERMISSION_GRANTED) {
                askPermissionsAndRequestPhoto()
            }

            READ_REQUEST -> if (grantResults[0] == PERMISSION_GRANTED) {
                askPermissionsAndRequestPhoto()
            }
        }
    }

    private fun requestPhoto() {
        Matisse.from(this@MainActivity)
            .choose(MimeType.ofAll())
            .maxSelectable(1)
            .thumbnailScale(0.85f)
            .imageEngine(Glide4Engine())
            .capture(true)
            .captureStrategy(CaptureStrategy(true, FILE_PROVIDER))
            .forResult(PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainResult(data)
            val intent = Intent(this, VisualActivity::class.java)
            intent.putExtra(LOCAL_IMAGE_URI, uris[0])
            startActivity(intent)
        }
    }

    companion object {

        private const val READ_REQUEST = 3
        private const val WRITE_REQUEST = 2
        private const val PHOTO_REQUEST = 4
        private const val FILE_PROVIDER = "com.yoox.samplekotlinapp.fileprovider"
    }
}
