package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class OrderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtuserName, txtorderAddress, txtorderPrice,txtorderDate,txtorderid,txtuserid,txtorderstatus;
    public ItemClickListner itemList;
    public Button assignOrder,remove_order;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtuserName = (TextView) itemView.findViewById(R.id.order_user_name);
        //txtorderQuantity = (TextView) itemView.findViewById(R.id.order_pro_quantity);
        txtorderPrice = (TextView) itemView.findViewById(R.id.order_total_price);
        txtorderDate = (TextView) itemView.findViewById(R.id.order_date_time);
        txtorderid = (TextView) itemView.findViewById(R.id.order_id);
        txtuserid = (TextView) itemView.findViewById(R.id.order_user_id);
        txtorderAddress = (TextView) itemView.findViewById(R.id.order_address);
        txtorderstatus = (TextView) itemView.findViewById(R.id.order_status);
        assignOrder = (Button)itemView.findViewById(R.id.order_assign);
        remove_order = (Button)itemView.findViewById(R.id.delete_order);
    }

    @Override
    public void onClick(View v) {
        itemList.onClick(v,getAdapterPosition(),false);
    }

    public void setItemList(ItemClickListner itemList) {
        this.itemList = itemList;
    }
}
