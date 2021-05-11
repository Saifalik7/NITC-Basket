package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class MyHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtMyhistoryorderId, txtMyhistoryorderPrice,txtMyhistoryorderDate,txtMyhistoryorderstatus;
    public ItemClickListner itemListItm;
    public Button feedback;

    public MyHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMyhistoryorderId = (TextView) itemView.findViewById(R.id.myHistoryorder_id);
        txtMyhistoryorderPrice = (TextView) itemView.findViewById(R.id.myHistoryorder_total_price);
        txtMyhistoryorderDate = (TextView) itemView.findViewById(R.id.myHistoryorder_date);
        txtMyhistoryorderstatus = (TextView) itemView.findViewById(R.id.myHistoryorder_status);
        feedback = (Button) itemView.findViewById(R.id.myhistoryorder_feedback);

    }

    @Override
    public void onClick(View v) {
        itemListItm.onClick(v, getAdapterPosition(), false);
    }

    public void setItemListItm(ItemClickListner itemListItm) {
        this.itemListItm = itemListItm;
    }
}
