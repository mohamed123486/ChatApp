package com.example.finelall.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.finelall.Profile.Profile;
import com.example.finelall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SingUp extends AppCompatActivity {
TextInputEditText username,email,password;
private FirebaseAuth auth;
private DatabaseReference database;
HashMap<String ,Object>hashMap=new HashMap<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance().getReference();
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        getSupportActionBar().hide();
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));
            }
        });
        findViewById(R.id.singup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString();
                String mail=email.getText().toString();
                String pass=password.getText().toString();
                CreateAccount(user,mail,pass);
            }
        });
    }
    private void CreateAccount(String user,String email,String password){
        if (user.isBlank()||email.isBlank()||password.isBlank()){
            Toast.makeText(this, "PLease Complete Your Data", Toast.LENGTH_SHORT).show();
        }else
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    hashMap.put("Username",user);
                    hashMap.put("Email",email);
                    hashMap.put("Password",password);
                    database.child("User").child(auth.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SingUp.this, "Welcome", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Profile.class).putExtra("sin",10));
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }
}