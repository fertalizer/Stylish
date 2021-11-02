package com.mark.stylish.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String mId;
    private String mMainImage;
    private ArrayList<String> mImages;
    private String mTitle;
    private String mDescription;
    private int mPrice;
    private ArrayList<Stock> mStocks;
    private String mTexture;
    private String mWash;
    private String mPlace;
    private String mNote;
    private String mStory;
    private ArrayList<String> mSize;
    private ArrayList<Variants> mVariants;
    private ArrayList<Color> mColors;

    public Product() {
        this.mId = "";
        this.mMainImage = "";
        this.mImages = new ArrayList<>();
        this.mTitle = "";
        this.mDescription = "";
        this.mPrice = 0;
        this.mTexture = "";
        this.mWash = "";
        this.mPlace = "";
        this.mNote = "";
        this.mStory = "";
        this.mSize = new ArrayList<>();
        this.mVariants = new ArrayList<>();
        this.mColors = new ArrayList<>();
        Hots hots = new Hots();
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTexture() {
        return mTexture;
    }

    public void setTexture(String texture) {
        mTexture = texture;
    }

    public String getWash() {
        return mWash;
    }

    public void setWash(String wash) {
        mWash = wash;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public String getStory() {
        return mStory;
    }

    public void setStory(String story) {
        mStory = story;
    }

    public String getMainImage() {
        return mMainImage;
    }

    public void setMainImage(String mainImage) {
        mMainImage = mainImage;
    }

    public ArrayList<String> getSize() {
        return mSize;
    }

    public void setSize(ArrayList<String> size) {
        mSize = size;
    }

    public ArrayList<Variants> getVariants() {
        return mVariants;
    }

    public void setVariants(ArrayList<Variants> variants) {
        mVariants = variants;
    }

    public ArrayList<Color> getColors() {
        return mColors;
    }

    public void setColors(ArrayList<Color> colors) {
        mColors = colors;
    }
}
