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

public class CatalogAccessoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Object> mAccessoryObjectArrayList = new ArrayList<>();
    private Context mContext;

    public CatalogAccessoryAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public class AccessoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageviewproudctpicture;
        public TextView textviewproducttitle;
        public TextView textviewproductprice;

        public AccessoryViewHolder(View view) {
            super(view);
            this.imageviewproudctpicture = view.findViewById(R.id.tv_catalog_accessory_picture);
            this.textviewproducttitle = view.findViewById(R.id.tv_catalog_accessory_title);
            this.textviewproductprice = view.findViewById(R.id.iv_catalog_accessory_price);

            imageviewproudctpicture.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("AccRecyclerViewButton","fertalizer_accessory_viewHolder0");

            Product product = (Product) mAccessoryObjectArrayList.get(getAdapterPosition());
            ((MainActivity) mContext).loadProductDetail(product);
        }
    }

    public void updateAccessoryData(ArrayList<Object> objectArrayList) {
        this.mAccessoryObjectArrayList = objectArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder menApparelViewHolder = null;
        View view = mInflater.inflate(R.layout.item_catalog_accessory, viewGroup, false);
        menApparelViewHolder = new AccessoryViewHolder(view);
        return menApparelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = mAccessoryObjectArrayList.get(position);
        String titleString = ((Product)object).getTitle();
        String priceString = "NT$ " + ((Product)object).getPrice();

        new DownloadAccessoryImageTask(((AccessoryViewHolder)holder).imageviewproudctpicture,
                ((Product)object).getMainImage())
                .executeOnExecutor(Executors.newCachedThreadPool());
        ((AccessoryViewHolder)holder).textviewproducttitle.setText(titleString);
        ((AccessoryViewHolder)holder).textviewproductprice.setText(priceString);
    }


    @Override
    public int getItemCount() {
//        return 5;
        return mAccessoryObjectArrayList.size();
    }

    public class DownloadAccessoryImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadAccessoryImageTask(ImageView imageView, String url) {
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
