package com.example.nitcbasket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nitcbasket.prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    //private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //load = new ProgressDialog(this);
    }

    public void login(View v)
    {

        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
    }
    public void register(View v)
    {

        Intent i = new Intent(this,Register.class);
        startActivity(i);
    }



}