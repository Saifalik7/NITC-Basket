package com.example.nitcbasket.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nitcbasket.Login;
import com.example.nitcbasket.Model.Product;
import com.example.nitcbasket.ProductDetails;
import com.example.nitcbasket.R;
import com.example.nitcbasket.SearchProduct;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity {

    private NavigationView nav;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private DatabaseReference productref;
    private RecyclerView rcv;
    private RecyclerView.LayoutManager manager;
    private FloatingActionButton fbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        fbutton = (FloatingActionButton)findViewById(R.id.add);

        productref = FirebaseDatabase.getInstance().getReference().child("Products");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.nav_id);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);


        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

         fbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(Homepage.this, Cart.class);
                 startActivity(i);
             }
         });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home :

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_cart :
                        Intent i = new Intent(Homepage.this, Cart.class);
                        startActivity(i);
                        //finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_orders :
                        Intent in = new Intent(Homepage.this, MyOrders.class);
                        startActivity(in);
                       // finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_History :
                        Intent uIntent = new Intent(Homepage.this, MyHistory.class);
                        startActivity(uIntent);
                       // finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_search :
                        Intent intenti = new Intent(Homepage.this, SearchProduct.class);
                        startActivity(intenti);
                       // finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_setting :
                        Intent inte = new Intent(Homepage.this, Settings.class);
                        startActivity(inte);
                        //finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logout :
                        Intent inten = new Intent(Homepage.this, Login.class);
                        startActivity(inten);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        //get user name
        View headerView = nav.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.user_name);
        CircleImageView imgView =  headerView.findViewById(R.id.profile_image);

          userNameText.setText(Prevalent.currentUser.getName());
          Picasso.get().load(Prevalent.currentUser.getImage()).placeholder(R.drawable.ic_baseline_account_circle_24).into(imgView);

        rcv = (RecyclerView)findViewById(R.id.rcv);
        rcv.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        rcv.setLayoutManager(manager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options= new
                FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productref,Product.class).build();

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
                                Intent i = new Intent(Homepage.this, ProductDetails.class);
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

        rcv.setAdapter(adapter);
        adapter.startListening();
    }
}









