package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class MyOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView  txtMyorderId, txtMyorderPrice,txtMyorderDate,txtMyorderstatus;
    public Button myOrderCancel;
    public ItemClickListner itemLis;


    public MyOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMyorderId = (TextView) itemView.findViewById(R.id.myorder_id);
        txtMyorderPrice = (TextView) itemView.findViewById(R.id.myorder_total_price);
        txtMyorderDate = (TextView) itemView.findViewById(R.id.myorder_date);
        txtMyorderstatus = (TextView) itemView.findViewById(R.id.myorder_status);
        myOrderCancel = (Button) itemView.findViewById(R.id.myorder_cancel);

    }

    @Override
    public void onClick(View v) {

            itemLis.onClick(v, getAdapterPosition(), false);

    }

    public void setItemLis(ItemClickListner itemLis) {
        this.itemLis = itemLis;
    }
}
