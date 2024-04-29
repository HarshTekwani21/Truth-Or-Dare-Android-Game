package com.example.truth_dare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BottlePagerAdapter extends PagerAdapter {

    private Context context;
    private int[] bottleImages;

    public BottlePagerAdapter(Context context, int[] bottleImages) {
        this.context = context;
        this.bottleImages = bottleImages;
    }

    @Override
    public int getCount() {
        return bottleImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_bottle, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageViewBottle);
        imageView.setImageResource(bottleImages[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
