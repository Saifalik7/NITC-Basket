package com.example.nitcbasket.admin;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nitcbasket.Login;
import com.example.nitcbasket.Model.Product;
import com.example.nitcbasket.R;
import com.example.nitcbasket.user.Settings;
import com.example.nitcbasket.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminDash extends AppCompatActivity {

    private NavigationView admin_nav;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout admin_drawerLayout;
    private RecyclerView admin_rcv;
    private DatabaseReference productref;
    private RecyclerView.LayoutManager admin_manager;
    private FloatingActionButton admin_fbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);


        admin_fbutton = (FloatingActionButton)findViewById(R.id.admin_add);
        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        admin_nav=(NavigationView)findViewById(R.id.admin_nav);
        admin_drawerLayout=(DrawerLayout)findViewById(R.id.admin_drawer);


        toggle=new ActionBarDrawerToggle(this,admin_drawerLayout,toolbar,R.string.open,R.string.close);
        admin_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        admin_fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDash.this, AdminPanel.class);
                startActivity(i);
            }
        });

        admin_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.admin_nav_home :

                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.admin_nav_add :
                        Intent i = new Intent(AdminDash.this, AdminPanel.class);
                        startActivity(i);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.admin_nav_orders :
                        Intent in = new Intent(AdminDash.this, Orders.class);
                        startActivity(in);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_History :
                        Intent inte = new Intent(AdminDash.this, History.class);
                        startActivity(inte);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_search :
                        Intent intess = new Intent(AdminDash.this, AdminSearchProduct.class);
                        startActivity(intess);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_users :
                        Intent intes = new Intent(AdminDash.this, ShowUsers.class);
                        startActivity(intes);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_feedback :
                        Intent intest = new Intent(AdminDash.this, Users_feedback.class);
                        startActivity(intest);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_addDel :
                        Intent intent = new Intent(AdminDash.this, AddDeliveryMan.class);
                        startActivity(intent);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_nav_logout :
                        Intent inten = new Intent(AdminDash.this, Login.class);
                        startActivity(inten);
                        admin_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        admin_rcv = (RecyclerView)findViewById(R.id.admin_rcv);
        admin_rcv.setHasFixedSize(true);
        admin_manager = new LinearLayoutManager(this);
        admin_rcv.setLayoutManager(admin_manager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new
                FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productref, Product.class).build();

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
                                Intent i = new Intent(AdminDash.this, AdminProductUpdate.class);
                                i.putExtra("Pid", product.getPid());
                                startActivity(i);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        admin_rcv.setAdapter(adapter);
        adapter.startListening();
    }
}