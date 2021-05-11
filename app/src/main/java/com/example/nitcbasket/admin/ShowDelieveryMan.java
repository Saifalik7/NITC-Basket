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
import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.viewHolder.OrderViewHolder;
import com.example.nitcbasket.viewHolder.ShowDelManViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowDelieveryMan extends AppCompatActivity {

    private RecyclerView showdel_Recy;
    private RecyclerView.LayoutManager show_manager;
    private String UID = "",OID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_delievery_man);

        UID = getIntent().getStringExtra("UID");
        OID = getIntent().getStringExtra("OID");

        showdel_Recy = (RecyclerView)findViewById(R.id.show_del_mans_recy);
        showdel_Recy.setHasFixedSize(true);
        show_manager = new LinearLayoutManager(this);
        showdel_Recy.setLayoutManager(show_manager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference del_ref = FirebaseDatabase.getInstance().getReference("DeliveryMans");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(del_ref,User.class).build();

        FirebaseRecyclerAdapter<User,ShowDelManViewHolder> adapter = new
                FirebaseRecyclerAdapter<User, ShowDelManViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ShowDelManViewHolder showDelManViewHolder, int i, @NonNull User user) {

                        showDelManViewHolder.del_name.setText("Name: " + user.getName());
                        showDelManViewHolder.del_id.setText("D_id: " + user.getUid());

                        showDelManViewHolder.assign_to_del.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final DatabaseReference store_userref = FirebaseDatabase.getInstance()
                                        .getReference().child("OrderToDeliver").child(user.getUid())
                                        .child("OrderForMe");

                                final DatabaseReference currOref = FirebaseDatabase.getInstance()
                                        .getReference().child("Orders");

                                currOref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot snap : snapshot.getChildren())
                                        {
                                            OrderItem orders = snap.getValue(OrderItem.class);
                                            if(orders.getOid().equals(OID))
                                            {
                                                HashMap<String, Object> delMap = new HashMap<>();
                                                delMap.put("Date",orders.getDate());
                                                delMap.put("Oid",orders.getOid());
                                                delMap.put("Time",orders.getTime());
                                                delMap.put("address",orders.getAddress());
                                                delMap.put("contact",orders.getContact());
                                                delMap.put("name",orders.getName());
                                                delMap.put("status",orders.getStatus());
                                                delMap.put("totalAmount",orders.getTotalAmount());
                                                delMap.put("userId",orders.getUserId());

                                                store_userref.child(OID).setValue(delMap);

                                                Toast.makeText(getApplicationContext(),
                                                        "Order is Assigned to" + user.getName(),
                                                        Toast.LENGTH_LONG)
                                                        .show();

                                                Intent i = new Intent(ShowDelieveryMan.this,Orders.class);
                                                startActivity(i);

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ShowDelManViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delman_show_layout,parent,false);
                        ShowDelManViewHolder holder = new ShowDelManViewHolder(view);
                        return holder;
                    }
                };
        showdel_Recy.setAdapter(adapter);
        adapter.startListening();
    }
}





