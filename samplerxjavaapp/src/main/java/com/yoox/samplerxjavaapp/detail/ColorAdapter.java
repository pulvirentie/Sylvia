package com.yoox.samplerxjavaapp.detail;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yoox.net.models.outbound.Color;
import com.yoox.samplerxjavaapp.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private final List<Color> data;
    private final OnColorSelectedListener onItemSelectedListener;

    ColorAdapter(List<Color> data, OnColorSelectedListener onItemSelectedListener) {
        this.data = data;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_color, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position), onItemSelectedListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).hashCode();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView name;

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }

        void bind(Color item, OnColorSelectedListener onItemSelectedListener) {
            int color = android.graphics.Color.parseColor("#" + item.getRgb());
            image.setImageDrawable(new ColorDrawable(color));
            name.setText(item.getName());
            itemView.setOnClickListener(v -> onItemSelectedListener.onColorSelected(item));
        }
    }

    public interface OnColorSelectedListener {
        void onColorSelected(Color item);
    }

}
