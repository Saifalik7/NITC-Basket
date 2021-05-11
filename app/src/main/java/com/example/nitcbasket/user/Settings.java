package com.example.nitcbasket.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcbasket.Model.User;
import com.example.nitcbasket.R;
import com.example.nitcbasket.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private CircleImageView  cirImg;
    private TextView close, update, changeProfile;
    private EditText eName, eContact, epass;

    private Uri imageUrl;
    private String myUrl = "";
    private StorageReference profileref;
    private String checker = "";
    private StorageTask uploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cirImg = (CircleImageView)findViewById(R.id.set_profile_img);
        close = (TextView)findViewById(R.id.close);
        update = (TextView)findViewById(R.id.update);
        changeProfile = (TextView)findViewById(R.id.change_profile);
        eName = (EditText)findViewById(R.id.edit_name);
        eContact = (EditText)findViewById(R.id.edit_contact);
        epass = (EditText)findViewById(R.id.edit_pass);

        profileref = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        userInfo(cirImg,eName,eContact,epass);

        getUserDetails();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("checked")){

                    userInfoSaved();
                }
                else{
                    updateOnlyUserInfo();
                }
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "checked";

                CropImage.activity(imageUrl).setAspectRatio(1,1).start(Settings.this);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null )
        {
            CropImage.ActivityResult  result = CropImage.getActivityResult(data);
            imageUrl = result.getUri();

            cirImg.setImageURI(imageUrl);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.this,Settings.class));
            finish();
        }
    }


    public void userInfo(CircleImageView cirImg,EditText eName, EditText eContact,EditText epass )
    {
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getUid());
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    if(snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String uname = snapshot.child("name").getValue().toString();
                        String ucontact = snapshot.child("contact").getValue().toString();
                        String upass = snapshot.child("password").getValue().toString();
                        Picasso.get().load(image).into(cirImg);
                        eName.setText(uname);
                        eContact.setText(ucontact);
                        epass.setText(upass);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(eName.getText().toString())) {
          eName.setError("please enter name");
           return;
        }
        else  if (TextUtils.isEmpty(eContact.getText().toString())) {
           // Toast.makeText(getApplicationContext(),"Enter contact",Toast.LENGTH_LONG).show();
            eContact.setError("please enter contact");
           return ;
        }
        else if (TextUtils.isEmpty(epass.getText().toString())) {
            //Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_LONG).show();
            epass.setError("please enter password");
           return;
        }
        else if(checker.equals("checked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog load = new ProgressDialog(this);
        load.setMessage("Uploading Image");
        load.setCanceledOnTouchOutside(false);
        load.show();
        if(imageUrl != null)
        {
           final StorageReference fileref = profileref.child(Prevalent.currentUser.getContact() + ".jpg");
           uploadtask = fileref.putFile(imageUrl);

           uploadtask.continueWithTask(new Continuation() {
               @Override
               public Object then(@NonNull Task task) throws Exception {

                   if(!task.isSuccessful())
                   {
                       throw task.getException();
                   }
                   return fileref.getDownloadUrl();
               }
           })
                   .addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       Uri downloaduri = task.getResult();
                       myUrl = downloaduri.toString();

                       DatabaseReference ref = FirebaseDatabase.getInstance()
                               .getReference().child("Users");

                       HashMap<String, Object> userMap = new HashMap<>();
                       userMap.put("name", eName.getText().toString());
                       userMap.put("contact", eContact.getText().toString());
                       userMap.put("password", epass.getText().toString());
                       userMap.put("image", myUrl);
                        ref.child(Prevalent.currentUser.getUid()).updateChildren(userMap);
                       load.dismiss();
                       startActivity(new Intent(Settings.this, Homepage.class));
                       finish();
                   }

               }
           });
        }
    }

    private void getUserDetails()
    {
        DatabaseReference prodref = FirebaseDatabase.getInstance().getReference().child("Users");

        prodref.child(Prevalent.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    User ud = snapshot.getValue(User.class);

                    eName.setText(ud.getName());
                    eContact.setText(ud.getContact());
                    epass.setText(ud.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateOnlyUserInfo()
    {

        if (TextUtils.isEmpty(eName.getText().toString())) {
            eName.setError("please enter name");
            return;
        }
        else  if (TextUtils.isEmpty(eContact.getText().toString())) {
            // Toast.makeText(getApplicationContext(),"Enter contact",Toast.LENGTH_LONG).show();
            eContact.setError("please enter contact");
            return ;
        }
        else if (TextUtils.isEmpty(epass.getText().toString())) {
            //Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_LONG).show();
            epass.setError("please enter password");
            return;
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference().child("Users");

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", eName.getText().toString());
            userMap.put("contact", eContact.getText().toString());
            userMap.put("password", epass.getText().toString());

            ref.child(Prevalent.currentUser.getUid()).updateChildren(userMap);
            Toast.makeText(Settings.this, "Information updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, Homepage.class));
            finish();
        }
    }
}










