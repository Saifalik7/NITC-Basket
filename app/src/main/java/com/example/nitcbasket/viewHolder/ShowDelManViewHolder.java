package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class ShowDelManViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView del_name,del_id;
    public Button assign_to_del;
    public ItemClickListner clickItem;

    public ShowDelManViewHolder(@NonNull View itemView) {
        super(itemView);

        del_name = (TextView) itemView.findViewById(R.id.del_man_name);
        del_id = (TextView) itemView.findViewById(R.id.del_man_id);
        assign_to_del = (Button) itemView.findViewById(R.id.del_man_assign);
    }

    @Override
    public void onClick(View v) {
        clickItem.onClick(v,getAdapterPosition(),false);
    }

    public void setClickItem(ItemClickListner clickItem) {
        this.clickItem = clickItem;
    }
}
