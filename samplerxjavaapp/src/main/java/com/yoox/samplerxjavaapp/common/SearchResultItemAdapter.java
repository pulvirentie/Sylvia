package com.yoox.samplerxjavaapp.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yoox.net.models.outbound.SearchResultItem;
import com.yoox.samplerxjavaapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultItemAdapter extends RecyclerView.Adapter<SearchResultItemAdapter.ViewHolder> {

    private final List<SearchResultItem> data;
    private final OnItemSelectedListener onItemSelectedListener;

    public SearchResultItemAdapter(OnItemSelectedListener onItemSelectedListener) {
        this.data = new ArrayList<>(0);
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void replaceItems(List<SearchResultItem> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_search, parent, false);

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
        private final TextView category;
        private final TextView brand;
        private final TextView price;

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.category);
            brand = itemView.findViewById(R.id.brand);
            price = itemView.findViewById(R.id.price);
        }

        void bind(SearchResultItem item, OnItemSelectedListener onItemSelectedListener) {

            Glide.with(image).load(item.getPreviewImage()).into(image);

            category.setText(item.getCategory().getName().getName());
            brand.setText(item.getBrand().getName());
            price.setText(item.getDiscountedPrice().getRawPrice());

            itemView.setOnClickListener(v -> onItemSelectedListener.onItemSelected(item));
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SearchResultItem item);
    }

}
