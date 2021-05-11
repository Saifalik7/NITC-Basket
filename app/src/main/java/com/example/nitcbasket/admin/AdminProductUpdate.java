package com.example.nitcbasket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.Product;
import com.example.nitcbasket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminProductUpdate extends AppCompatActivity {

    private String productId = "";
    private TextView admin_pro_name;
    EditText admin_pro_quantity, admin_pro_price;
    private Button update_product, remove_product;
    private ImageView admin_pro_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update);

        productId = getIntent().getStringExtra("Pid");

        admin_pro_img = (ImageView) findViewById(R.id.admin_product_detail_image);
        admin_pro_name = (TextView) findViewById(R.id.admin_product_name);
        admin_pro_quantity = (EditText) findViewById(R.id.admin_product_Quantity);
        admin_pro_price = (EditText) findViewById(R.id.admin_product_price);

        update_product = (Button) findViewById(R.id.update_product);
        remove_product = (Button) findViewById(R.id.remove_product);

        getProductDetails(productId);

        remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Products").child(productId).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Item Removed Successfully",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(AdminProductUpdate.this, AdminDash.class));
                                }
                            }
                        });
            }
        });

        update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(admin_pro_quantity.getText().toString())) {
                   admin_pro_quantity.setError("please enter quantity");
                   return;
                }
                else  if (TextUtils.isEmpty(admin_pro_price.getText().toString())) {
                   admin_pro_price.setError("please enter price");
                   return;
                }
                    else
                        updateProductInfo();

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

                    admin_pro_name.setText(products.getPname());
                    admin_pro_quantity.setText(products.getQuantity());
                    admin_pro_price.setText(products.getPrice() );
                    Picasso.get().load(products.getImage()).into(admin_pro_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProductInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference().child("Products");

        HashMap<String, Object> updatePro = new HashMap<>();
        updatePro.put("Price", admin_pro_price.getText().toString());
        updatePro.put("Quantity", admin_pro_quantity.getText().toString());

        ref.child(productId).updateChildren(updatePro);
        startActivity(new Intent(AdminProductUpdate.this,AdminDash.class));
        finish();
    }
}