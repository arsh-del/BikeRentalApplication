package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button btn,btnlog;
    EditText txtemail , txtpassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        btnlog=findViewById(R.id.btnlogin);
        btn=findViewById(R.id.btnsup);
        txtemail=findViewById(R.id.emailtxt);
        txtpassword=findViewById(R.id.passtxt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtemail.getText().toString().trim();
                Toast.makeText(Login.this, "txt" + email, Toast.LENGTH_SHORT).show();
                String password = txtpassword.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity( new Intent( getApplicationContext(),DashBoard.class ) );

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText( Login.this , "login failed " , Toast.LENGTH_SHORT ).show();

                        }
                    }
                });
            }
        } );
    }
}

