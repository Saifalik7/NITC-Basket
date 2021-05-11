package com.example.nitcbasket.admin;

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

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddDeliveryMan extends AppCompatActivity {

    private EditText del_fullName,del_emailText,del_contact,del_password,del_confirmPassword;
    private ProgressDialog load;
    private int x;

    static DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_man);

        Button regi = (Button) findViewById(R.id.del_buttonRegister);
        x = 0;
        // mAuth = FirebaseAuth.getInstance();
        load = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance().getReference("DeliveryMans");

        del_fullName = findViewById(R.id.del_editName);
        del_emailText = findViewById(R.id.del_editEmail);
        del_password = findViewById(R.id.del_editPassword);
        del_contact = findViewById(R.id.del_editContact);
        del_confirmPassword = findViewById(R.id.del_editConPassword);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                load.setMessage("Creating a New Account");
                load.setCanceledOnTouchOutside(false);
                load.show();
                register();
            }
        });
    }

    public void register() {
        String email, pass, cont, conPas, name;
        email = del_emailText.getText().toString();
        pass = del_password.getText().toString();
        cont = del_contact.getText().toString();
        conPas = del_confirmPassword.getText().toString();
        name = del_fullName.getText().toString();

        if (TextUtils.isEmpty(name)) {
           del_fullName.setError("please enter name");
            load.dismiss();
            return;
        }
        // Validations for input email and password
        else if (TextUtils.isEmpty(email)) {
           del_emailText.setError("Please enter email");
            load.dismiss();
            return;
        }

        else if (TextUtils.isEmpty(cont)) {
            del_contact.setError("Please enter contact");
            load.dismiss();
            return;
        }
        else if(cont.length() != 10)
        {
            del_contact.setError("contact size should be 10");
            load.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(pass)) {
          del_password.setError("please enter password");
            load.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(conPas)) {
           del_confirmPassword.setError("please confirm password");
            load.dismiss();
            return;
        }
        else if(pass.equals(conPas) == false)
        {
           del_confirmPassword.setError("Password doesn't match");
           load.dismiss();
           return;
        }

        else {

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    for(DataSnapshot snap : snapshot.getChildren()) {
                        User ud = snap.getValue(User.class);

                        if(ud.getEmail().equals(email))
                        {
                            x = 1;
                            load.dismiss();
                            //Toast.makeText(getApplicationContext(),"Email already in use",Toast.LENGTH_LONG).show();
                            //del_emailText.setError("email already in use");

                        }

                    }
                    if(x == 0)
                    {
                       /* String id=db.push().getKey();
                        User udata = new User(name, email, cont, pass,id,);
                        db.child(id).setValue(udata);
                        Toast.makeText(Register.this,"User registered",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this,Homepage.class);
                        startActivity(i);*/
                        String id = db.push().getKey();
                        HashMap<String,Object> uMap = new HashMap<>();
                        uMap.put("name",name);
                        uMap.put("email",email);
                        uMap.put("contact",cont);
                        uMap.put("password",pass);
                        uMap.put("Uid",id);
                        uMap.put("currStatus","Active");
                        uMap.put("refuseCount","0");
                        db.child(id).setValue(uMap);
                        load.dismiss();
                        Toast.makeText(AddDeliveryMan.this,"Added ",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(AddDeliveryMan.this, AdminDash.class);
                        startActivity(i);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }
}