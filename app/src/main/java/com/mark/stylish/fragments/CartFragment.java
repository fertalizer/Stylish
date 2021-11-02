package com.mark.stylish.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.stylish.MainActivity;
import com.mark.stylish.R;
import com.mark.stylish.StylishDataBaseHelper;
import com.mark.stylish.adapters.CartAdapter;


public class CartFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CartAdapter mCartAdapter;
//    private TextView mTextViewPayButton;
    private SQLiteDatabase mDatabase;
    private StylishDataBaseHelper mStylishHelper;
    private TextView mTextViewGoToPay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
//        mTextViewPayButton = (TextView)view.findViewById(R.id.tv_cart_pay_button);
        mRecyclerView = view.findViewById(R.id.rv_cart_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mStylishHelper = new StylishDataBaseHelper(getContext());
        mDatabase = mStylishHelper.getWritableDatabase();
        Cursor cursor = mStylishHelper.getAllProductInfo(mDatabase);

        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                //Do Something
                Log.d("Cart Product ID", cursor
                        .getString(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_ID)));
                Log.d("Cart Product Image", cursor
                        .getString(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_MAIN_IMAGE)));
                Log.d("Cart Product Title", cursor
                        .getString(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_TITLE)));
                Log.d("Cart Product Price", "" + cursor
                        .getInt(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_PRICE)));
                Log.d("Cart Product Color", cursor
                        .getString(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_COLOR)));
                Log.d("Cart Product Size", cursor
                        .getString(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_SIZE)));
                Log.d("Cart Product Stock", "" + cursor
                        .getInt(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_STOCK)));
                Log.d("Cart Product Num", "" + cursor
                        .getInt(cursor.getColumnIndex(mStylishHelper.COLUMN_PRODUCT_SELECTED_AMOUNT)));
                cursor.moveToNext();
            }
        }

        mCartAdapter = new CartAdapter(getContext(), cursor, mStylishHelper, mDatabase);
        mRecyclerView.setAdapter(mCartAdapter);

        mTextViewGoToPay = (TextView)view.findViewById(R.id.tv_cart_pay_button);
        mTextViewGoToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PayFragment();
                ((MainActivity)getActivity()).goToPay(fragment);
            }
        });

        return view;
    }
}
