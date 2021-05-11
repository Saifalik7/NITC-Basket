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
import android.widget.Button;
import android.widget.EditText;

import com.example.nitcbasket.Model.Product;
import com.example.nitcbasket.ProductDetails;
import com.example.nitcbasket.R;
import com.example.nitcbasket.SearchProduct;
import com.example.nitcbasket.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminSearchProduct extends AppCompatActivity {

    private EditText admin_editsearch;
    private Button admin_btnsearch;
    private RecyclerView admin_search_recy;
    private String admin_searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_product);

        admin_editsearch = (EditText)findViewById(R.id.admin_search_prod);
        admin_btnsearch = (Button)findViewById(R.id.admin_search_btn);

        admin_search_recy = (RecyclerView) findViewById(R.id.admin_search_recy);
        admin_search_recy.setLayoutManager(new LinearLayoutManager(AdminSearchProduct.this));

        admin_btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                admin_searchInput = admin_editsearch.getText().toString();
                onStart();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference adminseachref = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(adminseachref.orderByChild("Pname").startAt(admin_searchInput),Product.class).build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new
                FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Product product) {

                        productViewHolder.prodName.setText(product.getPname());
                        productViewHolder.prodPrice.setText("Price = " + product.getPrice() + "₹ per Kg");
                        productViewHolder.prodQuantity.setText("Quantity = " + product.getQuantity() + " Kg");
                        Picasso.get().load(product.getImage()).into(productViewHolder.prodImg);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AdminSearchProduct.this, AdminProductUpdate.class);
                                i.putExtra("Pid",product.getPid());
                                startActivity(i);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        admin_search_recy.setAdapter(adapter);
        adapter.startListening();
    }
}