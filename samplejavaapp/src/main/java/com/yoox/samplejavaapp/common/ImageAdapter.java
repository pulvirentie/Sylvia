package com.yoox.samplejavaapp.common;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    private final List<String> urls;

    public ImageAdapter(List<String> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        FrameLayout frameLayout = new FrameLayout(context);
        ProgressBar progressBar = new ProgressBar(context);
        int loaderSize = convertDpToPixel(64f, context);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(loaderSize, loaderSize, Gravity.CENTER));
        AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        frameLayout.addView(progressBar);
        frameLayout.addView(imageView);

        container.addView(frameLayout);
        Glide.with(context).load(urls.get(position)).into(imageView);
        return frameLayout;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    private static int convertDpToPixel(float dp, Context context) {
        float floatValue = dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(floatValue);
    }
}
