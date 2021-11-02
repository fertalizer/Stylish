package com.mark.stylish.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mark.stylish.R;
import com.mark.stylish.adapters.HomeAdapter;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    private HomeAdapter mHomeAdapter;
//    private ArrayList<Hots> mHotsList = new ArrayList<>();
    private ArrayList<Object> mObjectsList;
//    private ArrayList<Product> mProductList = new ArrayList<Product>();
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);


//        mProductList.add(new Product(getString(R.string.productTitle1), getString(R.string.productDetail1)));
//        mProductList.add(new Product(getString(R.string.productTitle2), getString(R.string.productDetail2)));
//        mProductList.add(new Product(getString(R.string.productTitle3), getString(R.string.productDetail3)));

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    Log.d("TAG","獲取數據成功");
//                    Log.d("TAG","response.code()=="+response.code());
//                    Log.d("TAG","response.body().string()=="+response.body().string());
//                }
//            }
//        });
        mHomeAdapter = new HomeAdapter(getContext());
        mRecyclerView.setAdapter(mHomeAdapter);

//        mHomeAdapter = new HomeAdapter(getActivity(), mProductList, mHotsList);
//        mRecyclerView.setAdapter(mHomeAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mObjectsList = new ArrayList<>();
        String url = "https://api.appworks-school.tw/api/1.0/marketing/hots";
        if (fileIsExist()) {
            Log.i("Mark", "File exists");
            String jsonString = readfromInternal();
            Log.i("Mark", "jsonString = " + jsonString);
            parseDataFromInternal(jsonString);
        } else {
            Log.i("Mark", "Call Api");
            new HomeTask().execute(url);
        }
    }

    public void saveToInternal(String contents) {
        String filename = "stylishApi.txt";
        String fileContents = contents;
        FileOutputStream outputStream;
        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readfromInternal() {
        String filename = "stylishApi.txt";
        String jsonString = "";
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        StringBuilder builder;
        String line;
        try {
            fileInputStream = getContext().openFileInput(filename);
            inputStreamReader = new InputStreamReader(fileInputStream);
            reader = new BufferedReader(inputStreamReader);
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            jsonString = builder.toString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public boolean fileIsExist() {
        String filename = "stylishApi.txt";
        boolean isExist = false;
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        StringBuilder builder;
        String line;
        try {
            fileInputStream = getContext().openFileInput(filename);
            inputStreamReader = new InputStreamReader(fileInputStream);
            reader = new BufferedReader(inputStreamReader);
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            isExist = (builder.toString()) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public void parseDataFromInternal(String jsonString) {
        Product product;
        Variants variants;
        Color color;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject eachItemInData = dataArray.getJSONObject(i);
                JSONArray productsArray = eachItemInData.getJSONArray("products");
                String dataTitle = eachItemInData.getString("title");
                mObjectsList.add(dataTitle);
                for (int j = 0; j < productsArray.length(); j++) {
                    JSONObject eachItemInProducts = productsArray.getJSONObject(j);
                    JSONArray imagesArray = eachItemInProducts.getJSONArray("images");
                    JSONArray sizesArray = eachItemInProducts.getJSONArray("sizes");
                    product = new Product();
                    for (int k = 0; k < imagesArray.length(); k++) {
                        String images = imagesArray.getString(k);
                        product.getImages().add(images);
                    }
                    for (int k = 0; k < sizesArray.length(); k++) {
                        String sizes = sizesArray.getString(k);
                        product.getSize().add(sizes);
                    }
                    JSONArray variantsArray = eachItemInProducts.getJSONArray("variants");
                    for (int k = 0; k < variantsArray.length(); k++) {
                        variants = new Variants();
                        JSONObject eachItemInVariants = variantsArray.getJSONObject(k);
                        String colorCode = eachItemInVariants.getString("color_code");
                        String size = eachItemInVariants.getString("size");
                        String stockString = eachItemInVariants.getString("stock");
                        int stock = Integer.parseInt(stockString);
                        variants.setColorCode(colorCode);
                        variants.setSize(size);
                        variants.setStock(stock);
                        product.getVariants().add(variants);
                    }
                    JSONArray colorsArray = eachItemInProducts.getJSONArray("colors");
                    for (int k = 0; k < colorsArray.length(); k++) {
                        color = new Color();
                        JSONObject eachItemInColors = colorsArray.getJSONObject(k);
                        String colorCode = eachItemInColors.getString("code");
                        String name = eachItemInColors.getString("name");
                        color.setCode(colorCode);
                        color.setName(name);
                        product.getColors().add(color);
                    }
                    product.setTitle(eachItemInProducts.getString("title"));
                    product.setDescription(eachItemInProducts.getString("description"));
                    product.setId(eachItemInProducts.getString("id"));
                    product.setTexture(eachItemInProducts.getString("texture"));
                    product.setWash(eachItemInProducts.getString("wash"));
                    product.setPlace(eachItemInProducts.getString("place"));
                    product.setNote(eachItemInProducts.getString("note"));
                    product.setStory(eachItemInProducts.getString("story"));
                    String priceString = eachItemInProducts.getString("price");
                    int price = Integer.parseInt(priceString);
                    product.setPrice(price);
                    product.setMainImage(eachItemInProducts.getString("main_image"));

                    Log.d("Mark", "Product 標題 = " + product.getTitle());
                    Log.d("Mark", "Product 內容 = " + product.getDescription());
                    Log.d("Mark", "Product 圖片 = " + product.getImages());
                    Log.d("Mark", "Product ID = " + product.getId());

//                        mHomeAdapter.updateProduct(product);
                    mObjectsList.add(product);
                }
//                    newHots.setTitle(eachItemInData.getString("title"));
//                    Log.d("Mark", "NewHots 標題 = " + newHots.getTitle());
//                    Log.d("Mark", "NewHots ProductList = " + newHots.getProducts());
//                    mObjectsList.add(newHots);
                Log.d("Mark", "HotsList = " + mObjectsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHomeAdapter.updateHomeData(mObjectsList);
    }

    public class HomeTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... urls) {
            String jsonString = null;
            try {
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();
                Response response = client.newCall(request).execute();
                jsonString = response.body().string();
                Log.d("TAG", "JSON String = " + jsonString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            saveToInternal(jsonString);
            return jsonString;  // pass json string to onPostExecute method
        }

        @Override
        protected void onPostExecute(String jsonString) { // Do in main thread
//            Hots hots;
//            NewHots newHots;
            Product product;
            Variants variants;
            Color color;
            Log.d("TAG", "s = " + jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject eachItemInData = dataArray.getJSONObject(i);
                    JSONArray productsArray = eachItemInData.getJSONArray("products");
                    String dataTitle = eachItemInData.getString("title");
                    mObjectsList.add(dataTitle);
                    for (int j = 0; j < productsArray.length(); j++) {
                        JSONObject eachItemInProducts = productsArray.getJSONObject(j);
                        JSONArray imagesArray = eachItemInProducts.getJSONArray("images");
                        JSONArray sizesArray = eachItemInProducts.getJSONArray("sizes");
                        product = new Product();
                        for (int k = 0; k < imagesArray.length(); k++) {
                            String images = imagesArray.getString(k);
                            product.getImages().add(images);
                        }
                        for (int k = 0; k < sizesArray.length(); k++) {
                            String sizes = sizesArray.getString(k);
                            product.getSize().add(sizes);
                        }
                        JSONArray variantsArray = eachItemInProducts.getJSONArray("variants");
                        for (int k = 0; k < variantsArray.length(); k++) {
                            variants = new Variants();
                            JSONObject eachItemInVariants = variantsArray.getJSONObject(k);
                            String colorCode = eachItemInVariants.getString("color_code");
                            String size = eachItemInVariants.getString("size");
                            String stockString = eachItemInVariants.getString("stock");
                            int stock = Integer.parseInt(stockString);
                            variants.setColorCode(colorCode);
                            variants.setSize(size);
                            variants.setStock(stock);
                            product.getVariants().add(variants);
                        }
                        JSONArray colorsArray = eachItemInProducts.getJSONArray("colors");
                        for (int k = 0; k < colorsArray.length(); k++) {
                            color = new Color();
                            JSONObject eachItemInColors = colorsArray.getJSONObject(k);
                            String colorCode = eachItemInColors.getString("code");
                            String name = eachItemInColors.getString("name");
                            color.setCode(colorCode);
                            color.setName(name);
                            product.getColors().add(color);
                        }
                        product.setTitle(eachItemInProducts.getString("title"));
                        product.setDescription(eachItemInProducts.getString("description"));
                        product.setId(eachItemInProducts.getString("id"));
                        product.setTexture(eachItemInProducts.getString("texture"));
                        product.setWash(eachItemInProducts.getString("wash"));
                        product.setPlace(eachItemInProducts.getString("place"));
                        product.setNote(eachItemInProducts.getString("note"));
                        product.setStory(eachItemInProducts.getString("story"));
                        String priceString = eachItemInProducts.getString("price");
                        int price = Integer.parseInt(priceString);
                        product.setPrice(price);
                        product.setMainImage(eachItemInProducts.getString("main_image"));

                        Log.d("Mark", "Product 標題 = " + product.getTitle());
                        Log.d("Mark", "Product 內容 = " + product.getDescription());
                        Log.d("Mark", "Product 圖片 = " + product.getImages());
                        Log.d("Mark", "Product ID = " + product.getId());

//                        mHomeAdapter.updateProduct(product);
                        mObjectsList.add(product);
                    }
//                    newHots.setTitle(eachItemInData.getString("title"));
//                    Log.d("Mark", "NewHots 標題 = " + newHots.getTitle());
//                    Log.d("Mark", "NewHots ProductList = " + newHots.getProducts());
//                    mObjectsList.add(newHots);
                    Log.d("Mark", "HotsList = " + mObjectsList);
                }

                //Take out ArrayList<Product> from ArrayList<NewHots>
//                ArrayList<Product> takeOutProductList = new ArrayList<>();
//                for (int i = 0; i < mHotsList.size(); i++) {
//                    takeOutProductList.addAll(mHotsList.get(i).getProductList());
//                }
//                Log.d("Mark", "takeOutProductList.size(): " + takeOutProductList.size());


//                mHomeAdapter = new HomeAdapter(getActivity(), takeOutProductList);
                mHomeAdapter.updateHomeData(mObjectsList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
