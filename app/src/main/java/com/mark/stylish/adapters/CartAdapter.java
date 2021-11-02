package com.mark.stylish.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.mark.stylish.StylishDataBaseHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private Cursor mCursor;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private StylishDataBaseHelper mStylishHelper;
    private int mNewAmount;
    private int mStockNumber;

    public CartAdapter(Context context, Cursor cursor,
                       StylishDataBaseHelper stylishHelper, SQLiteDatabase sqLiteDatabase) {
        this.mInflater = LayoutInflater.from(context);
        this.mCursor = cursor;
        this.mContext = context;
        this.mStylishHelper = stylishHelper;
        this.mDatabase = sqLiteDatabase;
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        public ImageView imageviewproductmainimage;
        public TextView textviewproducttitle;
        public ImageView imageviewproductcolor;
        public TextView textviewproductsize;
        public TextView textviewproductprice;
        public TextView textviewproductamount;
        public ImageView imageviewaddbutton;
        public ImageView imageviewsubtractbutton;
        public TextView textviewremovebutton;

        public CartHolder(View view) {
            super(view);
            imageviewproductmainimage = (ImageView)view.findViewById(R.id.iv_cart_product_main_image);
            textviewproducttitle = (TextView)view.findViewById(R.id.tv_cart_product_title);
            imageviewproductcolor = (ImageView)view.findViewById(R.id.iv_cart_product_color);
            textviewproductsize = (TextView)view.findViewById(R.id.tv_cart_product_size);
            textviewproductprice = (TextView)view.findViewById(R.id.tv_cart_product_price);
            textviewproductamount = (TextView)view.findViewById(R.id.tv_cart_purchase_number);
            imageviewaddbutton = (ImageView)view.findViewById(R.id.iv_cart_add_button);
            imageviewsubtractbutton = (ImageView)view.findViewById(R.id.iv_cart_subtract_button);
            textviewremovebutton = (TextView)view.findViewById(R.id.tv_cart_remove_button);

            textviewremovebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(getAdapterPosition());
                    long id = mCursor.getLong(mCursor.getColumnIndex(mStylishHelper.KEY_ID));
                    mStylishHelper.removeProductInfo(mDatabase, id);
                    mCursor = mStylishHelper.getAllProductInfo(mDatabase);
                    int amount = mCursor.getCount();
                    ((MainActivity) mContext).updateBadge(amount);
                    notifyDataSetChanged();
                }
            });

            imageviewaddbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(getAdapterPosition());
                    mNewAmount = mCursor
                            .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_AMOUNT));
                    mStockNumber = mCursor
                            .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_STOCK));

                    if (mNewAmount < mStockNumber) {
                        mNewAmount++;
                        long id = mCursor.getLong(mCursor.getColumnIndex(mStylishHelper.KEY_ID));
                        mStylishHelper.plusProductAmount(mDatabase, id, mNewAmount);
                        mCursor = mStylishHelper.getAllProductInfo(mDatabase);
                        imageviewsubtractbutton.setClickable(true);
                        imageviewsubtractbutton.setBackgroundResource(R.drawable.subtract_button_background_opacity_100);
                        imageviewsubtractbutton.setColorFilter(R.color.colorDetailDialogOpacity100);
                        notifyDataSetChanged();
                    }

                    if (mNewAmount == mStockNumber) {
                        imageviewaddbutton.setClickable(false);
                        imageviewaddbutton.setBackgroundResource(R.drawable.add_button_background_opacity_40);
                        imageviewaddbutton.setColorFilter(R.color.colorDetailDialogOpacity40);
                    }
                }
            });

            imageviewsubtractbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(getAdapterPosition());
                    mNewAmount = mCursor
                            .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_AMOUNT));
                    if (mNewAmount > 1 && mNewAmount <= mStockNumber) {
                        mNewAmount--;
                        long id = mCursor.getLong(mCursor.getColumnIndex(mStylishHelper.KEY_ID));
                        mStylishHelper.minusProductAmount(mDatabase, id, mNewAmount);
                        mCursor = mStylishHelper.getAllProductInfo(mDatabase);
                        imageviewaddbutton.setClickable(true);
                        imageviewaddbutton.setBackgroundResource(R.drawable.add_button_background_opacity_100);
                        imageviewaddbutton.setColorFilter(R.color.colorDetailDialogOpacity100);
                        notifyDataSetChanged();
                    }

                    if (mNewAmount == 1) {
                        imageviewsubtractbutton.setClickable(false);
                        imageviewsubtractbutton.setBackgroundResource(R.drawable.subtract_button_background_opacity_40);
                        imageviewsubtractbutton.setColorFilter(R.color.colorDetailDialogOpacity40);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder cartViewHolder = null;
        View view = mInflater.inflate(R.layout.item_cart_list, viewGroup, false);
        cartViewHolder = new CartHolder(view);

        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        mNewAmount = mCursor
                .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_AMOUNT));
        mStockNumber = mCursor
                .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_STOCK));

        Log.d("Cart Adapter", "mNewAmount = " + mNewAmount);
        Log.d("Cart Adapter", "mStockNumber = " + mStockNumber);

        if (mNewAmount == mStockNumber) {
            ((CartHolder)holder).imageviewaddbutton.setClickable(false);
            ((CartHolder)holder).imageviewaddbutton.setBackgroundResource(R.drawable.add_button_background_opacity_40);
            ((CartHolder)holder).imageviewaddbutton.setColorFilter(R.color.colorDetailDialogOpacity40);
            ((CartHolder)holder).imageviewsubtractbutton.setClickable(true);
            ((CartHolder)holder).imageviewsubtractbutton.setBackgroundResource(R.drawable.subtract_button_background_opacity_100);
            ((CartHolder)holder).imageviewsubtractbutton.setColorFilter(R.color.colorDetailDialogOpacity100);
        }

        if (mNewAmount < mStockNumber && mNewAmount > 1) {
            ((CartHolder)holder).imageviewaddbutton.setClickable(true);
            ((CartHolder)holder).imageviewaddbutton.setBackgroundResource(R.drawable.add_button_background_opacity_100);
            ((CartHolder)holder).imageviewaddbutton.setColorFilter(R.color.colorDetailDialogOpacity100);
            ((CartHolder)holder).imageviewsubtractbutton.setClickable(true);
            ((CartHolder)holder).imageviewsubtractbutton.setBackgroundResource(R.drawable.subtract_button_background_opacity_100);
            ((CartHolder)holder).imageviewsubtractbutton.setColorFilter(R.color.colorDetailDialogOpacity100);
        }

        if (mStockNumber == 1) {
            ((CartHolder)holder).imageviewaddbutton.setClickable(false);
            ((CartHolder)holder).imageviewaddbutton.setBackgroundResource(R.drawable.add_button_background_opacity_40);
            ((CartHolder)holder).imageviewaddbutton.setColorFilter(R.color.colorDetailDialogOpacity40);
            ((CartHolder)holder).imageviewsubtractbutton.setClickable(false);
            ((CartHolder)holder).imageviewsubtractbutton.setBackgroundResource(R.drawable.subtract_button_background_opacity_40);
            ((CartHolder)holder).imageviewsubtractbutton.setColorFilter(R.color.colorDetailDialogOpacity40);
        }

        String productTitle = mCursor
                .getString(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_TITLE));
        int productPrice = mCursor
                .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_PRICE));
        String productPriceString = "NT$" + Integer.toString(productPrice);
        String productColor = mCursor
                .getString(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_COLOR));
        String productSize = mCursor
                .getString(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_SIZE));
        int productAmount = mCursor
                .getInt(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_AMOUNT));
        String productAmountString = Integer.toString(productAmount);


        ((CartHolder)holder).textviewproducttitle.setText(productTitle);
        ((CartHolder)holder).textviewproductprice.setText(productPriceString);
        ((CartHolder)holder).imageviewproductcolor.setBackgroundColor(Color.parseColor(productColor));
        ((CartHolder)holder).textviewproductsize.setText(productSize);
        ((CartHolder)holder).textviewproductamount.setText(productAmountString);

        String mainImageUrl = mCursor.getString(mCursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_MAIN_IMAGE));

        new DownloadCartImageTask(((CartHolder)holder).imageviewproductmainimage,
                mainImageUrl).executeOnExecutor(Executors.newCachedThreadPool());
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class DownloadCartImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        String url;

        public DownloadCartImageTask(ImageView imageView, String url) {
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
