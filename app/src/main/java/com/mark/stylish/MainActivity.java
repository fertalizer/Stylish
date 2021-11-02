package com.mark.stylish;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mark.stylish.fragments.CartFragment;
import com.mark.stylish.fragments.CatalogFragment;
import com.mark.stylish.fragments.HomeFragment;
import com.mark.stylish.fragments.ProductDetailFragment;
import com.mark.stylish.fragments.ProfileFragment;
import com.mark.stylish.objects.Color;
import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mark.stylish.MyApp.getContext;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ImageView mLogo;
    private TextView mCatalogTitle;
    private TextView mCartTitle;
    private TextView mProfileTitle;
    private TextView mFacebookLogInButton;
    private BottomSheetDialog mBottomSheetDialog;
    private LoginManager mLoginManager;
    private CallbackManager mCallbackManager;
    private Button mButtonDialogClose;
    private Toolbar mToolbar;
    private View mBadge;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogo = (ImageView)findViewById(R.id.iv_logo);
        mCatalogTitle = (TextView)findViewById(R.id.tv_catalog);
        mCartTitle = (TextView)findViewById(R.id.tv_cart);
        mProfileTitle = (TextView)findViewById(R.id.tv_profile);
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.btv_navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        mBadge = LayoutInflater.from(this).inflate(R.layout.badge_main_bottom, itemView, true);
        StylishDataBaseHelper stylishDataBaseHelper = new StylishDataBaseHelper(this);
        mDatabase = stylishDataBaseHelper.getWritableDatabase();
        Cursor cursor = stylishDataBaseHelper.getAllProductInfo(mDatabase);
        int amount = cursor.getCount();
        updateBadge(amount);

        mToolbar = (Toolbar)findViewById(R.id.tb_toolbar_home);

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            String productId = bundle.getString("productId");
//            Log.i("Mark", "onCreate ID from another APP = " + productId);
//            new getDetailTask(productId).execute();
//        }

        mCatalogTitle.setVisibility(View.INVISIBLE);
        mCartTitle.setVisibility(View.INVISIBLE);
        mProfileTitle.setVisibility(View.INVISIBLE);
        loadFragment(new HomeFragment());
        // init facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
