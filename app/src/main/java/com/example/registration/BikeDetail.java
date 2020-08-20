package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class BikeDetail extends AppCompatActivity {

    TextView markertxt;
    ImageView userimg, bikepic;
    private StorageReference StorageReference;
    private StorageReference bikeStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);
        userimg=findViewById(R.id.userimg);
        bikepic=findViewById(R.id.bikeimg);
        bikeStorageReference=FirebaseStorage.getInstance().getReference().child("BikeLending.jpg");
        StorageReference =FirebaseStorage.getInstance().getReference().child("profile.jpg");
       // final StorageReference fileRef = StorageReference.child("profile.jpg");

        try {
            final File localFile = File.createTempFile("profile", "jpg");
            StorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(BikeDetail.this, "Working...", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap=BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            userimg.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BikeDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            final File localFile = File.createTempFile("profile", "jpg");
            bikeStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(BikeDetail.this, "Working...", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap=BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            bikepic.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BikeDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}