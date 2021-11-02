package com.mark.stylish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mark.stylish.R;
import com.mark.stylish.fragments.ProductDetailFragment;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.util.ArrayList;

public class ProductDetailDialogColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Product mProduct;
//    private Context mContext;
    private Fragment mFragment;
    private ArrayList<Variants> mVariantsArrayList;
    private int mSelectPosition = -1;

    public ProductDetailDialogColorAdapter(Context context,Fragment fragment, Product product) {
        this.mInflater = LayoutInflater.from(context);
//        this.mContext = context;
        this.mFragment = fragment;
        this.mProduct = product;
    }

    public class DialogColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageviewdialogcolor;
        public ImageView imageviewdialogcolorselect;

        public DialogColorViewHolder(View view) {
            super(view);
            imageviewdialogcolor = (ImageView)view.findViewById(R.id.iv_dialog_product_color);
            imageviewdialogcolorselect = (ImageView)view.findViewById(R.id.iv_dialog_product_color_select);

            imageviewdialogcolor.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.i("ColorBar", "Success");
            Color color = mProduct.getColors().get(getAdapterPosition());
            mVariantsArrayList = ((ProductDetailFragment)mFragment).classifyVariants(mProduct, color);
            Log.i("ColorBar", "mVariantsArrayList = " + mVariantsArrayList);
            ((ProductDetailFragment)mFragment).showSizeBar(mProduct, mVariantsArrayList);

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
        RecyclerView.ViewHolder dialogColorViewHolder = null;
        View view = mInflater.inflate(R.layout.item_product_detail_dialog_color, null);
        dialogColorViewHolder = new DialogColorViewHolder(view);
        return dialogColorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Color color = mProduct.getColors().get(position);
        String colorString = "#" + color.getCode();

        ((DialogColorViewHolder) holder).imageviewdialogcolor
                .setBackgroundColor(android.graphics.Color.parseColor(colorString));

        if (mSelectPosition == position) {
            ((DialogColorViewHolder) holder).imageviewdialogcolorselect.setVisibility(View.VISIBLE);
        } else {
            ((DialogColorViewHolder) holder).imageviewdialogcolor.setVisibility(View.VISIBLE);
            ((DialogColorViewHolder) holder).imageviewdialogcolorselect.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return mProduct.getColors().size();
    }

//    public ArrayList<Variants> classifyVariants(Color color) {
//        ArrayList<Variants> variants = new ArrayList<>();
//        for (int i = 0; i < mProduct.getVariants().size(); i++) {
//            if (mProduct.getVariants().get(i).getColorCode().equals(color.getCode())) {
//                variants.add(mProduct.getVariants().get(i));
//            }
//        }
//        return variants;
//    }
}
