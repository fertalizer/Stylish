package com.mark.stylish.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mark.stylish.R;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ProductDetailGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Product mProduct;

    public  ProductDetailGalleryAdapter(Context context, Product product) {
        this.mInflater = LayoutInflater.from(context);
        this.mProduct = product;
    }

    public class ProductDetailGalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView productdetailgalleryimage;

        public ProductDetailGalleryViewHolder(View view) {
            super(view);
            productdetailgalleryimage = (ImageView) view.findViewById(R.id.iv_product_detail_gallery_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder productDetailGalleryViewHolder = null;
        View view = mInflater.inflate(R.layout.item_product_detail_gallery, null);
        productDetailGalleryViewHolder = new ProductDetailGalleryViewHolder(view);

        return productDetailGalleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        position = position % mProduct.getImages().size();
        String urlImage = mProduct.getImages().get(position);
        Log.d("Gallery", "urlImage = " + urlImage);
        Log.d("Gallery", "RecyclerView size = " + mProduct.getImages().size());
        new DownloadImageTask(((ProductDetailGalleryViewHolder)holder).productdetailgalleryimage,
                urlImage).executeOnExecutor(Executors.newCachedThreadPool());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
//        return mProduct.getImages().size();
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadImageTask(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        //Due to different interface, use InputStream instead of OkHttp
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}