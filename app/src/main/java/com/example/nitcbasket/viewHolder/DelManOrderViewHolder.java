package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class DelManOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView deluserName, delorderAddress, delorderPrice,delorderDate,delorderid;
    public ItemClickListner delitemList;
    public Button verify_user,refuse;

    public DelManOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        deluserName = (TextView) itemView.findViewById(R.id.del_dash_user_name);
        delorderPrice = (TextView) itemView.findViewById(R.id.del_dash_total_price);
        delorderDate = (TextView) itemView.findViewById(R.id.del_dash_date_time);
        delorderid = (TextView) itemView.findViewById(R.id.del_dash_order_id);
        delorderAddress = (TextView) itemView.findViewById(R.id.del_dash_address);

        verify_user = (Button) itemView.findViewById(R.id.verify_user);
        refuse = (Button) itemView.findViewById(R.id.say_no);
    }

    @Override
    public void onClick(View v) {
        delitemList.onClick(v,getAdapterPosition(),false);
    }

    public void setDelitemList(ItemClickListner delitemList) {
        this.delitemList = delitemList;
    }
}
