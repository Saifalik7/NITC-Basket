package com.example.nitcbasket.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nitcbasket.R;

public class AdminPanel extends AppCompatActivity {

    private ImageView vegetables, fruits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        vegetables = (ImageView) findViewById(R.id.imgvegi);
        fruits = (ImageView) findViewById(R.id.imgfruit);

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminPanel.this, AdminAddProduct.class);
                i.putExtra("category","Vegetables");
                startActivity(i);

            }
        });
        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPanel.this,AdminAddProduct.class);
                i.putExtra("category","Fruits");
                startActivity(i);
            }
        });
    }
}