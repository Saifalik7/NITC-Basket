package com.example.nitcbasket.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.CartItem;
import com.example.nitcbasket.ProductDetails;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {

    private RecyclerView recy;
    private RecyclerView.LayoutManager manager;
    private Button next,calculate;
    private TextView total_price;
    private String productId = "";
    private int overalTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productId = getIntent().getStringExtra("Pid");

        recy = (RecyclerView)findViewById(R.id.cart_recy);
        recy.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recy.setLayoutManager(manager);


        next = (Button) findViewById(R.id.next);
        calculate = (Button) findViewById(R.id.calculate);
        total_price = (TextView) findViewById(R.id.total_price);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_price.setText("Total Price = " + String.valueOf(overalTotalPrice));

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                      Intent i = new Intent(Cart.this, ConfirmOrder.class);
                      i.putExtra("Total", String.valueOf(overalTotalPrice));
                      startActivity(i);
                      finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Cart");

        FirebaseRecyclerOptions<CartItem> options = new FirebaseRecyclerOptions.Builder<CartItem>()
                .setQuery(dbref.child("User View").child(Prevalent.currentUser.getUid()).child("Products"),CartItem.class)
                .build();

        FirebaseRecyclerAdapter<CartItem, CartViewHolder> adapter = new
                FirebaseRecyclerAdapter<CartItem, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull CartItem cartItem) {

                        cartViewHolder.txtProductName.setText(cartItem.getPname());
                        cartViewHolder.txtProductQuantity.setText(cartItem.getQuantity() + "Kg");
                        cartViewHolder.txtProductPrice.setText(cartItem.getPrice() + "₹ per Kg");

                        int oneTypeProductPrice = ((Integer.valueOf(cartItem.getPrice())) * (Integer.valueOf(cartItem.getQuantity())));
                        overalTotalPrice = overalTotalPrice + oneTypeProductPrice;

                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                {
                                    "Edit", "Remove"

                                };
                                //
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Cart.this);
                                dialogBuilder.setTitle("Cart Options");

                                dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which == 0)
                                        {
                                            Intent i = new Intent(Cart.this, ProductDetails.class);
                                            i.putExtra("Pid",cartItem.getPid());
                                            startActivity(i);
                                        }
                                        if(which == 1)
                                        {
                                            dbref.child("User View").child(Prevalent.currentUser.getUid()).child("Products").
                                                    child(cartItem.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(),"Item Removed Successfully",Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(Cart.this,Cart.class));
                                                        }
                                                }
                                            });
                                        }
                                    }
                                });
                                dialogBuilder.show();
                                //
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;

                    }
                };
           recy.setAdapter(adapter);
           adapter.startListening();
    }
}











