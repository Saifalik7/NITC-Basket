package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    public ItemClickListner itemListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.cart_pro_name);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.cart_pro_quantity);
        txtProductPrice = (TextView) itemView.findViewById(R.id.cart_pro_price);

    }

    @Override
    public void onClick(View v) {
        itemListner.onClick(v,getAdapterPosition(),false);

    }

    public void setItemListner(ItemClickListner itemListner) {
        this.itemListner = itemListner;
    }
}
