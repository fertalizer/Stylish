package com.mark.stylish.fragments;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.stylish.MainActivity;
import com.mark.stylish.R;
import com.mark.stylish.StylishDataBaseHelper;
import com.mark.stylish.adapters.ProductDetailAdapter;
import com.mark.stylish.adapters.ProductDetailDialogColorAdapter;
import com.mark.stylish.adapters.ProductDetailDialogSizeAdapter;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.util.ArrayList;

public class ProductDetailFragment extends Fragment {
    private Product mProduct;
    private RecyclerView mRecyclerView;
    private ProductDetailAdapter mProductDetailAdapter;
    private TextView mTextViewAddToCart;
    private BottomSheetDialog mBottomSheetDialogAddToCart;
    private ImageView mImageViewAddToCartDialogClose;
    private TextView mTextViewAddToCartDialogTitle;
    private TextView mTextViewAddToCartDialogPrice;
    private RecyclerView mRecyclerViewAddToCartDialogColorList;
    private RecyclerView mRecyclerViewAddToCartDialogSizeList;
    private ProductDetailDialogColorAdapter mProductDetailDialogColorAdapter;
    private ProductDetailDialogSizeAdapter mProductDetailDialogSizeAdapter;
    private TextView mTextViewSelectAmount;
    private TextView mTextViewStockNumber;
    private TextView mTextViewErrorMessage;
    private ImageView mImageViewAddButton;
    private ImageView mImageViewSubtractButton;
    private EditText mEditTextNumberNow;
    private TextView mTextViewDialogAddToCart;
    private int mStockNumber;
    private int mNumberNow;
    private  String mNumberNowString;
    private SQLiteDatabase mDatabase;
    private Dialog mDialogAddSuccessfully;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProduct = (Product) getArguments().getSerializable("Product");


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        mRecyclerView = view.findViewById(R.id.rv_product_detail);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mProductDetailAdapter = new ProductDetailAdapter(getContext(), mProduct);
        mRecyclerView.setAdapter(mProductDetailAdapter);

        mTextViewAddToCart = (TextView)view.findViewById(R.id.btn_add_to_cart);
        initializeDialogView();
        setTitleAndPrice(mProduct);
        showColorBar(mProduct);

