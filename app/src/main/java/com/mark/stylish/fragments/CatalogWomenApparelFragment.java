package com.mark.stylish.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.stylish.R;
import com.mark.stylish.adapters.CatalogWomenApparelAdapter;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatalogWomenApparelFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CatalogWomenApparelAdapter mCatalogWomenApparelAdapter;
    private ArrayList<Object> mWomenObjectArrayList;
    private boolean isLoading;
    int mPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog_women_apparel, container, false);
        mRecyclerView = view.findViewById(R.id.rv_women_apparel);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mCatalogWomenApparelAdapter = new CatalogWomenApparelAdapter(getContext());
        mRecyclerView.setAdapter(mCatalogWomenApparelAdapter);

        mWomenObjectArrayList = new ArrayList<>();
        isLoading = false;
        mPage = 0;
        new WomenApparelTask().execute();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("Women Scroll", "-----------onScrollStateChanged-----------");
                Log.i("Women Scroll", "newState: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.i("Women Scroll", "-----------onScrolled-----------");
                Log.i("Women Scroll", "dx: " + dx);
                Log.i("Women Scroll", "dy: " + dy);
                Log.i("Women Scroll", "CHECK_SCROLL_UP: " + recyclerView.canScrollVertically(-1));
                Log.i("Women Scroll", "CHECK_SCROLL_DOWN: " + recyclerView.canScrollVertically(1));

                /* When recycler view touches bottom, App will call API.

                   Assume that calling API takes 10 seconds, if user drags view up and down and view
                   touches bottom again during 10 seconds, App will call API again.

                   Therefore, a condition is necessary for checking whether App is calling  API.
                 */

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    Log.i("Women Scroll", "We are at Bottom");
                    Log.i("Women Scroll", "isLoading =  " + isLoading);
                    isLoading = true;
                    new WomenApparelTask().execute();
                }
            }
        });

        return view;
    }

    public class WomenApparelTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {
                Request request = new Request.Builder()
                        .url("https://api.appworks-school.tw/api/1.0/products/women?paging=" + mPage)
                        .build();
                Response response = client.newCall(request).execute();
                jsonString = response.body().string();
                Log.d("TAG", "JSON String = " + jsonString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            isLoading = false;
            Product product;
            Variants variants;
            Color color;

            super.onPostExecute(jsonString);

            try {
                JSONObject object = new JSONObject(jsonString);
                if (jsonString.contains("next_paging")) {
                    String paging = object.getString("next_paging");
                    Log.d("WomenApparel", "Paging = " + paging);
                    mPage = Integer.parseInt(paging);
                    Log.d("WomenApparel", "Paging index = " + mPage);
                } else {
                    isLoading = true;
                }
                JSONArray dataArray = object.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    product = new Product();
                    JSONObject eachItemInData = dataArray.getJSONObject(i);
                    String dataId = eachItemInData.getString("id");
                    product.setId(dataId);
                    String dataTitle = eachItemInData.getString("title");
                    product.setTitle(dataTitle);
                    String dataPriceString = eachItemInData.getString("price");
                    int dataPrice = Integer.parseInt(dataPriceString);
                    product.setPrice(dataPrice);
                    String dataMainImage = eachItemInData.getString("main_image");
                    product.setMainImage(dataMainImage);
                    String dataTexture = eachItemInData.getString("texture");
                    product.setTexture(dataTexture);
                    String dataWash = eachItemInData.getString("wash");
                    product.setWash(dataWash);
                    String dataPlace = eachItemInData.getString("place");
                    product.setPlace(dataPlace);
                    String dataNote = eachItemInData.getString("note");
                    product.setNote(dataNote);
                    String dataStory = eachItemInData.getString("story");
                    product.setStory(dataStory);

                    Log.d("WomenApparel", "Title = " + dataTitle);
                    Log.d("WomenApparel", "Price = " + dataPrice);
                    Log.d("WomenApparel", "Main Image = " + dataMainImage);

                    JSONArray sizesArray = eachItemInData.getJSONArray("sizes");
                    for (int j = 0; j < sizesArray.length(); j++) {
                        String sizes = sizesArray.getString(j);
                        product.getSize().add(sizes);
                    }

                    JSONArray colorsArray = eachItemInData.getJSONArray("colors");
                    for (int j = 0; j < colorsArray.length(); j++) {
                        color = new Color();
                        JSONObject eachItemInColors = colorsArray.getJSONObject(j);
                        String code = eachItemInColors.getString("code");
                        String name = eachItemInColors.getString("name");
                        color.setCode(code);
                        color.setName(name);
                        product.getColors().add(color);
                    }

                    JSONArray variantsArray = eachItemInData.getJSONArray("variants");
                    for (int j = 0; j < variantsArray.length(); j++) {
                        variants = new Variants();
                        JSONObject eachItemInVariants = variantsArray.getJSONObject(j);
                        String colorCode = eachItemInVariants.getString("color_code");
                        String size = eachItemInVariants.getString("size");
                        String stockString = eachItemInVariants.getString("stock");
                        int stock = Integer.parseInt(stockString);
                        variants.setColorCode(colorCode);
                        variants.setSize(size);
                        variants.setStock(stock);
                        product.getVariants().add(variants);
                    }

                    JSONArray imagesArray = eachItemInData.getJSONArray("images");
                    for (int j = 0; j < imagesArray.length(); j++) {
                        String images = imagesArray.getString(j);
                        product.getImages().add(images);
                    }
                    mWomenObjectArrayList.add(product);

                    mCatalogWomenApparelAdapter.updateWomenData(mWomenObjectArrayList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


