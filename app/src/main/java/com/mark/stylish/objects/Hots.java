package com.mark.stylish.objects;

import java.util.ArrayList;

public class Hots {
    private String mTitle;
    private ArrayList<Product> mProductList;
    String MyHots;

    public Hots() {
        this.mTitle = "";
        this.mProductList = new ArrayList<>();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<Product> getProductList() {
        return mProductList;
    }

    public void setProductList(ArrayList<Product> productList) {
        mProductList = productList;
    }
}
