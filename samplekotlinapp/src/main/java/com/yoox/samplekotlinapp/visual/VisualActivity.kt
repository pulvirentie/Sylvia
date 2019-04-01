package com.yoox.samplekotlinapp.visual


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.samplekotlinapp.R
import com.yoox.samplekotlinapp.common.SearchResultItemAdapter
import com.yoox.samplekotlinapp.detail.DetailActivity
import com.yoox.samplekotlinapp.detail.DetailActivity.Companion.ID

class VisualActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual)

        val loader = findViewById<View>(R.id.loader)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = SearchResultItemAdapter(this::onItemSelected)
        recyclerView.adapter = adapter

        val visualViewModel = ViewModelProviders.of(this).get(VisualViewModel::class.java)
        visualViewModel.state
            .observe(this, Observer { newData ->

                return@Observer when (newData) {
                    VisualState.Loading -> {
                        renderLoading(loader)
                    }
                    is VisualState.Result -> {
                        renderResult(loader, adapter, newData)
                    }
                }

            })

        visualViewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })


        val remoteImageUri = intent.getStringExtra(REMOTE_IMAGE_URI)
        val localImageUri = intent.getParcelableExtra<Uri>(LOCAL_IMAGE_URI)
        visualViewModel.loadItems(remoteImageUri, localImageUri)

    }

    private fun renderResult(
        loader: View,
        adapter: SearchResultItemAdapter,
        newData: VisualState.Result
    ) {
        loader.visibility = View.INVISIBLE
        adapter.replaceItems(newData.items)
    }

    private fun renderLoading(loader: View) {
        loader.visibility = View.VISIBLE
    }

    private fun onItemSelected(item: SearchResultItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(ID, item.id)
        startActivity(intent)
    }

    companion object {

        const val LOCAL_IMAGE_URI = "LOCAL_IMAGE_URI"
        const val REMOTE_IMAGE_URI = "REMOTE_IMAGE_URI"
    }

}
