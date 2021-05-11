package com.example.nitcbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.admin.AdminDash;
import com.example.nitcbasket.deliveryman.DeliveryManLogin;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.user.Homepage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

   private EditText Email, Pass;
   private TextView delman_login;

   private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        load = new ProgressDialog(this);
        Button btn = (Button) findViewById(R.id.buttonlogin);
        Email = findViewById(R.id.editEmail);
        Pass =  findViewById(R.id.editPassword);

        delman_login = (TextView) findViewById(R.id.del_man_login);

        delman_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, DeliveryManLogin.class);
                startActivity(i);
                finish();
            }
        });

        Paper.init(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setMessage("Logging in");
                load.setCanceledOnTouchOutside(false);
                load.show();
                login();
            }
        });





    }

    public void login()
    {

        String email,pass;
        email = Email.getText().toString();
        pass = Pass.getText().toString();
        if (TextUtils.isEmpty(email)) {
           /* Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();*/
            Email.setError("Enter email");
            load.dismiss();
            return;
        } else if (TextUtils.isEmpty(pass)) {
           /* Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();*/
            Pass.setError("enter password");
            load.dismiss();
            return;
        }
        else if(email.equals("naziya_m190393ca@nitc.ac.in") && pass.equals("11223344") )
        {
               Toast.makeText(getApplicationContext(), "Login succesfull", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Login.this, AdminDash.class);
                startActivity(i);
                finish();
                return ;

        }
        else {

           final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
            //final DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Admin");

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int x = 0;
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        User ud = snap.getValue(User.class);
                       // Toast.makeText(getApplicationContext(),"child",Toast.LENGTH_LONG).show();
                         if(ud.getEmail().equals(email)  )
                         {
                             if(ud.getPassword().equals(pass)) {
                                 Prevalent.currentUser = ud;
                                 x = 1;
                                 Toast.makeText(getApplicationContext(), "Login succesfull", Toast.LENGTH_LONG).show();

                                 Intent i = new Intent(Login.this, Homepage.class);
                                 startActivity(i);
                                 finish();
                             }
                             else{
                                 load.dismiss();
                                 /*Toast.makeText(getApplicationContext(),
                                         "Password is Wrong!!",
                                         Toast.LENGTH_LONG)
                                         .show();*/
                                 Pass.setError("Password is wrong");
                             }
                         }

                    }
                    if(x == 0)
                    {
                        load.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Credential",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Admin



        }
    }
        public void register (View v)
        {
            Intent i = new Intent(this, Register.class);
            startActivity(i);
        }


}