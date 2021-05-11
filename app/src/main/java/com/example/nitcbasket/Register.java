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
import android.widget.Toast;

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.user.ConfirmOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText fullName,emailText,contact,password,confirmPassword;
    String userId;
    private ProgressDialog load;
    private int x ;

    static DatabaseReference db;
   // private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       Button regi = (Button) findViewById(R.id.buttonRegister);
       // mAuth = FirebaseAuth.getInstance();
        x=0;
        load = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance().getReference("Users");

        fullName = findViewById(R.id.editName);
        emailText = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        contact = findViewById(R.id.editContact);
        confirmPassword = findViewById(R.id.editConPassword);
       regi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {

               register();
           }
       });
    }
    public void login(View v)
    {
        Intent i = new Intent(Register.this,Login.class);
        startActivity(i);
    }
    public void register() {
        String email, pass, cont, conPas, name;
        email = emailText.getText().toString();
        pass = password.getText().toString();
        cont = contact.getText().toString();
        conPas = confirmPassword.getText().toString();
        name = fullName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            fullName.setError("Please Enter your name");
           // Toast.makeText(Register.this, "please enter Name", Toast.LENGTH_LONG).show();
           // load.dismiss();
            return;
        }
        // Validations for input email and password
        else if (TextUtils.isEmpty(email)) {
           emailText.setError("please enter your email");
           // load.dismiss();
            return;
        }
        else if((email.substring(email.length()-10,email.length())).equals("nitc.ac.in") == false)
        {
            emailText.setError("Enter your NITC email ");
            return;
        }

       else if (TextUtils.isEmpty(cont)) {
            contact.setError("Please Enter your contact");
            //load.dismiss();
            return;
        }
       else if(cont.length() != 10)
        {
            contact.setError("contact size should be 10");
        }
        else if (TextUtils.isEmpty(pass)) {
            password.setError("please Enter your password");
            //load.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(conPas)) {
           confirmPassword.setError("Confirm your password");
           // load.dismiss();
            return;
        }else if (pass.equals(conPas) == false ) {
            confirmPassword.setError("Password doesn't match");
           // load.dismiss();
            return;
        }

        else {

            load.setMessage("Creating a New Account");
            load.setCanceledOnTouchOutside(false);
            load.show();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // x = 0;
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        User ud = snap.getValue(User.class);

                        if( email.equals(ud.getEmail()) == true)
                        {
                            x = 1;
                            load.dismiss();
                           // Toast.makeText(Register.this,"Email already in use",Toast.LENGTH_LONG).show();
                            //break;
                            //emailText.setError("email already in use");
                           // break;
                        }

                    }
                    if(x == 0)
                    {
                        String id = db.push().getKey();
                        HashMap<String, Object> uMap = new HashMap<>();
                        uMap.put("name", name);
                        uMap.put("email", email);
                        uMap.put("contact", cont);
                        uMap.put("password", pass);
                        uMap.put("Uid", id);
                        uMap.put("currStatus","Active");
                        uMap.put("refuseCount","0");
                        db.child(id).setValue(uMap);
                        load.dismiss();
                        Toast.makeText(Register.this, "User registered", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Register.this, Login.class);
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