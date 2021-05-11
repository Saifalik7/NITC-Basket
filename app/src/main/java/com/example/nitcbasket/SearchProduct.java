package com.example.nitcbasket;

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
import com.example.nitcbasket.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProduct extends AppCompatActivity {


    private EditText editsearch;
    private Button btnsearch;
    private RecyclerView search_recy;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        editsearch = (EditText)findViewById(R.id.search_prod);
        btnsearch = (Button)findViewById(R.id.search_btn);

        search_recy = (RecyclerView) findViewById(R.id.search_recy);
        search_recy.setLayoutManager(new LinearLayoutManager(SearchProduct.this));

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchInput = editsearch.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference seachref = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(seachref.orderByChild("Pname").startAt(searchInput),Product.class).build();

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
                                Intent i = new Intent(SearchProduct.this,ProductDetails.class);
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
        search_recy.setAdapter(adapter);
        adapter.startListening();
    }
}