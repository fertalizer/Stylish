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

public class CatalogMenApparelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<Object> mMenObjectArrayList = new ArrayList<>();
    private Context mContext;

    public CatalogMenApparelAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public class MenApparelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageviewproudctpicture;
        public TextView textviewproducttitle;
        public TextView textviewproductprice;

        public MenApparelViewHolder(View view) {
            super(view);
            this.imageviewproudctpicture = view.findViewById(R.id.iv_catalog_men_apparel_picture);
            this.textviewproducttitle = view.findViewById(R.id.tv_catalog_men_apparel_title);
            this.textviewproductprice = view.findViewById(R.id.tv_catalog_men_apparel_price);

            imageviewproudctpicture.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("MenRecyclerViewButton","fertalizer_men_viewHolder");

            Product product = (Product) mMenObjectArrayList.get(getAdapterPosition());
            ((MainActivity) mContext).loadProductDetail(product);
        }
    }

    public void updateMenData(ArrayList<Object> objectArrayList) {
        this.mMenObjectArrayList = objectArrayList;
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
        View view = mInflater.inflate(R.layout.item_catalog_men_apparel, viewGroup, false);
        menApparelViewHolder = new MenApparelViewHolder(view);
        return menApparelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = mMenObjectArrayList.get(position);
        String titleString = ((Product)object).getTitle();
        String priceString = "NT$ " + ((Product)object).getPrice();

        new DownloadMenImageTask(((MenApparelViewHolder)holder).imageviewproudctpicture,
                ((Product)object).getMainImage())
                .executeOnExecutor(Executors.newCachedThreadPool());
        ((MenApparelViewHolder)holder).textviewproducttitle.setText(titleString);
        ((MenApparelViewHolder)holder).textviewproductprice.setText(priceString);
    }


    @Override
    public int getItemCount() {
//        return 6;
        return  mMenObjectArrayList.size();
    }

    public class DownloadMenImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadMenImageTask(ImageView imageView, String url) {
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
