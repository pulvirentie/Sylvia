package com.yoox.samplekotlinapp.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoox.net.models.outbound.SearchResultItem
import com.yoox.samplekotlinapp.R

class SearchResultItemAdapter(
    private val onItemSelectedListener: (SearchResultItem) -> Unit
) :
    RecyclerView.Adapter<SearchResultItemAdapter.ViewHolder>() {

    private val data: MutableList<SearchResultItem> = mutableListOf()

    fun replaceItems(newData: List<SearchResultItem>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_search, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position], onItemSelectedListener)

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val brand: TextView = itemView.findViewById(R.id.brand)
        private val price: TextView = itemView.findViewById(R.id.price)

        fun bind(item: SearchResultItem, onItemSelectedListener: (SearchResultItem) -> Unit) {

            Glide.with(image).load(item.previewImage).into(image)

            category.text = item.category.name.name
            brand.text = item.brand.name
            price.text = item.discountedPrice.rawPrice

            itemView.setOnClickListener { onItemSelectedListener(item) }
        }
    }
}
