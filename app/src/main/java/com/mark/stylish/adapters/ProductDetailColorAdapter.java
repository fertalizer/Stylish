package com.mark.stylish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mark.stylish.R;
import com.mark.stylish.objects.Color;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ProductDetailColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Color> mColorArrayList;

    public  ProductDetailColorAdapter(Context context, ArrayList<Color> colorArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.mColorArrayList = colorArrayList;
    }


    public class ProductDetailColorViewHolder extends RecyclerView.ViewHolder {
        public ImageView productdetailcolor;

        public ProductDetailColorViewHolder(View view) {
            super(view);
            productdetailcolor = view.findViewById(R.id.iv_product_detail_color);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder productDetailColorViewHolder = null;
        View view = mInflater.inflate(R.layout.item_product_detail_color, null);
        productDetailColorViewHolder = new ProductDetailColorViewHolder(view);

        return productDetailColorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Color color = mColorArrayList.get(position);
        String colorString = "#" + color.getCode();

        ((ProductDetailColorViewHolder)holder).productdetailcolor.setBackgroundColor(android.graphics.Color.parseColor(colorString));
    }

    @Override
    public int getItemCount() {
        return mColorArrayList.size();
    }
}
