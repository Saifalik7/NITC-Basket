package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView prodName, prodPrice, prodQuantity;
    public ImageView prodImg;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        prodName = (TextView) itemView.findViewById(R.id.prod_name);
        prodPrice = (TextView) itemView.findViewById(R.id.prod_price);
        prodQuantity = (TextView) itemView.findViewById(R.id.prod_Qunatity);
        prodImg = (ImageView) itemView.findViewById(R.id.prod_img);
    }

    public void setOnClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }


    @Override
    public void onClick(View view) {

        listner.onClick(view ,getAdapterPosition(),false);
    }
}
