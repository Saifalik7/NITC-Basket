package com.example.nitcbasket.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nitcbasket.Model.OrderItem;
import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.viewHolder.MyHistoryViewHolder;
import com.example.nitcbasket.viewHolder.MyOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyHistory extends AppCompatActivity {

    private RecyclerView myHistoryRecy;
    private RecyclerView.LayoutManager myHistorymanager;
    private EditText dia_name,dia_oid,dia_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        myHistoryRecy = (RecyclerView)findViewById(R.id.myhistory_recy);
        myHistoryRecy.setHasFixedSize(true);
        myHistorymanager = new LinearLayoutManager(this);
        myHistoryRecy.setLayoutManager(myHistorymanager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference histRef = FirebaseDatabase.getInstance().getReference().child("UserHistory")
                .child(Prevalent.currentUser.getUid()).child("myHistory");

        FirebaseRecyclerOptions<OrderItem> options = new FirebaseRecyclerOptions.Builder<OrderItem>()
                .setQuery(histRef,OrderItem.class).build();

        FirebaseRecyclerAdapter<OrderItem, MyHistoryViewHolder> adapter = new
                FirebaseRecyclerAdapter<OrderItem, MyHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyHistoryViewHolder myHistoryViewHolder, int i, @NonNull OrderItem orderItem) {

                        myHistoryViewHolder.txtMyhistoryorderId.setText("Oid: " + orderItem.getOid());
                        myHistoryViewHolder.txtMyhistoryorderPrice.setText("Price: " + orderItem.getTotalAmount() + "₹");
                        myHistoryViewHolder.txtMyhistoryorderstatus.setText("Status: " + orderItem.getStatus());
                        myHistoryViewHolder.txtMyhistoryorderDate.setText("Date: " + orderItem.getDate());

                        myHistoryViewHolder.feedback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                process(orderItem.getOid(), orderItem.getName());
                            }

                        });

                    }

                    @NonNull
                    @Override
                    public MyHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myhistory_layout,parent,false);
                        MyHistoryViewHolder holder = new MyHistoryViewHolder(view);
                        return holder;
                    }
                };
        myHistoryRecy.setAdapter(adapter);
        adapter.startListening();


    }

    public void process(String o_id,String o_name)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.feedback_dialogbox, null);
        dialogBuilder.setView(dialogView);

        dia_name = (EditText) dialogView.findViewById(R.id.dia_name);
        dia_oid = (EditText) dialogView.findViewById(R.id.dia_oid);
        dia_feedback = (EditText) dialogView.findViewById(R.id.dia_feedback);

       //assign value
        dia_name.setText(o_name);
        dia_oid.setText(o_id);
        //end

        dialogBuilder.setTitle("Give Feedback");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                HashMap<String,Object> feedk = new HashMap<>();
                feedk.put("Name", dia_name.getText().toString());
                feedk.put("Oid", dia_oid.getText().toString());
                feedk.put("Feedback", dia_feedback.getText().toString());

                DatabaseReference feedref = FirebaseDatabase.getInstance().getReference().child("Feedback");
                String id = feedref.push().getKey();
                feedref.child(id).setValue(feedk);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }
}