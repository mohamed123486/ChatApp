package com.example.finelall.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finelall.Manger.mange;
import com.example.finelall.R;
import com.example.finelall.model.FindFriends_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class Friends_Profile extends AppCompatActivity {
CircularImageView imageView;
TextView name,email;
ProgressDialog progressDialog;
AppCompatButton add,accept;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);
        imageView=findViewById(R.id.friends_img);
        name=findViewById(R.id.friends_name);
        add=findViewById(R.id.add);
        email=findViewById(R.id.friends_email);
        accept=findViewById(R.id.accept);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Data .....");
        progressDialog.show();
        String id=getIntent().getStringExtra("id");
        if (!id.isEmpty()){
            FirebaseDatabase.getInstance().getReference().child("User").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String  na=snapshot.child("Username").getValue(String.class);
                    String  i=snapshot.child("Image").getValue(String.class);
                    String  e=snapshot.child("Email").getValue(String.class);
                    Picasso.get().load(i).placeholder(R.drawable.ic_baseline_person_24).into(imageView);
                    name.setText(na);
                    email.setText(e);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        FindFriends_model model= (FindFriends_model) getIntent().getSerializableExtra("model");
        String userid=getIntent().getStringExtra("id");
        if (model!=null){
            Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_baseline_person_24).into(imageView);
            name.setText(model.getUsername());
            email.setText(model.getEmail());
            progressDialog.dismiss();
        }



        new mange(userid,add,accept);

    }
}