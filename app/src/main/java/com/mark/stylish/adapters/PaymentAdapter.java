package com.mark.stylish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.stylish.R;

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;

    public PaymentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public class  PaymentViewHolder extends RecyclerView.ViewHolder {
        public PaymentViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder paymentViewHolder = null;
        View view = mInflater.inflate(R.layout.item_payment_info, null);
        paymentViewHolder = new PaymentViewHolder(view);

        return paymentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
