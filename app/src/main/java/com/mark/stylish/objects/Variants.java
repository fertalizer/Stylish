package com.mark.stylish.objects;

import java.io.Serializable;

public class Variants implements Serializable {
    private String mColorCode;
    private String mSize;
    private int mStock;

    public Variants() {
        this.mColorCode = "";
        this.mSize = "";
        this.mStock = 0;
    }

    public String getColorCode() {
        return mColorCode;
    }

    public void setColorCode(String colorCode) {
        mColorCode = colorCode;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public int getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        mStock = stock;
    }
}
