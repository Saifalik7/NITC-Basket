package com.example.nitcbasket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nitcbasket.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProduct extends AppCompatActivity {

    private String categoryName,pName,pQuantity,pPrice,saveCurrentDate, saveCurrentTime;
    private Button addProduct;
    private EditText name, quantity, price;
    private ImageView inputImg;
    private Uri ImgUrl;
    private ProgressDialog load;
    private static final int Gallerypic = 1;
    private String productRandomKey,downloadImageUrl;
    private StorageReference productref;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        load = new ProgressDialog(this);
        categoryName = getIntent().getExtras().get("category").toString();
        productref = FirebaseStorage.getInstance().getReference().child("Product Images");
        db = FirebaseDatabase.getInstance().getReference().child("Products");

        addProduct= (Button) findViewById(R.id.addprod) ;
        name = (EditText) findViewById(R.id.editprod);
        quantity = (EditText) findViewById(R.id.editquant);
        price = (EditText) findViewById(R.id.editprice);
        inputImg = (ImageView) findViewById(R.id.inputimg);

        inputImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    public void openGallery()
    {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,Gallerypic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallerypic && resultCode == RESULT_OK && data != null )
        {
            ImgUrl = data.getData();
            inputImg.setImageURI(ImgUrl);

        }
    }

    private void validateData()
    {
       pName = name.getText().toString();
       pPrice = price.getText().toString();
       pQuantity = quantity.getText().toString();

       if(ImgUrl == null)
       {
           Toast.makeText(getApplicationContext(),
                   "Image is mandatory",
                   Toast.LENGTH_LONG)
                   .show();
           return ;
       }else if (TextUtils.isEmpty(pName)) {
           name.setError("please enter product name");
           return ;
       }
       else if (TextUtils.isEmpty(pQuantity)) {
          quantity.setError("please enter quantity");
          return;

       }
       else if (TextUtils.isEmpty(pPrice)) {
          price.setError("please enter price");
          return;

       }else
       {
           load.setMessage("Adding Product..!");
           load.setCanceledOnTouchOutside(false);
           load.show();
           storeProductInfo();
       }
    }
    private void storeProductInfo()
    {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
        StorageReference filepath = productref.child(ImgUrl.getLastPathSegment() + productRandomKey + ".jpg");
       final UploadTask upload = filepath.putFile(ImgUrl);

       upload.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               String message = e.toString();
               Toast.makeText(getApplicationContext(),"Error :" + message + "occur", Toast.LENGTH_LONG).show();
           }
       })
       .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               Toast.makeText(getApplicationContext(),
                       "Image uploaded succesfully",
                       Toast.LENGTH_LONG)
                       .show();

               Task<Uri> urltask = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                   @Override
                   public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful()) {
                           throw task.getException();
                       }
                       downloadImageUrl = filepath.getDownloadUrl().toString();
                       return filepath.getDownloadUrl();
                   }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {

                       if(task.isSuccessful()) {
                               downloadImageUrl = task.getResult().toString();
                           saveProductInfoToDatabase();
                       }
                   }
               });
           }
       });
    }
    private void saveProductInfoToDatabase()
    {
        HashMap<String,Object> imgdata = new HashMap<>();
        imgdata.put("Pid",productRandomKey);
        imgdata.put("Date", saveCurrentDate);
        imgdata.put("Time",saveCurrentTime);
        imgdata.put("Price",pPrice);
        imgdata.put("Pname",pName);
        imgdata.put("Quantity",pQuantity);
        imgdata.put("Image",downloadImageUrl);

        db.child(productRandomKey).setValue(imgdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Product Added Successfully",
                                    Toast.LENGTH_LONG)
                                    .show();
                            Intent i = new Intent(AdminAddProduct.this, AdminPanel.class);
                            startActivity(i);

                        }
                        else
                        {
                            load.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Error adding Product",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

    }
}








