package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;
import com.example.nitcbasket.store.itemListner;

public class ShowUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView users_name, users_email, users_status;
    public Button block_user;
    public ImageView user_img;
    public itemListner itemListclik;

    public ShowUsersViewHolder(@NonNull View itemView) {
        super(itemView);

        users_name = (TextView) itemView.findViewById(R.id.show_users_name);
        users_email = (TextView) itemView.findViewById(R.id.show_users_email);
        users_status = (TextView) itemView.findViewById(R.id.show_users_status);
        block_user = (Button) itemView.findViewById(R.id.edit_block_user);

    }

    @Override
    public void onClick(View v) {
        itemListclik.onClick(v,getAdapterPosition(),false);

    }

    public void setItemListclik(itemListner itemListclik) {
        this.itemListclik = itemListclik;
    }
}
