package com.example.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity {


    ImageView img;
    TextView fullname,email,phone,verifying;
    FirebaseAuth fauth;
    FirebaseStorage fstore;
    String userId;
    Button changeProfileImage,btn2,button3;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    private static final int GALLERY_INTENT_CODE = 1023;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    public StorageReference storageReference;
    EditText regName,regUsername,regEmail,regPhoneNo,regPassword;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img=findViewById(R.id.img);
        changeProfileImage=findViewById(R.id.passtxt);
         btn2=findViewById(R.id.btn2);
        button3=findViewById(R.id.button3);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseStorage.getInstance();
        storageReference =FirebaseStorage.getInstance().getReference();

          regName =findViewById(R.id.reg_name);
          regEmail =findViewById(R.id.reg_email);
          regPassword =findViewById(R.id.reg_password);
          regPhoneNo =findViewById(R.id.reg_phoneNo);

        firebaseAuth = FirebaseAuth.getInstance();

button3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(Register.this,DashBoard.class);
        startActivity(i);
    }
});

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(openGalleryIntent,1000);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //Accessing Gallery pictures
            rootnode = FirebaseDatabase.getInstance();
            reference = rootnode.getReference("users");

                String name=regName.getEditableText().toString();
                String email=regEmail.getEditableText().toString();
                String phone=regPhoneNo.getEditableText().toString();
                String password=regPassword.getEditableText().toString();
                String latitude = "";
                String longitude= "";
                Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);
                intent.putExtra("phone",phone);
                startActivity(intent);

            UserHelperClass helperClass = new UserHelperClass(name,email,phone,password,latitude,longitude);

            reference.child(fauth.getCurrentUser().getUid()).setValue(helperClass);
                Toast.makeText(Register.this, "Registered Succesfully..", Toast.LENGTH_SHORT).show();
            //



             //   String email = emailId.getText().toString();
             //   String password = passwordt.getText().toString();
              //  firebaseAuth = FirebaseAuth.getInstance();

                if(email.isEmpty())
                {
                    regEmail.setError( "please enter your email id " );
                    regEmail.requestFocus();
                }
                else if (password.isEmpty())
                {
                    regPassword.setError( "please enter your password" );
                    regPassword.requestFocus();

                } else if (!email.isEmpty() && (!password.isEmpty()))
                {
                    firebaseAuth.createUserWithEmailAndPassword( email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete ( @NonNull Task<AuthResult> task ) {
                           // userID=fauth.getCurrentUser().getUid();
                        //    Toast.makeText(Register.this, "userid"+userID, Toast.LENGTH_SHORT).show();
                            if(!task.isSuccessful()){
                               // Toast.makeText( UserMapsActivity.this , "Sign Up fAILED" , Toast.LENGTH_SHORT ).show();
                                Toast.makeText(Register.this, "Sign Up Succesfull...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                               // Toast.makeText( MainActivity.this , "Sign up sucessfull" , Toast.LENGTH_SHORT ).show();
                             //   startActivity( new Intent( MainActivity.this,HomeActivity.class ) );
                                Toast.makeText(Register.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } );
                }
            }


    //        }
        });
    }

    @Override              //fetching picture and seting on image view
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                img.setImageURI(imageUri);    //Set im age in image view on xml file
                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {  // uploading image to firebase with picasso library
        final StorageReference fileRef = storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(img); // using picasso library code to upload add dependency for picasso from internet
                        Toast.makeText(Register.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
