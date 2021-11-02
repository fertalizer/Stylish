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
import android.widget.TextView;

import com.mark.stylish.MainActivity;
import com.mark.stylish.R;
import com.mark.stylish.objects.Product;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class CatalogWomenApparelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Object> mWomenObjectArrayList = new ArrayList<>();
    private Context mContext;

    public CatalogWomenApparelAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    public class WomenApparelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageviewproudctpicture;
        public TextView textviewproducttitle;
        public TextView textviewproductprice;

        public WomenApparelViewHolder(View view) {
            super(view);
            this.imageviewproudctpicture = view.findViewById(R.id.iv_catalog_women_apparel_picture);
            this.textviewproducttitle = view.findViewById(R.id.tv_catalog_women_apparel_title);
            this.textviewproductprice = view.findViewById(R.id.tv_catalog_women_apparel_price);

            imageviewproudctpicture.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("WomenRecyclerViewButton","fertalizer_women_viewHolder");

            Product product = (Product) mWomenObjectArrayList.get(getAdapterPosition());
            ((MainActivity) mContext).loadProductDetail(product);
        }
    }

    public void updateWomenData(ArrayList<Object> objectArrayList) {
        this.mWomenObjectArrayList = objectArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder womenApparelViewHolder = null;
        View view = mInflater.inflate(R.layout.item_catalog_women_apparel, viewGroup, false);
        womenApparelViewHolder = new WomenApparelViewHolder(view);
        return womenApparelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = mWomenObjectArrayList.get(position);
        String titleString = ((Product)object).getTitle();
        String priceString = "NT$ " + ((Product)object).getPrice();

        new DownloadWomenImageTask(((WomenApparelViewHolder)holder).imageviewproudctpicture,
                ((Product)object).getMainImage())
                .executeOnExecutor(Executors.newCachedThreadPool());
        ((WomenApparelViewHolder)holder).textviewproducttitle.setText(titleString);
        ((WomenApparelViewHolder)holder).textviewproductprice.setText(priceString);
    }


    @Override
    public int getItemCount() {
//        return 7;
        return mWomenObjectArrayList.size();
    }

    public class DownloadWomenImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadWomenImageTask(ImageView imageView, String url) {
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
