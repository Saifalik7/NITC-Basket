package com.example.nitcbasket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nitcbasket.Model.OrderItem;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.viewHolder.HistoryViewHolder;
import com.example.nitcbasket.viewHolder.MyHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {


    private RecyclerView HistoryRecy;
    private RecyclerView.LayoutManager Historymanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryRecy = (RecyclerView)findViewById(R.id.history_recy);
        HistoryRecy.setHasFixedSize(true);
        Historymanager = new LinearLayoutManager(this);
        HistoryRecy.setLayoutManager(Historymanager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference histRef = FirebaseDatabase.getInstance().getReference().child("History");

        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(histRef,OrderItem.class).build();

        FirebaseRecyclerAdapter<OrderItem, HistoryViewHolder> adapter = new FirebaseRecyclerAdapter<OrderItem, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull OrderItem orderItem) {

                historyViewHolder.txthistoryorderDate.setText("Date: " + orderItem.getDate());
                historyViewHolder.txthistoryorderId.setText("Oid: " + orderItem.getOid());
                historyViewHolder.txthistoryorderstatus.setText("status: " + orderItem.getStatus());
                historyViewHolder.txthistoryorderPrice.setText("Price: " + orderItem.getTotalAmount());
            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout,parent,false);
                HistoryViewHolder holder = new HistoryViewHolder(view);
                return holder;
            }
        };


        HistoryRecy.setAdapter(adapter);
        adapter.startListening();

    }
}