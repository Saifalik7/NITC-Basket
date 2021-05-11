package com.example.nitcbasket.deliveryman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nitcbasket.Login;
import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.admin.AdminDash;
import com.example.nitcbasket.prevalent.Prevalent;
import com.example.nitcbasket.prevalent.PrevalentDel;
import com.example.nitcbasket.user.Homepage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryManLogin extends AppCompatActivity {

    private EditText del_editEmail, del_editPass;

    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man_login);

        load = new ProgressDialog(this);
        Button del_btn = (Button) findViewById(R.id.del_buttonlogin);
        del_editEmail = findViewById(R.id.del_editEmail);
        del_editPass =  findViewById(R.id.del_editPassword);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setMessage("Logging in");
                load.setCanceledOnTouchOutside(false);
                load.show();
                del_login();
            }
        });
    }

    private void del_login()
    {
        String email,pass;
        email = del_editEmail.getText().toString();
        pass = del_editPass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            del_editEmail.setError("please enter email");
            load.dismiss();
            return;
        } else if (TextUtils.isEmpty(pass)) {
           del_editPass.setError("please enter password");
            load.dismiss();
            return;
        }


        else {

            final DatabaseReference del_db = FirebaseDatabase.getInstance().getReference("DeliveryMans");

            del_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int x = 0;
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        User udata = snap.getValue(User.class);

                        if(udata.getEmail().equals(email)  )
                        {
                            if(udata.getPassword().equals(pass)) {
                                PrevalentDel.currentDelMan = udata;
                                x = 1;
                                Toast.makeText(getApplicationContext(), "Login succesfull", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(DeliveryManLogin.this, DelManDash.class);
                                startActivity(i);
                            }
                            else{
                                load.dismiss();
                                /*Toast.makeText(getApplicationContext(),
                                        "Password is Wrong!!",
                                        Toast.LENGTH_LONG)
                                        .show();*/
                                del_editPass.setError("Password is wrong");
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

        }
    }
}









