package com.mark.stylish.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mark.stylish.R;
import com.mark.stylish.UserManager;
import com.mark.stylish.objects.Hots;
import com.mark.stylish.objects.User;

import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.dom.DOMLocator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    User mUser = new User();
    private ConstraintLayout mButtonAwaitingPayment;
    private ConstraintLayout mButtonAwaitingShipment;
    private ConstraintLayout mAButtonAwaitingShipped;
    private ConstraintLayout mButtonAwaitingReview;
    private ConstraintLayout mButtonExchange;
    private ConstraintLayout mButtonStarred;
    private ConstraintLayout mButtonNotification;
    private ConstraintLayout mButtonRefunded;
    private ConstraintLayout mButtonAddressed;
    private ConstraintLayout mButtonCustomerService;
    private ConstraintLayout mButtonSystemFeedback;
    private ConstraintLayout mButtonRegisterCellphone;
    private ConstraintLayout mButtonSettings;
    TextView mTextViewProfileUserName;
    ImageView mImageViewProfileUserPicture;
    private Toast mToast;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_awaiting_payment:
                showToast(getResources().getString(R.string.awaitingPaymentComingSoon));
                break;
            case R.id.layout_awaiting_shipment:
                showToast(getResources().getString(R.string.awaitingShipmentComingSoon));
                break;
            case R.id.layout_shipped:
                showToast(getResources().getString(R.string.shippedComingSoon));
                break;
            case R.id.layout_awaiting_review:
                showToast(getResources().getString(R.string.awaitingReviewComingSoon));
                break;
            case R.id.layout_exchange:
                showToast(getResources().getString(R.string.exchangeComingSoon));
                break;
            case R.id.layout_starred:
                showToast(getResources().getString(R.string.starredComingSoon));
                break;
            case R.id.layout_notification:
                showToast(getResources().getString(R.string.notificationComingSoon));
                break;
            case R.id.layout_refunded:
                showToast(getResources().getString(R.string.refundedComingSoon));
                break;
            case R.id.layout_addressed:
                showToast(getResources().getString(R.string.addressedComingSoon));
                break;
            case R.id.layout_customer_service:
                showToast(getResources().getString(R.string.customerServiceComingSoon));
                break;
            case R.id.layout_system_feedback:
                showToast(getResources().getString(R.string.systemFeedbackComingSoon));
                break;
            case R.id.layout_register_cellphone:
                showToast(getResources().getString(R.string.registerCellphoneComingSoon));
                break;
            case R.id.layout_settings:
                showToast(getResources().getString(R.string.settingsComingSoon));
                break;
            default:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mButtonAwaitingPayment = (ConstraintLayout)view.findViewById(R.id.layout_awaiting_payment);
        mButtonAwaitingShipment = (ConstraintLayout) view.findViewById(R.id.layout_awaiting_shipment);
        mAButtonAwaitingShipped = (ConstraintLayout) view.findViewById(R.id.layout_shipped);
        mButtonAwaitingReview = (ConstraintLayout) view.findViewById(R.id.layout_awaiting_review);
        mButtonExchange = (ConstraintLayout) view.findViewById(R.id.layout_exchange);
        mButtonStarred = (ConstraintLayout) view.findViewById(R.id.layout_starred);
        mButtonNotification = (ConstraintLayout) view.findViewById(R.id.layout_notification);
        mButtonRefunded = (ConstraintLayout) view.findViewById(R.id.layout_refunded);
        mButtonAddressed = (ConstraintLayout) view.findViewById(R.id.layout_addressed);
        mButtonCustomerService = (ConstraintLayout) view.findViewById(R.id.layout_customer_service);
        mButtonSystemFeedback = (ConstraintLayout) view.findViewById(R.id.layout_system_feedback);
        mButtonRegisterCellphone = (ConstraintLayout) view.findViewById(R.id.layout_register_cellphone);
        mButtonSettings = (ConstraintLayout) view.findViewById(R.id.layout_settings);
        mTextViewProfileUserName = (TextView) view.findViewById(R.id.tv_profile_user_name);
        mImageViewProfileUserPicture = (ImageView)view.findViewById(R.id.iv_profile_user_picture);


        mButtonAwaitingPayment.setOnClickListener(this);
        mButtonAwaitingShipment.setOnClickListener(this);
        mAButtonAwaitingShipped.setOnClickListener(this);
        mButtonAwaitingReview.setOnClickListener(this);
        mButtonExchange.setOnClickListener(this);
        mButtonStarred.setOnClickListener(this);
        mButtonNotification.setOnClickListener(this);
        mButtonRefunded.setOnClickListener(this);
        mButtonAddressed.setOnClickListener(this);
        mButtonCustomerService.setOnClickListener(this);
        mButtonSystemFeedback.setOnClickListener(this);
        mButtonRegisterCellphone.setOnClickListener(this);
        mButtonSettings.setOnClickListener(this);

        new GetUserDataByToken(mImageViewProfileUserPicture, mTextViewProfileUserName).execute();

        return view;
    }

    public class GetUserDataByToken extends AsyncTask<Void, Void, String> {
        private OkHttpClient client = new OkHttpClient();
        private User user = new User();
        String jsonString = null;
        ImageView imageView;
        TextView textView;

        public GetUserDataByToken(ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url("https://api.appworks-school.tw/api/1.0/user/profile")
                    .header("Authorization", "Bearer "
                            + UserManager.getInstance().getStylishToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                jsonString = response.body().string();
                Log.d("Profile", "Header = " + jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {

            super.onPostExecute(jsonString);

            try {
                new DownloadProfilePictureTask(jsonString, textView, imageView).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class DownloadProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
        User user  = new User();
        ImageView imageView;
        TextView textView;
        String jsonString;

        public DownloadProfilePictureTask(String jsonString, TextView textView, ImageView imageView) {
            this.textView = textView;
            this.imageView = imageView;
            this.jsonString = jsonString;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject data = jsonObject.getJSONObject("data");
                String name = data.getString("name");
                String picture = data.getString("picture");

                user.setName(name);
                user.setPicture(picture);

                Log.d("User", user.getName());
                Log.d("User", user.getPicture());

                InputStream inputStream = new URL(user.getPicture()).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            textView.setText(user.getName());
        }
    }

    private void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
