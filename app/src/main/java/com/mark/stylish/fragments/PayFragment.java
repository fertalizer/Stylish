package com.mark.stylish.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.stylish.R;


public class PayFragment extends Fragment {
    RecyclerView mRecyclerViewPaymentInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        mRecyclerViewPaymentInfo = (RecyclerView)view.findViewById(R.id.rv_pay_payment_info);
        mRecyclerViewPaymentInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewPaymentInfo.setHasFixedSize(true);


        return view;
    }
}
