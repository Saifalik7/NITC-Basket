package com.example.nitcbasket.user;

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
import com.example.nitcbasket.viewHolder.MyOrderViewHolder;
import com.example.nitcbasket.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MyOrders extends AppCompatActivity {

    private RecyclerView myorderRecy;
    private RecyclerView.LayoutManager mymanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);


        myorderRecy = (RecyclerView)findViewById(R.id.myorders_recy);
        myorderRecy.setHasFixedSize(true);
        mymanager = new LinearLayoutManager(this);
        myorderRecy.setLayoutManager(mymanager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference conf_or = FirebaseDatabase.getInstance().getReference().child("Orders");

        final DatabaseReference myorderRef = FirebaseDatabase.getInstance().getReference().child("UserOrders")
                .child(Prevalent.currentUser.getUid()).child("myOrders");

        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(myorderRef,OrderItem.class).build();

        FirebaseRecyclerAdapter<OrderItem,MyOrderViewHolder> adapter = new
                FirebaseRecyclerAdapter<OrderItem, MyOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyOrderViewHolder myOrderViewHolder, int i, @NonNull OrderItem orderItem) {

                        myOrderViewHolder.txtMyorderId.setText("Oid: " + orderItem.getOid());
                        myOrderViewHolder.txtMyorderPrice.setText("Price: " + orderItem.getTotalAmount() + "₹");
                        myOrderViewHolder.txtMyorderstatus.setText("Status: " + orderItem.getStatus());
                        myOrderViewHolder.txtMyorderDate.setText("Date: " + orderItem.getDate());
                        //set status of order
                        String a = orderItem.getDate() + orderItem.getTime();
                        Calendar calender = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                        String bd = currentDate.format(calender.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        String bt= currentTime.format(calender.getTime());

                        String b = bd + bt;
                        //System.out.println(b);

                        char ad1 = a.charAt(3);
                        char ad2 = a.charAt(4);
                        String day1 = Character.toString(ad1) + Character.toString(ad2);

                        char bd1 = b.charAt(3);
                        char bd2 = b.charAt(4);
                        String day2 = Character.toString(bd1) + Character.toString(bd2);

                        //hour
                        char ah1 = a.charAt(11);
                        char ah2 = a.charAt(12);
                        String h1 = Character.toString(ah1) + Character.toString(ah2);

                        char bh1 = b.charAt(11);
                        char bh2 = b.charAt(12);
                        String h2 = Character.toString(bh1) + Character.toString(bh2);

                        //minutes
                        char am1 = a.charAt(14);
                        char am2 = a.charAt(15);
                        String m1 = Character.toString(am1) + Character.toString(am2);

                        char bm1 = b.charAt(14);
                        char bm2 = b.charAt(15);
                        String m2 = Character.toString(bm1) + Character.toString(bm2);


                        if((Integer.valueOf(day2) - Integer.valueOf(day1)) > 0)
                        {
                            myOrderViewHolder.myOrderCancel.setText("Confirmed");
                            myorderRef.child(orderItem.getOid()).child("status").setValue("Confirm");
                            conf_or.child(orderItem.getOid()).child("status").setValue("Confirm");
                            myOrderViewHolder.txtMyorderstatus.setText("Status: " + orderItem.getStatus());
                        }
                       else  if ((Integer.valueOf(day2) - Integer.valueOf(day1)) == 0 && (Integer.valueOf(h2) - Integer.valueOf(h1)) >= 0
                                && (Integer.valueOf(m2) - Integer.valueOf(m1)) >= 2) {

                            myOrderViewHolder.myOrderCancel.setText("Confirmed");
                            myorderRef.child(orderItem.getOid()).child("status").setValue("Confirm");
                            conf_or.child(orderItem.getOid()).child("status").setValue("Confirm");
                            myOrderViewHolder.txtMyorderstatus.setText("Status: " + orderItem.getStatus());
                            //myOrderViewHolder.txtMyorderstatus.setText("Status: " + "");
                        }
                        //

                        myOrderViewHolder.myOrderCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //check time constraint
                                String a = orderItem.getDate() + orderItem.getTime();
                                Calendar calender = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                                String bd = currentDate.format(calender.getTime());

                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                 String bt= currentTime.format(calender.getTime());

                                 String b = bd + bt;
                                //System.out.println(b);

                                char ad1 = a.charAt(3);
                                char ad2 = a.charAt(4);
                                String day1 = Character.toString(ad1) + Character.toString(ad2);

                                char bd1 = b.charAt(3);
                                char bd2 = b.charAt(4);
                                String day2 = Character.toString(bd1) + Character.toString(bd2);

                                //hour
                                char ah1 = a.charAt(11);
                                char ah2 = a.charAt(12);
                                String h1 = Character.toString(ah1) + Character.toString(ah2);

                                char bh1 = b.charAt(11);
                                char bh2 = b.charAt(12);
                                String h2 = Character.toString(bh1) + Character.toString(bh2);

                                //minutes
                                char am1 = a.charAt(14);
                                char am2 = a.charAt(15);
                                String m1 = Character.toString(am1) + Character.toString(am2);

                                char bm1 = b.charAt(14);
                                char bm2 = b.charAt(15);
                                String m2 = Character.toString(bm1) + Character.toString(bm2);

                                if((Integer.valueOf(day2) - Integer.valueOf(day1)) > 0) {
                                    Toast.makeText(MyOrders.this, "Your order is confirmed", Toast.LENGTH_SHORT).show();
                                }
                                else if (((Integer.valueOf(day2) - Integer.valueOf(day1)) == 0) && (Integer.valueOf(h2) - Integer.valueOf(h1)) == 0
                                        && (Integer.valueOf(m2) - Integer.valueOf(m1)) >= 2)
                                {

                                    Toast.makeText(getApplicationContext(),"Time up You can not cancel the order",Toast.LENGTH_LONG).show();
                                }

                                else {

                                    //end of time constraint

                                    //extraxt data of order and saved to history
                                    final DatabaseReference hisRef = FirebaseDatabase.getInstance().getReference().child("History");

                                    final DatabaseReference myhisRef = FirebaseDatabase.getInstance().getReference().child("UserHistory")
                                            .child(Prevalent.currentUser.getUid()).child("myHistory");

                                    final HashMap<String, Object> historyMap = new HashMap<>();
                                    historyMap.put("Oid", orderItem.getOid());
                                    historyMap.put("userId", Prevalent.currentUser.getUid());
                                    historyMap.put("totalAmount", orderItem.getTotalAmount());
                                    historyMap.put("name", orderItem.getName());
                                    historyMap.put("contact", orderItem.getContact());
                                    historyMap.put("address", orderItem.getAddress());
                                    historyMap.put("Date", orderItem.getDate());
                                    historyMap.put("Time", orderItem.getTime());
                                    historyMap.put("status", "Cancelled");

                                    hisRef.child(orderItem.getOid()).setValue(historyMap);
                                    myhisRef.child(orderItem.getOid()).setValue(historyMap);

                                    // change status od admin Order View
                                    DatabaseReference adminv = FirebaseDatabase.getInstance().getReference().child("Orders");
                                    adminv.child(orderItem.getOid()).child("status").setValue("Cancelled");
                                    //end here

                                    myorderRef.child(orderItem.getOid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Your Order is cancelled!!",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_layout,parent,false);
                        MyOrderViewHolder holder = new MyOrderViewHolder(view);
                        return holder;
                    }
                };
        myorderRecy.setAdapter(adapter);
        adapter.startListening();
    }
}