package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txthistoryorderId, txthistoryorderPrice,txthistoryorderDate,txthistoryorderstatus;
    public ItemClickListner hisitemListItm;


    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txthistoryorderId = (TextView) itemView.findViewById(R.id.Historyorder_id);
        txthistoryorderPrice = (TextView) itemView.findViewById(R.id.Historyorder_total_price);
        txthistoryorderDate = (TextView) itemView.findViewById(R.id.Historyorder_date);
        txthistoryorderstatus = (TextView) itemView.findViewById(R.id.Historyorder_status);

    }

    @Override
    public void onClick(View v) {
        hisitemListItm.onClick(v, getAdapterPosition(), false);
    }

    public void setHisitemListItm(ItemClickListner hisitemListItm) {
        this.hisitemListItm = hisitemListItm;
    }
}
