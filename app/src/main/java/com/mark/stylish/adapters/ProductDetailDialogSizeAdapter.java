package com.mark.stylish.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.stylish.R;
import com.mark.stylish.fragments.ProductDetailFragment;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.util.ArrayList;

public class ProductDetailDialogSizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Product mProduct;
    private Fragment mFragment;
    private ArrayList<Variants> mVariantsArrayList;
    private int mSelectPosition = -1;

    public ProductDetailDialogSizeAdapter(Context context, Fragment fragment, Product product,
                                          ArrayList<Variants> variantsArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.mFragment = fragment;
        this.mProduct = product;
        this.mVariantsArrayList = variantsArrayList;
    }

    public class DialogSizeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textviewdialogsize;
        public ImageView imageviewdialogsizeselect;
        public ImageView imageviewdialogsizestrikethrough;

        public DialogSizeViewHolder(View view) {
            super(view);
            textviewdialogsize = (TextView)view.findViewById(R.id.tv_dialog_product_size);
            imageviewdialogsizeselect = (ImageView)view.findViewById(R.id.iv_dialog_product_size_select);
            imageviewdialogsizestrikethrough = (ImageView)view.findViewById(R.id.iv_dialog_product_size_strike_through);

            textviewdialogsize.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((ProductDetailFragment)mFragment).showStockNumber(mVariantsArrayList, getAdapterPosition());

            mSelectPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder dialogSizeViewHolder = null;
        View view = mInflater.inflate(R.layout.item_product_detail_dialog_size, null);
        dialogSizeViewHolder = new DialogSizeViewHolder(view);
        return dialogSizeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Variants variants = mVariantsArrayList.get(position);
        String sizeString = variants.getSize();
        String textColor = "#663f3a3a";

        ((DialogSizeViewHolder) holder).textviewdialogsize.setText(sizeString);

        if (mSelectPosition == position) {
            ((DialogSizeViewHolder) holder).imageviewdialogsizeselect.setVisibility(View.VISIBLE);
        } else {
            ((DialogSizeViewHolder) holder).textviewdialogsize.setVisibility(View.VISIBLE);
            ((DialogSizeViewHolder) holder).imageviewdialogsizeselect.setVisibility(View.INVISIBLE);
        }

        if (variants.getStock() == 0) {
            ((DialogSizeViewHolder)holder).textviewdialogsize.setTextColor(Color.parseColor(textColor));
            ((DialogSizeViewHolder)holder).imageviewdialogsizestrikethrough.setVisibility(View.VISIBLE);
            ((DialogSizeViewHolder) holder).imageviewdialogsizeselect.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mVariantsArrayList.size();
    }
}
