package com.mark.stylish.objects;

public class User {
    private String mId;
    private String mName;
    private String mEmail;
    private String mPicture;
    private int mPurchase;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }
}
