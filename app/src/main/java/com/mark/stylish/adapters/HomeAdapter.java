package com.mark.stylish.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private ArrayList<Product> mProductListData;
//    private ArrayList<NewHots> mHotsListData;
    private ArrayList<Object> mObjectsList = new ArrayList<>();
    private LayoutInflater mInflater;
    private static final int ITEM_ZERO = 0;
    private static final int ITEM_ONE = 1;
    private static final int ITEM_TWO = 2;
    private Context mContext;
//    private ArrayList<Product> mProductArrayList = new ArrayList<>();


    public HomeAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
//        this.mProductListData = productListData;
//        this.mHotsListData = hotsListData;
    }

    public void updateHomeData(ArrayList<Object> listData) {
        this.mObjectsList = listData;
        notifyDataSetChanged();
    }

//    public void updateProduct(Product product) {
//        this.mProductArrayList.add(product);
//    }

    public class HomeViewHolder0 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textviewtitle1;
        public TextView textviewdetail1;
        public ImageView imageview1;
        public ConstraintLayout constraintlayouthomelist0;

        public HomeViewHolder0(View view) {
            super(view);
            textviewtitle1 = (TextView)view.findViewById(R.id.tv_home_list_title1);
            textviewdetail1 = (TextView)view.findViewById(R.id.tv_home_list_detail1);
            imageview1 = (ImageView)view.findViewById(R.id.iv_home_list_image1);
            constraintlayouthomelist0 = (ConstraintLayout)view.findViewById(R.id.layout_home_list0);

//            imageview1.setOnClickListener(this);
            constraintlayouthomelist0.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("HomeRecyclerViewButton","fertalizer_home_viewHolder0");

            Product product = (Product) mObjectsList.get(getAdapterPosition());
            ((MainActivity) mContext).loadProductDetail(product);
        }
    }

    public class HomeViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textviewtitle2;
        public TextView textviewdetail2;
        public ImageView imageview2;
        public ImageView imageview3;
        public ImageView imageview4;
        public ImageView imageview5;
        public ConstraintLayout constraintlayouthomelist1;

        public HomeViewHolder1(View view) {
            super(view);
            textviewtitle2 = (TextView)view.findViewById(R.id.tv_home_list_title2);
            textviewdetail2 = (TextView)view.findViewById(R.id.tv_home_list_detail2);
            imageview2 = (ImageView)view.findViewById(R.id.iv_home_list_image2);
            imageview3 = (ImageView)view.findViewById(R.id.iv_home_list_image3);
            imageview4 = (ImageView)view.findViewById(R.id.iv_home_list_image4);
            imageview5 = (ImageView)view.findViewById(R.id.iv_home_list_image5);
            constraintlayouthomelist1 = (ConstraintLayout)view.findViewById(R.id.layout_home_list1);

//            imageview2.setOnClickListener(this);
//            imageview3.setOnClickListener(this);
//            imageview4.setOnClickListener(this);
//            imageview5.setOnClickListener(this);
            constraintlayouthomelist1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("HomeRecyclerViewButton","fertalizer_home_viewHolder1");

            Product product = (Product) mObjectsList.get(getAdapterPosition());
            ((MainActivity) mContext).loadProductDetail(product);
        }
    }

    public class HotsTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView hotstitle;

        public HotsTitleViewHolder(View view) {
            super(view);
            hotstitle = (TextView)view.findViewById(R.id.tv_hots_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectsList.get(position) instanceof String) {
            return ITEM_TWO;
        } else if (position == 1) {
            return ITEM_ZERO;
        } else {
            return ITEM_ONE;
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        RecyclerView.ViewHolder homeViewHolder = null;
////        if(viewType == ITEM_ZERO) {
////            View view = mInflater.inflate(R.layout.item_home_list0, viewGroup, false);
////            homeViewHolder = new HomeViewHolder0(view);
////        } else {
////            View view = mInflater.inflate(R.layout.item_home_list1, viewGroup, false);
////            homeViewHolder = new HomeViewHolder1(view);
////        }

        RecyclerView.ViewHolder homeViewHolder = null;
        if (viewType == ITEM_TWO) {
            View view = mInflater.inflate(R.layout.item_hots_title, viewGroup, false);
            homeViewHolder = new HotsTitleViewHolder(view);
        } else if (viewType == ITEM_ZERO) {
            View view = mInflater.inflate(R.layout.item_home_list0, viewGroup, false);
            homeViewHolder = new HomeViewHolder0(view);
        } else {
            View view = mInflater.inflate(R.layout.item_home_list1, viewGroup, false);
            homeViewHolder = new HomeViewHolder1(view);
        }

        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = mObjectsList.get(position);
//        Product product = mProductListData.get(position);
//        NewHots hots = mHotsListData.get(position);

//
        if (holder instanceof HomeViewHolder0) {
            ((HomeViewHolder0) holder).textviewtitle1.setText(((Product) object).getTitle());
            ((HomeViewHolder0) holder).textviewdetail1.setText(((Product) object).getDescription());
            new DownloadProductImageTask(((HomeViewHolder0) holder).imageview1, ((Product) object).getImages().get(0))
                    .executeOnExecutor(Executors.newCachedThreadPool());
        } else if (holder instanceof HomeViewHolder1) {
            ((HomeViewHolder1) holder).textviewtitle2.setText(((Product) object).getTitle());
            ((HomeViewHolder1) holder).textviewdetail2.setText(((Product) object).getDescription());
            new DownloadProductImageTask(((HomeViewHolder1) holder).imageview2, ((Product) object).getImages().get(0))
                    .executeOnExecutor(Executors.newCachedThreadPool());
            new DownloadProductImageTask(((HomeViewHolder1) holder).imageview3, ((Product) object).getImages().get(1))
                    .executeOnExecutor(Executors.newCachedThreadPool());
            new DownloadProductImageTask(((HomeViewHolder1) holder).imageview4, ((Product) object).getImages().get(2))
                    .executeOnExecutor(Executors.newCachedThreadPool());
            new DownloadProductImageTask(((HomeViewHolder1) holder).imageview5, ((Product) object).getImages().get(3))
                    .executeOnExecutor(Executors.newCachedThreadPool());
        } else {
            ((HotsTitleViewHolder) holder).hotstitle.setText(((String) object));
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("Mark", "mProductListData.size() = " + mProductListData.size());
        return mObjectsList.size();
//        return mProductListData.size();
//        return mHotsListData.size();
    }

    public class DownloadProductImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadProductImageTask(ImageView imageView, String url) {
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
