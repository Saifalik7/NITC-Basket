package com.example.nitcbasket.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
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

public class ConfirmOrder extends AppCompatActivity {

    private TextView shipment_detail;
    private EditText shipment_name,shipment_contact,shipment_address;
    private Button place_order;
    private String totalAmount = "";
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalAmount = getIntent().getStringExtra("Total");

        load = new ProgressDialog(this);

        shipment_detail = (TextView)findViewById(R.id.shipment_details);
        shipment_name = (EditText)findViewById(R.id.shipment_name);
        shipment_contact = (EditText)findViewById(R.id.shipment_contact);
        shipment_address = (EditText)findViewById(R.id.shipment_address);
        place_order = (Button) findViewById(R.id.confirm_order);

        //getuserdetails

        DatabaseReference pro_dref = FirebaseDatabase.getInstance().getReference().child("Users");

        pro_dref.child(Prevalent.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    User ud = snapshot.getValue(User.class);

                    shipment_name.setText(ud.getName());
                    shipment_contact.setText(ud.getContact());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference blockUserref = FirebaseDatabase.getInstance().getReference().child("Users");
                blockUserref.child(Prevalent.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            User ud = snapshot.getValue(User.class);
                            if (ud.getCurrStatus().equals("Blocked")) {
                                Toast.makeText(ConfirmOrder.this, "You are Blocked", Toast.LENGTH_LONG).show();
                                return;
                            } else {

                                load.setMessage("Sending Order");
                                load.setCanceledOnTouchOutside(false);
                                load.show();
                                check();
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

    private void check()
    {
        if (TextUtils.isEmpty(shipment_name.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_LONG).show();
            load.dismiss();
            return;
        }
        else  if (TextUtils.isEmpty(shipment_contact.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Enter contact",Toast.LENGTH_LONG).show();
            load.dismiss();
            return;
        }
        else  if (TextUtils.isEmpty(shipment_address.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_LONG).show();
            load.dismiss();
            return;
        }
        else
        {
            confirmOrder();
        }
    }

    private void confirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders");
        final DatabaseReference userorderef = FirebaseDatabase.getInstance().getReference()
                .child("UserOrders").child(Prevalent.currentUser.getUid()).child("myOrders");

        String oid = orderref.push().getKey();
        final  HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("Oid",oid);
        orderMap.put("userId",Prevalent.currentUser.getUid());
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", shipment_name.getText().toString());
        orderMap.put("contact",shipment_contact.getText().toString());
        orderMap.put("address", shipment_address.getText().toString());
        orderMap.put("Date", saveCurrentDate);
        orderMap.put("Time", saveCurrentTime);
        orderMap.put("status","Not Shipped");

        userorderef.child(oid).setValue(orderMap);
        orderref.child(oid).setValue(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart")
                            .child("User View").child(Prevalent.currentUser.getUid())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                load.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Your Order Placed Successfully",
                                        Toast.LENGTH_LONG)
                                        .show();

                                Intent i = new Intent(ConfirmOrder.this, Homepage.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }
}







