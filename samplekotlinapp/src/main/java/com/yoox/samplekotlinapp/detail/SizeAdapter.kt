package com.yoox.samplekotlinapp.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yoox.net.models.outbound.Size
import com.yoox.samplekotlinapp.R

class SizeAdapter internal constructor(
    private val data: List<Size>,
    private val onSizeSelectedListener: (Size) -> Unit
) : RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_size, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position], onSizeSelectedListener)

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)

        fun bind(item: Size, onItemSelectedListener: (Size) -> Unit) {
            name.text = item.name
            itemView.setOnClickListener { onItemSelectedListener(item) }
        }
    }

}
