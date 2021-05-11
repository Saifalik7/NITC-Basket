package com.example.nitcbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.Product;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.user.Homepage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    private String productId = "";
    public String available = "";
    private TextView pro_name,pro_quantity, pro_price,prod_count;
    private Button plus,minus, addtocart;
    private ImageView pro_img;
    private int quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("Pid");

        pro_img = (ImageView) findViewById(R.id.product_detail_image);
        pro_name = (TextView) findViewById(R.id.product_name);
        pro_quantity = (TextView) findViewById(R.id.product_Quantity);
        pro_price = (TextView) findViewById(R.id.product_price);
        prod_count = (TextView) findViewById(R.id.prod_count);

        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        addtocart = (Button) findViewById(R.id.add_to_cart);

        quantity = 1;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* final int[] avail = new int[1];
                DatabaseReference db = (DatabaseReference) FirebaseDatabase.getInstance().getReference()
                        .child("Products").child(productId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    Product products = snapshot.getValue(Product.class);
                                    avail[0] =(Integer.valueOf(products.getQuantity()));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });*/

                if((Integer.valueOf(available)) <= quantity)
                {
                    Toast.makeText(getApplicationContext(),"You can not select more than available quantity",Toast.LENGTH_LONG).show();
                }
                else {
                    quantity = quantity + 1;
                    prod_count.setText("" + quantity);
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity <= 1)
                {
                    Toast.makeText(getApplicationContext(),"quantity can not less than 1",Toast.LENGTH_LONG).show();
                }else {
                    quantity = quantity - 1;
                    prod_count.setText("" + quantity);
                }
            }
        });

        getProductDetails(productId);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfoToCart();
            }
        });

    }
    private void addInfoToCart()
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        final DatabaseReference cartref = FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("Pid", productId);
        cartMap.put("Pname", pro_name.getText().toString());
        cartMap.put("Price", pro_price.getText().toString());
        cartMap.put("Quantity", prod_count.getText().toString());
        cartMap.put("Date", saveCurrentDate);
        cartMap.put("Time", saveCurrentTime);

        cartref.child("User View").child(Prevalent.currentUser.getUid()).child("Products").child(productId)
                .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    cartref.child("Admin View").child(Prevalent.currentUser.getUid()).child("Products").child(productId)
                            .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_LONG).show();

                                Intent i = new Intent(ProductDetails.this, Homepage.class);
                                startActivity(i);
                            }

                        }
                    });
                }
            }
        });

    }

    private void getProductDetails(String productId)
    {
        DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("Products");

        prodref.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Product products = snapshot.getValue(Product.class);

                    pro_name.setText(products.getPname());
                    pro_quantity.setText(products.getQuantity());
                    available = products.getQuantity();
                    pro_price.setText(products.getPrice() );
                    Picasso.get().load(products.getImage()).into(pro_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }









}