//        loadFragment(new ProfileFragment());
//        printHashKey(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setIntent(intent);
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            String productId = bundle.getString("productId");
//            Log.i("Mark", "onNewIntent ID from another APP = " + productId);
//            new getDetailTask(productId).execute();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    mLogo.setVisibility(View.VISIBLE);
                    mCatalogTitle.setVisibility(View.INVISIBLE);
                    mCartTitle.setVisibility(View.INVISIBLE);
                    mProfileTitle.setVisibility(View.INVISIBLE);
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_catalog:
                    mLogo.setVisibility(View.INVISIBLE);
                    mCatalogTitle.setVisibility(View.VISIBLE);
                    mCartTitle.setVisibility(View.INVISIBLE);
                    mProfileTitle.setVisibility(View.INVISIBLE);
                    fragment = new CatalogFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    mLogo.setVisibility(View.INVISIBLE);
                    mCatalogTitle.setVisibility(View.INVISIBLE);
                    mCartTitle.setVisibility(View.VISIBLE);
                    mProfileTitle.setVisibility(View.INVISIBLE);
                    fragment = new CartFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    if (null == UserManager.getInstance().getSharedPreferences()) {
                        showFacebookLogInDialog();
                        return false;
                    } else {
                        mLogo.setVisibility(View.INVISIBLE);
                        mCatalogTitle.setVisibility(View.INVISIBLE);
                        mCartTitle.setVisibility(View.INVISIBLE);
                        mProfileTitle.setVisibility(View.VISIBLE);
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    default:
            }
            return false;
        }
    };

    public void updateBadge(int amount) {
        if (amount == 0) {
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setVisibility(View.VISIBLE);
            ((TextView) mBadge.findViewById(R.id.text_badge_main)).setText(String.valueOf(amount));
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }


    public void loadProductDetail(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Product", product);
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, productDetailFragment); //
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToPay(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment); //
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void toolbarAndBottomNavigationDisappear() {
        mToolbar.setVisibility(View.GONE);
        mBottomNavigationView.setVisibility(View.GONE);
    }

    public void toolbarAndBottomNavigationAppear() {
        getSupportFragmentManager().popBackStack();
        mToolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void showFacebookLogInDialog() {
        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        final View dialogLogInView = getLayoutInflater().inflate(R.layout.dialog_log_in_window, null);
//        Window window = mBottomSheetDialog.getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(dialogLogInView);
        mBottomSheetDialog.show();

        mFacebookLogInButton = (TextView)dialogLogInView.findViewById(R.id.tv_log_in_button);
        mButtonDialogClose = (Button)dialogLogInView.findViewById(R.id.btn_dialog_close_button);

        mFacebookLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                loginFB();
            }
        });

        mButtonDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    public void loginFB() {
        // init LoginManager & CallbackManager
        mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
        mLoginManager.logInWithReadPermissions(this, permissions);
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Log.d("FB", "Facebook getApplicationId: " + loginResult.getAccessToken().getApplicationId());
//                Log.d("FB", "Facebook getUserId: " + loginResult.getAccessToken().getUserId());
//                Log.d("FB", "Facebook getExpires: " + loginResult.getAccessToken().getExpires());
//                Log.d("FB", "Facebook getLastRefresh: " + loginResult.getAccessToken().getLastRefresh());
                Log.d("FB", "Facebook getToken: " + loginResult.getAccessToken().getToken());
//                Log.d("FB", "Facebook getSource: " + loginResult.getAccessToken().getSource());
//                Log.d("FB", "Facebook getRecentlyGrantedPermissions: " + loginResult.getRecentlyGrantedPermissions());
//                Log.d("FB", "Facebook getRecentlyDeniedPermissions: " + loginResult.getRecentlyDeniedPermissions());

                new GetStylishTokenTaskByFacebookToken(loginResult).execute();
            }

            @Override
            public void onCancel() {
                Log.d("FB", "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB", "Facebook onError:" + error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class GetStylishTokenTaskByFacebookToken extends AsyncTask<String, Void, String> {
        private OkHttpClient mOkHttpClient = new OkHttpClient();
        private LoginResult mLoginResult;

        public GetStylishTokenTaskByFacebookToken(LoginResult loginResult) {
            this.mLoginResult = loginResult;
        }

        String jsonString = null;

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("provider", "facebook")
                    .add("access_token", mLoginResult.getAccessToken().getToken())
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.appworks-school.tw/api/1.0/user/signin")
                    .post(body)
                    .build();
            Call call = mOkHttpClient.newCall(request);

            try {
                Response response = call.execute();
                jsonString = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            final Dialog dialogLogInSuccess;
            final Fragment fragment;

            super.onPostExecute(jsonString);
            Log.d("Stylish", "Stylish jason = " + jsonString);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject data = jsonObject.getJSONObject("data");
                String accessToken = data.getString("access_token");
                Log.d("Stylish", "jsonStringAccessToken = " + accessToken);


//                mSharedPreferences = getSharedPreferences("access_token", MODE_PRIVATE);
//                mSharedPreferences.edit().putString("HashKey", mStylishAccessToken).commit();
//                Log.d("Stylish", "SharePreferences = " + mStylishAccessToken);
//                String test = getSharedPreferences("access_token", MODE_PRIVATE).getString("HashKey", "");
//                Log.d("Stylish", "test = " + test);

                UserManager.getInstance().setSharedPreferences(accessToken);
                Log.d("Stylish", "UserMangerSharePreference = " + UserManager.getInstance().getSharedPreferences());

                mBottomSheetDialog.dismiss();

                dialogLogInSuccess = new Dialog(MainActivity.this);
                View dialogLogInSuccessView = getLayoutInflater().inflate(R.layout.dialog_add_successfully, null);
                dialogLogInSuccess.setContentView(dialogLogInSuccessView);
                dialogLogInSuccess.show();
                fragment = new ProfileFragment();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogLogInSuccess.dismiss();
                        loadFragment(fragment);
                    }
                }, 1500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    //    public void printHashKey(Context pContext) {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i("HashKey", "printHashKey() Hash Key: " + hashKey);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("HashKey", "printHashKey()", e);
//        } catch (Exception e) {
//            Log.e("HashKey", "printHashKey()", e);
//        }
//    }

    private class getDetailTask extends AsyncTask<String, Void, String> {
        private String id;
        private OkHttpClient client = new OkHttpClient();
        private Product product = new Product();
        private String host = "https://api.appworks-school.tw";
        private String api = "/api";
        private String version = "/1.0";
        private String products = "/products";
        private String details = "/details";
        private String accessId = "?id=";
        private String url = host + api + version + products + details + accessId;

        public getDetailTask(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {
                Request request = new Request.Builder()
                        .url(url + id)
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
            super.onPostExecute(jsonString);

            try {
                Log.i("Mark", jsonString);

                JSONObject object = new JSONObject(jsonString);
                JSONObject data = object.getJSONObject("data");

                String id = data.getString("id");
                product.setId(id);

                String title = data.getString("title");
                product.setTitle(title);

                String description = data.getString("description");
                product.setDescription(description);

                int price = data.getInt("price");
                product.setPrice(price);

                String texture = data.getString("texture");
                product.setTexture(texture);

                String wash = data.getString("wash");
                product.setWash(wash);

                String place = data.getString("place");
                product.setPlace(place);

                String note = data.getString("note");
                product.setNote(note);

                String mainImage = data.getString("main_image");
                product.setMainImage(mainImage);

                JSONArray colors = data.getJSONArray("colors");
                for (int i = 0; i < colors.length(); i++) {
                    JSONObject colorItem = colors.getJSONObject(i);
                    Color color = new Color();

                    String code = colorItem.getString("code");
                    color.setCode(code);

                    String name = colorItem.getString("name");
                    color.setName(name);

                    product.getColors().add(color);
                }

                JSONArray sizes = data.getJSONArray("sizes");
                for (int i = 0; i < sizes.length(); i++) {
                    String sizeItem = sizes.getString(i);

                    product.getSize().add(sizeItem);
                }

                JSONArray variants = data.getJSONArray("variants");
                for (int i = 0; i < variants.length(); i++) {
                    JSONObject variantItem = variants.getJSONObject(i);
                    Variants variant = new Variants();

                    String code = variantItem.getString("color_code");
                    variant.setColorCode(code);

                    String size = variantItem.getString("size");
                    variant.setSize(size);

                    int stock = variantItem.getInt("stock");
                    variant.setStock(stock);

                    product.getVariants().add(variant);
                }

                JSONArray images = data.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {
                    String image = images.getString(i);

                    product.getImages().add(image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            loadProductDetail(product);
        }
    }
}
