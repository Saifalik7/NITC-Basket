package com.example.nitcbasket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nitcbasket.Model.OrderItem;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Orders extends AppCompatActivity {

    private RecyclerView orderRecy;
    private RecyclerView.LayoutManager Omanager;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        orderId = getIntent().getStringExtra("Oid");

        orderRecy = (RecyclerView)findViewById(R.id.orders_recy);
        orderRecy.setHasFixedSize(true);
        Omanager = new LinearLayoutManager(this);
        orderRecy.setLayoutManager(Omanager);
    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(orderRef.orderByChild("Time"),OrderItem.class).build();

        FirebaseRecyclerAdapter<OrderItem, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<OrderItem, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull OrderItem orderItem) {

                        orderViewHolder.txtuserid.setText("Uid: " + orderItem.getUserId());
                        orderViewHolder.txtorderid.setText("Oid: " + orderItem.getOid());
                        orderViewHolder.txtuserName.setText("Name: " + orderItem.getName());
                        orderViewHolder.txtorderAddress.setText("Address: " + orderItem.getAddress());
                        orderViewHolder.txtorderDate.setText("Date: " + orderItem.getDate());
                        orderViewHolder.txtorderPrice.setText("Total Price : " + orderItem.getTotalAmount() + "₹");
                        orderViewHolder.txtorderstatus.setText("Status: " + orderItem.getStatus());

                        orderViewHolder.remove_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                orderRef.child(orderItem.getOid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),
                                                    " Order Removed!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });

                            }
                        });


                       orderViewHolder.assignOrder.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent i = new Intent(Orders.this, ShowDelieveryMan.class);
                               i.putExtra("UID",orderItem.getUserId());
                               i.putExtra("OID",orderItem.getOid());
                               startActivity(i);

                           }
                       });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordres_layout,parent,false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }
                };
        orderRecy.setAdapter(adapter);
        adapter.startListening();
    }

}













