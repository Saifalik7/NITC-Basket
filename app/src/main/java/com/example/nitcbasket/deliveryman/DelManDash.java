package com.example.nitcbasket.deliveryman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Login;
import com.example.nitcbasket.Model.OrderItem;
import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.admin.AddDeliveryMan;
import com.example.nitcbasket.admin.AdminDash;
import com.example.nitcbasket.admin.AdminPanel;
import com.example.nitcbasket.admin.History;
import com.example.nitcbasket.admin.Orders;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.prevalent.PrevalentDel;
import com.example.nitcbasket.viewHolder.DelManOrderViewHolder;
import com.example.nitcbasket.viewHolder.ProductViewHolder;
import com.example.nitcbasket.viewHolder.ShowDelManViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DelManDash extends AppCompatActivity {

    private NavigationView delivery_nav;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout delivery_drawerLayout;
    private RecyclerView del_dash_rcv;
    private RecyclerView.LayoutManager del_dash_manager;
    private FirebaseAuth mAuth;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_man_dash);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        delivery_nav=(NavigationView)findViewById(R.id.delivery_nav);
        delivery_drawerLayout=(DrawerLayout)findViewById(R.id.delivery_drawer);


        toggle=new ActionBarDrawerToggle(this,delivery_drawerLayout,toolbar,R.string.open,R.string.close);
        delivery_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        delivery_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.delivery_nav_home :

                        delivery_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.delivery_nav_orders :
                        Intent in = new Intent(DelManDash.this, DelManDash.class);
                        startActivity(in);
                        delivery_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.delivery_nav_History :
                        Intent inte = new Intent(DelManDash.this, Del_man_history.class);
                        startActivity(inte);
                        delivery_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.delivery_nav_logout :
                        Intent inten = new Intent(DelManDash.this, Login.class);
                        startActivity(inten);
                        delivery_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        View headerView = delivery_nav.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.user_name);
        userNameText.setText(PrevalentDel.currentDelMan.getName());

        del_dash_rcv = (RecyclerView)findViewById(R.id.del_dash_rcv);
        del_dash_rcv.setHasFixedSize(true);
        del_dash_manager = new LinearLayoutManager(this);
        del_dash_rcv.setLayoutManager(del_dash_manager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference delref = FirebaseDatabase.getInstance().getReference().child("OrderToDeliver")
                .child(PrevalentDel.currentDelMan.getUid()).child("OrderForMe");

        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(delref,OrderItem.class).build();

        FirebaseRecyclerAdapter<OrderItem, DelManOrderViewHolder> adapter = new
                FirebaseRecyclerAdapter<OrderItem, DelManOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DelManOrderViewHolder delManOrderViewHolder, int i, @NonNull OrderItem orderItem) {
                        delManOrderViewHolder.deluserName.setText("Name: " + orderItem.getName());
                        delManOrderViewHolder.delorderAddress.setText("Address: " + orderItem.getAddress());
                        delManOrderViewHolder.delorderDate.setText("Date: " + orderItem.getDate());
                        delManOrderViewHolder.delorderid.setText("Oid: " + orderItem.getOid());
                        delManOrderViewHolder.delorderPrice.setText("Price: " + orderItem.getTotalAmount());

                        //verify user
                        delManOrderViewHolder.verify_user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final DatabaseReference ver_ref = FirebaseDatabase.getInstance().getReference().child("History");

                              final DatabaseReference myver_ref = FirebaseDatabase.getInstance().getReference().child("UserHistory")
                                        .child(orderItem.getUserId()).child("myHistory");

                              final DatabaseReference del_man_his = FirebaseDatabase.getInstance().getReference()
                                      .child("DeliveryManHistory").child(PrevalentDel.currentDelMan.getUid())
                                      .child("Delivered");

                              final HashMap<String, Object> histMap = new HashMap<>();
                                histMap.put("Oid", orderItem.getOid());
                                histMap.put("userId", orderItem.getUserId());
                                histMap.put("totalAmount", orderItem.getTotalAmount());
                                histMap.put("name", orderItem.getName());
                                histMap.put("contact", orderItem.getContact());
                                histMap.put("address", orderItem.getAddress());
                                histMap.put("Date", orderItem.getDate());
                                histMap.put("Time", orderItem.getTime());
                                histMap.put("status", "Delivered");

                                ver_ref.child(orderItem.getOid()).setValue(histMap);
                                myver_ref.child(orderItem.getOid()).setValue(histMap);
                                del_man_his.child(orderItem.getOid()).setValue(histMap);



                                //remove from users orders
                                final DatabaseReference curr_his_ref = FirebaseDatabase.getInstance().getReference().child("UserOrders")
                                        .child(orderItem.getUserId()).child("myOrders");
                                curr_his_ref.child(orderItem.getOid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(),"User verified",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                //end

                                //remove admin orders
                                 //DatabaseReference admin_curr_ref = FirebaseDatabase.getInstance().getReference().child("Orders");
                                // admin_curr_ref.child(orderItem.getOid()).removeValue();

                                //end

                                //remove del mans orders
                                final DatabaseReference del_curr_ref = FirebaseDatabase.getInstance().getReference().child("OrderToDeliver")
                                        .child(PrevalentDel.currentDelMan.getUid()).child("OrderForMe");
                                del_curr_ref.child(orderItem.getOid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {

                                        }
                                    }
                                });
                                //end




                            }
                        });

                        //user refuse order
                        delManOrderViewHolder.refuse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String myid = orderItem.getUserId();
                                DatabaseReference curr_db = FirebaseDatabase.getInstance().getReference().child("Users");

                                curr_db.child(myid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()) {
                                            User ud = snapshot.getValue(User.class);

                                             count = (Integer.valueOf(ud.getRefuseCount()));
                                            //count = count + 1;

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                String k = String.valueOf(count + 1);
                                curr_db.child(myid).child("refuseCount").setValue(k);
                                if((count + 1) > 1)
                                {
                                    curr_db.child(myid).child("currStatus").setValue("Blocked");

                                }

                                final DatabaseReference fhisRef = FirebaseDatabase.getInstance().getReference().child("History");

                                final DatabaseReference fmyhisRef = FirebaseDatabase.getInstance().getReference().child("UserHistory")
                                        .child(orderItem.getUserId()).child("myHistory");

                                final DatabaseReference fdel_man_his = FirebaseDatabase.getInstance().getReference()
                                        .child("DeliveryManHistory").child(PrevalentDel.currentDelMan.getUid())
                                        .child("Delivered");

                                final HashMap<String, Object> fhistoryMap = new HashMap<>();
                                fhistoryMap.put("Oid", orderItem.getOid());
                                fhistoryMap.put("userId", orderItem.getUserId());
                                fhistoryMap.put("totalAmount", orderItem.getTotalAmount());
                                fhistoryMap.put("name", orderItem.getName());
                                fhistoryMap.put("contact", orderItem.getContact());
                                fhistoryMap.put("address", orderItem.getAddress());
                                fhistoryMap.put("Date", orderItem.getDate());
                                fhistoryMap.put("Time", orderItem.getTime());
                                fhistoryMap.put("status", "Refused");

                                fhisRef.child(orderItem.getOid()).setValue(fhistoryMap);
                                fmyhisRef.child(orderItem.getOid()).setValue(fhistoryMap);
                                fdel_man_his.child(orderItem.getOid()).setValue(fhistoryMap);



                                //remove from users orders
                                final DatabaseReference fmyorderRef = FirebaseDatabase.getInstance().getReference().child("UserOrders")
                                        .child(orderItem.getUserId()).child("myOrders");
                                fmyorderRef.child(orderItem.getOid()).removeValue();
                                //end

                                //remove admin orders
                               // DatabaseReference fadminref = FirebaseDatabase.getInstance().getReference().child("Orders");
                                //fadminref.child(orderItem.getOid()).removeValue();

                                //end

                                //remove del mans orders
                                final DatabaseReference fdelref = FirebaseDatabase.getInstance().getReference().child("OrderToDeliver")
                                        .child(PrevalentDel.currentDelMan.getUid()).child("OrderForMe");
                                fdelref.child(orderItem.getOid()).removeValue();
                                //end


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DelManOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_orders_to_del_layout,parent,false);
                        DelManOrderViewHolder holder = new DelManOrderViewHolder(view);
                        return holder;
                    }
                };

        del_dash_rcv.setAdapter(adapter);
        adapter.startListening();

    }
}












