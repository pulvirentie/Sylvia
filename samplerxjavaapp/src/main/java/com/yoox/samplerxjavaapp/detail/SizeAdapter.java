package com.yoox.samplerxjavaapp.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yoox.net.models.outbound.Size;
import com.yoox.samplerxjavaapp.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    private final List<Size> data;
    private final OnSizeSelectedListener onSizeSelectedListener;

    SizeAdapter(List<Size> data, OnSizeSelectedListener onSizeSelectedListener) {
        this.data = data;
        this.onSizeSelectedListener = onSizeSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_size, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position), onSizeSelectedListener);
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
        private final TextView name;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }

        void bind(Size item, OnSizeSelectedListener onItemSelectedListener) {
            name.setText(item.getName());
            itemView.setOnClickListener(v -> onItemSelectedListener.onSizeSelected(item));
        }
    }

    public interface OnSizeSelectedListener {
        void onSizeSelected(Size item);
    }

}
