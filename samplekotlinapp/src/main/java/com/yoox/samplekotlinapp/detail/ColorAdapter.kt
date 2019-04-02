package com.yoox.samplekotlinapp.detail

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yoox.net.models.outbound.Color
import com.yoox.samplekotlinapp.R

class ColorAdapter internal constructor(
    private val data: List<Color>,
    private val onItemSelectedListener: (Color) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_color, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position], onItemSelectedListener)

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val name: TextView = itemView.findViewById(R.id.name)

        fun bind(item: Color, onItemSelectedListener: (Color) -> Unit) {
            val color = android.graphics.Color.parseColor("#" + item.rgb)
            image.setImageDrawable(ColorDrawable(color))
            name.text = item.name
            itemView.setOnClickListener { onItemSelectedListener(item) }
        }
    }
}
