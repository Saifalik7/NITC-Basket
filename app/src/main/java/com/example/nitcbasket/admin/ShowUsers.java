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

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.viewHolder.ShowDelManViewHolder;
import com.example.nitcbasket.viewHolder.ShowUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowUsers extends AppCompatActivity {

    private RecyclerView showusers_Recy;
    private RecyclerView.LayoutManager users_manager;
    private EditText search_user;
    private Button search_button;
    private String search_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        search_user = (EditText) findViewById(R.id.search_user);
        search_button = (Button) findViewById(R.id.search_button);

        showusers_Recy = (RecyclerView)findViewById(R.id.show_users_mans_recy);
        showusers_Recy.setHasFixedSize(true);
        users_manager = new LinearLayoutManager(this);
        showusers_Recy.setLayoutManager(users_manager);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_in = search_user.getText().toString();
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference show_user = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(show_user.orderByChild("name").startAt(search_in),User.class).build();

        FirebaseRecyclerAdapter<User, ShowUsersViewHolder> adapter = new FirebaseRecyclerAdapter<User, ShowUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShowUsersViewHolder showUsersViewHolder, int i, @NonNull User user) {

                showUsersViewHolder.users_name.setText(user.getName());
                showUsersViewHolder.users_email.setText(user.getEmail());
                showUsersViewHolder.users_status.setText(user.getCurrStatus());

                showUsersViewHolder.block_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(user.getCurrStatus().equals("Active") == true)
                        {
                            show_user.child(user.getUid()).child("currStatus").setValue("Blocked");
                            //showUsersViewHolder.block_user.setText("UnBlock");
                            //startActivity(new Intent(ShowUsers.this,ShowUsers.class));
                            //finish();
                            showUsersViewHolder.block_user.setText("Block");
                            return;
                        }
                        else
                        {
                            show_user.child(user.getUid()).child("currStatus").setValue("Active");
                            show_user.child(user.getUid()).child("refuseCount").setValue("0");
                            //showUsersViewHolder.block_user.setText("Block");
                            //startActivity(new Intent(ShowUsers.this,ShowUsers.class));
                            //finish();
                            showUsersViewHolder.block_user.setText("UnBlock");
                            return ;
                        }

                    }
                });
            }

            @NonNull
            @Override
            public ShowUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_users_layout,parent,false);
                ShowUsersViewHolder holder = new ShowUsersViewHolder(view);
                return holder;
            }
        };
        showusers_Recy.setAdapter(adapter);
        adapter.startListening();
    }
}







