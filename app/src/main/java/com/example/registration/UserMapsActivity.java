package com.example.registration;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // private GoogleMap mMap;
    private GoogleMap mmap;
    FirebaseAuth fauth;

    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng sydney=new LatLng(-34,151);
    LatLng Teamworth=new LatLng(-31.083332,150.916672);
    LatLng Newcastle=new LatLng(-32.916668,151.750000);
    LatLng Brisbane=new LatLng(-27.470125,153.021072);
    LatLng Dubbo=new LatLng(-32.256943,148.601105);

    ArrayList<String> title=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        fauth = FirebaseAuth.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        arrayList.add(sydney);
        arrayList.add(Teamworth);
        arrayList.add(Newcastle);
        arrayList.add(Brisbane);
        arrayList.add(Dubbo);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        title.add("sydney");
        title.add("Teamworth");
        title.add("Newcastle");
        title.add("Brisbane");
        title.add("Dubbo");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmap = googleMap;
        LatLng sydney=new LatLng(-34,151);
        mmap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng Teamworth=new LatLng(-31.083332,150.916672);
        mmap.addMarker(new MarkerOptions().position(Teamworth).title("Teamworth"));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(Teamworth));

        LatLng Newcastle=new LatLng(-32.916668,151.750000);
        mmap.addMarker(new MarkerOptions().position(Newcastle).title("Newcastle"));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(Newcastle));

        LatLng Brisbane=new LatLng(-27.470125,153.021072);
        mmap.addMarker(new MarkerOptions().position(Brisbane).title("Brisbane"));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(Brisbane));

        LatLng Dubbo=new LatLng(-32.256943,148.601105);
        mmap.addMarker(new MarkerOptions().position(Dubbo).title("Dubbo"));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(Dubbo));

     //   "users/"+fauth.getCurrentUser().getUid()+"/longitude").setValue(Longitude).addOnCompleteListener(new OnCompleteListener<Void>() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+fauth.getCurrentUser().getUid()+"/Location");
        ValueEventListener listner= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double latitude =dataSnapshot.child("latitude").getValue(Double.class);
                Double longitude=dataSnapshot.child("longitude").getValue(Double.class);

                LatLng location =new LatLng(latitude,longitude);
                mmap.addMarker(new MarkerOptions().position(location).title("Marker Location"));
                mmap.moveCamera(CameraUpdateFactory.newLatLng(location));
                Toast.makeText(UserMapsActivity.this, "lat "+latitude, Toast.LENGTH_SHORT).show();
                Toast.makeText(UserMapsActivity.this, "long "+longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markertitle=marker.getTitle();
            //    Toast.makeText(UserMapsActivity.this, "waheguru "+markertitle, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(UserMapsActivity.this,BikeDetail.class);
                i.putExtra("title",markertitle);
                startActivity(i);
                return false;
            }
        });

        //  mmap = googleMap;
        //

    }


}
