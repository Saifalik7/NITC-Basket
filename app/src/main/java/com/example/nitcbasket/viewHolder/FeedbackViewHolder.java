package com.example.nitcbasket.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcbasket.R;
import com.example.nitcbasket.store.ItemClickListner;

public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView feedback_txt, feedback_name, feedback_oid;
    public ItemClickListner feedbackitem;

    public FeedbackViewHolder(@NonNull View itemView) {
        super(itemView);

        feedback_name = (TextView) itemView.findViewById(R.id.feedback_name);
        feedback_oid = (TextView) itemView.findViewById(R.id.feedback_oid);
        feedback_txt = (TextView) itemView.findViewById(R.id.feedback_write);
    }

    @Override
    public void onClick(View v) {
       feedbackitem.onClick(v, getAdapterPosition(), false);
    }

    public void setFeedbackitem(ItemClickListner feedbackitem) {
        this.feedbackitem = feedbackitem;
    }
}
