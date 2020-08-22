package com.example.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class LenderMaps extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    ImageView imageView;
    FirebaseDatabase root;
    DatabaseReference reference;
    Button on,off;
    public StorageReference storageReference;
    ImageButton imgbtn;
    FirebaseAuth fauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_maps);
        on=findViewById(R.id.on);
        off=findViewById(R.id.off);
        fauth = FirebaseAuth.getInstance();
       // imageView=findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference();
        imgbtn=findViewById(R.id.imgb);
       // imageView.setImageResource(R.drawable.giantcycle);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(openGalleryIntent,1000);
            }
        });
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(LenderMaps.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(LenderMaps.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }



    @Override              //fetching picture and seting on image view
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                imgbtn.setImageURI(imageUri);    //Set im age in image view on xml file
                uploadImageToFirebase(imageUri);
            }
        }
    }



    private void uploadImageToFirebase(Uri imageUri) {  // uploading image to firebase with picasso library
        final StorageReference fileRef = storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/BikeLending.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imgbtn); // using picasso library code to upload add dependency for picasso from internet
                        Toast.makeText(LenderMaps.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LenderMaps.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.addMarker(options);

                            final Double Latitude=location.getLatitude();
                            final Double Longitude=location.getLongitude();
                            Toast.makeText(LenderMaps.this, "Latitude "+Latitude, Toast.LENGTH_SHORT).show();
                            Toast.makeText(LenderMaps.this, "Log "+Longitude, Toast.LENGTH_SHORT).show();

                            on.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    root = FirebaseDatabase.getInstance();
                                    reference = root.getReference("Locations");

                                //    storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");

                                    LocationHelper helper= new LocationHelper(Latitude,Longitude);

                               FirebaseDatabase.getInstance().getReference("users/"+fauth.getCurrentUser().getUid()+"/Location").setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(LenderMaps.this, "Your Bike is Available for User 1", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(LenderMaps.this, "Some problem Occured", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                                    FirebaseDatabase.getInstance().getReference("users/"+fauth.getCurrentUser().getUid()+"/longitude").setValue(Longitude).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(LenderMaps.this, "Your Bike is Available for User 2", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(LenderMaps.this, "Some problem Occured", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                                }
                            });

                            off.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(LenderMaps.this, "You are Offline...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}