        dialogPopUp();
        dialogDismiss();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).toolbarAndBottomNavigationDisappear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).toolbarAndBottomNavigationAppear();
    }

    private void initializeDialogView() {
        mBottomSheetDialogAddToCart = new BottomSheetDialog(getActivity());
        View dialogAddToCartView = getLayoutInflater().inflate(R.layout.dialog_product_detail, null);
        mBottomSheetDialogAddToCart.setContentView(dialogAddToCartView);
        mImageViewAddToCartDialogClose = dialogAddToCartView.findViewById(R.id.iv_dialog_product_close_button);
        mTextViewAddToCartDialogTitle = dialogAddToCartView.findViewById(R.id.tv_dialog_product_title);
        mTextViewAddToCartDialogPrice = dialogAddToCartView.findViewById(R.id.tv_dialog_product_price);
        mRecyclerViewAddToCartDialogColorList = dialogAddToCartView.findViewById(R.id.rv_dialog_product_color_list);
        mRecyclerViewAddToCartDialogSizeList = dialogAddToCartView.findViewById(R.id.rv_dialog_product_size_list);
        mTextViewSelectAmount = dialogAddToCartView.findViewById(R.id.tv_dialog_product_amount_select);
        mTextViewStockNumber = dialogAddToCartView.findViewById(R.id.tv_dialog_product_stock);
        mTextViewErrorMessage = dialogAddToCartView.findViewById(R.id.tv_dialog_product_error_message);
        mImageViewAddButton = dialogAddToCartView.findViewById(R.id.iv_dialog_product_add_button);
        mImageViewSubtractButton = dialogAddToCartView.findViewById(R.id.iv_dialog_product_subtract_button);
        mEditTextNumberNow = dialogAddToCartView.findViewById(R.id.et_product_detail_number_input);
        mTextViewDialogAddToCart = dialogAddToCartView.findViewById(R.id.btn_dialog_add_to_cart);

        mImageViewAddButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));
        mImageViewSubtractButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));

        mImageViewAddButton.setClickable(false);
        mImageViewSubtractButton.setClickable(false);
        mTextViewDialogAddToCart.setClickable(false);
    }

    private void setTitleAndPrice(Product product) {
        String titleString = product.getTitle();
        String priceString = "NT$" + product.getPrice();

        mTextViewAddToCartDialogTitle.setText(titleString);
        mTextViewAddToCartDialogPrice.setText(priceString);

//        removeDuplicateColor(product.getVariants());

    }

    private void dialogPopUp() {
        //dialog pop up
        mTextViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialogAddToCart.show();
            }
        });
    }

    private void dialogDismiss() {
        //dialog dismiss
        mImageViewAddToCartDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialogAddToCart.dismiss();
            }
        });
    }

    public void showColorBar(Product product) {
        LinearLayoutManager layoutManagerColor = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewAddToCartDialogColorList.setLayoutManager(layoutManagerColor);
        mRecyclerViewAddToCartDialogColorList.setHasFixedSize(true);
        mProductDetailDialogColorAdapter = new ProductDetailDialogColorAdapter(getContext(),
                this, product);
        mRecyclerViewAddToCartDialogColorList.setAdapter(mProductDetailDialogColorAdapter);
    }

    public void showSizeBar(Product product, ArrayList<Variants> variantsArrayList) {
        LinearLayoutManager layoutManagerSize = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewAddToCartDialogSizeList.setLayoutManager(layoutManagerSize);
        mRecyclerViewAddToCartDialogSizeList.setHasFixedSize(true);
        mProductDetailDialogSizeAdapter = new ProductDetailDialogSizeAdapter(getContext(),
                this, product, variantsArrayList);
        mRecyclerViewAddToCartDialogSizeList.setAdapter(mProductDetailDialogSizeAdapter);
    }


    public ArrayList<Variants> classifyVariants(Product product, Color color) {
        ArrayList<Variants> variants = new ArrayList<>();
        for (int i = 0; i < product.getVariants().size(); i++) {
            if (product.getVariants().get(i).getColorCode().equals(color.getCode())) {
                variants.add(product.getVariants().get(i));
            }
        }
        return variants;
    }

    public void showStockNumber(final ArrayList<Variants> variantsArrayList, final int position) {
        final String stockString;

        mStockNumber = variantsArrayList.get(position).getStock();
        stockString =  getResources().getString(R.string.productDetailShowStock) + mStockNumber;

        mDialogAddSuccessfully = new Dialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_add_successfully, null);
        mDialogAddSuccessfully.setContentView(view);

        mTextViewStockNumber.setTextColor(android.graphics.Color.parseColor("#646464"));
        mTextViewSelectAmount.setTextColor(android.graphics.Color.parseColor("#646464"));

        if (variantsArrayList.get(position).getStock() == 0) {
            mTextViewStockNumber.setVisibility(View.GONE);
        } else {
            mTextViewStockNumber.setText(stockString);
            mTextViewStockNumber.setVisibility(View.VISIBLE);
        }
        mEditTextNumberNow.setTextColor(android.graphics.Color.parseColor("#3f3a3a"));
        mEditTextNumberNow.setBackgroundResource(R.drawable.edit_text_background_opacity_100);
        mTextViewErrorMessage.setVisibility(View.GONE);

        if (variantsArrayList.get(position).getStock() == 0) {
            mEditTextNumberNow.setText("0");
            mImageViewSubtractButton.setClickable(false);
            mImageViewSubtractButton.setBackgroundResource(R.drawable.subtract_button_background_opacity_40);
            mImageViewSubtractButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));
            mImageViewAddButton.setClickable(false);
            mImageViewAddButton.setBackgroundResource(R.drawable.add_button_background_opacity_40);
            mImageViewAddButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));
        } else {
            mEditTextNumberNow.setText("1");
            mNumberNowString = mEditTextNumberNow.getText().toString().trim();
            mNumberNow = Integer.parseInt(mNumberNowString);
            mImageViewAddButton.setBackgroundResource(R.drawable.add_button_background_opacity_100);
            mImageViewAddButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity100));
            mTextViewDialogAddToCart.setClickable(true);
            mTextViewDialogAddToCart.setBackgroundColor(getResources().getColor(R.color.colorDetailDialogOpacity100));
        }

        mEditTextNumberNow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextViewErrorMessage.setVisibility(View.GONE);
                mTextViewStockNumber.setTextColor(android.graphics.Color.parseColor("#646464"));
                mTextViewSelectAmount.setTextColor(android.graphics.Color.parseColor("#646464"));
                mTextViewDialogAddToCart.setBackgroundColor(getResources()
                        .getColor(R.color.colorDetailDialogUnlockAddToCart));

                mNumberNowString = mEditTextNumberNow.getText().toString().trim();
                if (!"".equals(mNumberNowString)) {
                    mNumberNow = Integer.parseInt(mNumberNowString);
                    Log.d("EditText", "Initial mNumberNowString = " + mNumberNowString);
                    Log.d("EditText", "Initial mNumberNow = " + mNumberNow);
                }
            }
        });


        mImageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberNow < mStockNumber) {
                    mNumberNow += 1;
                    mEditTextNumberNow.setText("" + mNumberNow);
                    mImageViewSubtractButton.setClickable(true);
                    mImageViewSubtractButton.setBackgroundResource(R.drawable.subtract_button_background_opacity_100);
                    mImageViewSubtractButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity100));
                }
                if (mNumberNow == mStockNumber) {
                    mImageViewAddButton.setClickable(false);
                    mImageViewAddButton.setBackgroundResource(R.drawable.add_button_background_opacity_40);
                    mImageViewAddButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));
                }
            }
        });

        mImageViewSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberNow > 1 && mNumberNow <= mStockNumber) {
                    mNumberNow -= 1;
                    mEditTextNumberNow.setText("" + mNumberNow);
                    mImageViewAddButton.setClickable(true);
                    mImageViewAddButton.setBackgroundResource(R.drawable.add_button_background_opacity_100);
                    mImageViewAddButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity100));
                }
                if (mNumberNow == 1) {
                    mImageViewSubtractButton.setClickable(false);
                    mImageViewSubtractButton.setBackgroundResource(R.drawable.subtract_button_background_opacity_40);
                    mImageViewSubtractButton.setColorFilter(getResources().getColor(R.color.colorDetailDialogOpacity40));
                }
            }
        });

        mTextViewDialogAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberNow <= mStockNumber && mNumberNow > 0) {
//                    Toast.makeText(getContext(),"加入成功", Toast.LENGTH_SHORT).show();
                    StylishDataBaseHelper stylishHelper = new StylishDataBaseHelper(getContext());
                    mDatabase = stylishHelper.getWritableDatabase();
                    boolean isProductExist = stylishHelper.isProductExist(mDatabase,
                            mProduct, variantsArrayList.get(position));
                    if (isProductExist) {
                        stylishHelper.updateProductInfo(mDatabase, mProduct,
                                variantsArrayList.get(position), mNumberNow);
                    } else {
                        stylishHelper.insertProductInfo(mDatabase,
                                mProduct, variantsArrayList.get(position), mNumberNow);
                    }
                    mDialogAddSuccessfully.show();
                    Cursor cursor = stylishHelper.getAllProductInfo(mDatabase);
                    int amount = cursor.getCount();
                    ((MainActivity)getActivity()).updateBadge(amount);
                } else {
                    outOfStock();
                }
            }
        });
    }

    private void outOfStock() {
        mTextViewDialogAddToCart.setBackgroundColor(getResources()
                .getColor(R.color.colorDetailDialogLockAddToCart));
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
        mTextViewStockNumber.setTextColor(android.graphics.Color.parseColor("#d0021b"));
        mTextViewSelectAmount.setTextColor(android.graphics.Color.parseColor("#d0021b"));
    }

//    private void removeDuplicateColor(ArrayList<Variants> variantsArrayList) {
//        ArrayList<String> colorList = new ArrayList<>();
//        colorList.add(variantsArrayList.get(0).getColorCode());
//        for (int i = 1; i < variantsArrayList.size(); i ++) {
//            boolean isDuplicate = false;
//            for (int j = 0; j < colorList.size(); j++) {
//                if(colorList.get(j).equals(variantsArrayList.get(i).getColorCode())) {
//                    isDuplicate = true;
//                    break;
//                }
//            }
//            if(!isDuplicate) {
//                colorList.add(variantsArrayList.get(i).getColorCode());
//            }
//        }
//        Log.d("ProductDetail", "colorList = " + colorList);
//    }
}
