package com.example.nitcbasket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nitcbasket.Model.Feedback;
import com.example.nitcbasket.R;
import com.example.nitcbasket.viewHolder.FeedbackViewHolder;
import com.example.nitcbasket.viewHolder.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Users_feedback extends AppCompatActivity {


    private RecyclerView feedbackRecy;
    private RecyclerView.LayoutManager feedbackmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feedback);

        feedbackRecy = (RecyclerView)findViewById(R.id.feedback_recy);
        feedbackRecy.setHasFixedSize(true);
        feedbackmanager = new LinearLayoutManager(this);
        feedbackRecy.setLayoutManager(feedbackmanager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference feed_ref = FirebaseDatabase.getInstance().getReference().child("Feedback");

        FirebaseRecyclerOptions<Feedback> options = new FirebaseRecyclerOptions.Builder<Feedback>()
                .setQuery(feed_ref,Feedback.class).build();

        FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> adapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, int i, @NonNull Feedback feedback) {

                feedbackViewHolder.feedback_name.setText("Name: " + feedback.getName());
                feedbackViewHolder.feedback_oid.setText("Oid: " + feedback.getOid());
                feedbackViewHolder.feedback_txt.setText(feedback.getFeedback());
            }

            @NonNull
            @Override
            public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_show_layout,parent,false);
                FeedbackViewHolder holder = new FeedbackViewHolder(view);
                return holder;
            }
        };
        feedbackRecy.setAdapter(adapter);
        adapter.startListening();
    }
}