package com.mark.stylish.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.stylish.MainActivity;
import com.mark.stylish.R;
import com.mark.stylish.objects.Product;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

public class ProductDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Product mProduct;
    private Context mContext;

    public ProductDetailAdapter(Context context, Product product) {
        this.mInflater = LayoutInflater.from(context);
        this.mProduct = product;
        this.mContext = context;
    }


    public class ProductDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView productdetailtitle;
        public TextView productdetailprice;
        public TextView productdetailid;
        public TextView productdetailstory;
//        public ImageView productdetailcolorbar;
        public TextView productdetailsize;
        public TextView productdetailstock;
        public TextView productdetailmaterial;
        public TextView productdetailwash;
        public TextView productdetailplace;
        public TextView productdetailnote;
        public RecyclerView productdetailcolorbar;
        public ImageView productdetailbackbutton;
        public RecyclerView productdetailgallery;

        public ProductDetailViewHolder(View view) {
            super(view);
            productdetailtitle = (TextView) view.findViewById(R.id.tv_product_detail_title);
            productdetailprice = (TextView) view.findViewById(R.id.tv_product_detail_price);
            productdetailid = (TextView) view.findViewById(R.id.tv_product_detail_id);
            productdetailstory = (TextView) view.findViewById(R.id.tv_product_detail_story);
//            productdetailcolorbar = (ImageView) view.findViewById(R.id.iv_product_detail_color_image);
            productdetailsize = (TextView) view.findViewById(R.id.tv_product_detail_size_text);
            productdetailstock = (TextView) view.findViewById(R.id.tv_product_detail_stock_text);
            productdetailmaterial = (TextView) view.findViewById(R.id.tv_product_detail_material_text);
            productdetailwash = (TextView) view.findViewById(R.id.tv_product_detail_wash_text);
            productdetailplace = (TextView) view.findViewById(R.id.tv_product_detail_place_text);
            productdetailnote = (TextView) view.findViewById(R.id.tv_product_detail_note_text);
            productdetailcolorbar = (RecyclerView) view.findViewById(R.id.rv_product_detail_color);
            productdetailbackbutton = (ImageView) view.findViewById(R.id.iv_product_detail_back_button);
            productdetailgallery = (RecyclerView) view.findViewById(R.id.rv_product_detail_gallery);

            productdetailbackbutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((MainActivity) mContext).toolbarAndBottomNavigationAppear();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder productDetailViewHolder = null;
        View view = mInflater.inflate(R.layout.item_product_detail, null);
        productDetailViewHolder = new ProductDetailViewHolder(view);

        return productDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        String sizeString;
//        String colorString = "#" + mProduct.getColors().get(mProduct.getColors().size() - 1).getCode();

        if (mProduct.getSize().size() < 2) {
            sizeString = mProduct.getSize().get(0);
        } else {
            sizeString = mProduct.getSize().get(0) + " - " + mProduct.getSize().get(mProduct.getSize().size() - 1);
        }
        ((ProductDetailViewHolder)holder).productdetailsize.setText(sizeString);

        int stockNumberSum = 0;
        for (int i = 0; i < mProduct.getVariants().size(); i++) {
            int eachStockNumber = mProduct.getVariants().get(i).getStock();
            stockNumberSum = stockNumberSum + eachStockNumber;
        }
        String stockString = Integer.toString(stockNumberSum);
        ((ProductDetailViewHolder)holder).productdetailstock.setText(stockString);

        String titleString = mProduct.getTitle();
        ((ProductDetailViewHolder)holder).productdetailtitle.setText(titleString);
        String priceString = "NT$ " + mProduct.getPrice();
        ((ProductDetailViewHolder)holder).productdetailprice.setText(priceString);
        String idString = mProduct.getId();
        ((ProductDetailViewHolder)holder).productdetailid.setText(idString);
        String storyString = mProduct.getStory();
        ((ProductDetailViewHolder)holder).productdetailstory.setText(storyString);
//        ((ProductDetailViewHolder)holder).productdetailcolorbar.setBackgroundColor(Color.parseColor(colorString));
        String textureString = mProduct.getTexture();
        ((ProductDetailViewHolder)holder).productdetailmaterial.setText(textureString);
        String washString = mProduct.getWash();
        ((ProductDetailViewHolder)holder).productdetailwash.setText(washString);
        String placeString = mProduct.getPlace();
        ((ProductDetailViewHolder)holder).productdetailplace.setText(placeString);
        String noteString = mProduct.getNote();
        ((ProductDetailViewHolder)holder).productdetailnote.setText(noteString);

        LinearLayoutManager layoutManagerColorBar = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        ((ProductDetailViewHolder)holder).productdetailcolorbar.setLayoutManager(layoutManagerColorBar);
        ((ProductDetailViewHolder)holder).productdetailcolorbar.setHasFixedSize(true);

        ProductDetailColorAdapter productDetailColorAdapter = new ProductDetailColorAdapter(mContext,
                mProduct.getColors());
        ((ProductDetailViewHolder)holder).productdetailcolorbar.setAdapter(productDetailColorAdapter);

        final LinearLayoutManager layoutManagerGallery = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        ((ProductDetailViewHolder)holder).productdetailgallery.setLayoutManager(layoutManagerGallery);
        ((ProductDetailViewHolder)holder).productdetailgallery.setHasFixedSize(true);
        ((ProductDetailViewHolder)holder).productdetailgallery
                .scrollToPosition(mProduct.getImages().size() * 100);

        final ProductDetailGalleryAdapter productDetailGalleryAdapter = new ProductDetailGalleryAdapter(mContext,
                mProduct);
        ((ProductDetailViewHolder)holder).productdetailgallery.setAdapter(productDetailGalleryAdapter);

        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(((ProductDetailViewHolder)holder).productdetailgallery);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
////            int count = mProduct.getImages().size() * 100;
//            int count = 0;
//            @Override
//            public void run() {
//                for (;;) {
//                    ((ProductDetailViewHolder) holder).productdetailgallery
//                            .smoothScrollToPosition(count + 1);
//                    count++;
//                }
//            }
//        }, 1000);


//        ((ProductDetailViewHolder)holder).productdetailgallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    View centerView = snapHelper.findSnapView(layoutManagerGallery);
//                    int pos = layoutManagerGallery.getPosition(centerView);
//                    Log.d("Snapped Item Position:", "" + pos);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 1;
//        return mObjectArrayList.size();
    }
}
