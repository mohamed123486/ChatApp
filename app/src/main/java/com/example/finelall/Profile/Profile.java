package com.example.finelall.Profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finelall.MainActivity;
import com.example.finelall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
TextInputEditText email,username;
TextInputLayout ma;
private DatabaseReference databaseReference;
private FirebaseAuth auth;
TextView tx;
CircularImageView image;
private FirebaseStorage storage;
ProgressDialog progressDialog;
String img;
AppCompatButton skip;
HashMap<String ,Object>hashMap=new HashMap<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    skip=findViewById(R.id.skip);
    ma=findViewById(R.id.ma);
    tx=findViewById(R.id.tx);
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("UpDate Your Image");
        progressDialog.setTitle("Loading.....");

        databaseReference= FirebaseDatabase.getInstance().getReference();
        email=findViewById(R.id.email);
        username=findViewById(R.id.username);
        image=findViewById(R.id.image);
        int i=getIntent().getIntExtra("sin",-1);
        if (i==10){
            email.setEnabled(false);
            username.setEnabled(false);
            RetrieveDataFromSingUp();
        }else {
            image.setEnabled(false);
            progressDialog.show();
            tx.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
            email.setEnabled(false);
            username.setEnabled(false);
            RetrieveDataFromSingUp();
        }
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Skiping", Toast.LENGTH_SHORT).show();
            }
        });
        ActivityResultLauncher<Intent>launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK){
                    progressDialog.show();
                    Uri i=result.getData().getData();
                    storage.getReference().child("ProfileImage").child(auth.getUid()).putFile(i).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        img=task.getResult().toString();
                                        Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(image);
                                        hashMap.put("Image",img);
                                        progressDialog.dismiss();
                                        databaseReference.child("User").child(auth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    Toast.makeText(Profile.this, "Profile UUpdated", Toast.LENGTH_SHORT).show();
                                                skip.setText("Finish");
                                            }
                                        });
                                    }
                                });
                        }
                    });
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("Image/*"));
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private void RetrieveDataFromSingUp(){
        databaseReference.child("User").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String mail=snapshot.child("Email").getValue(String.class);
                    String user=snapshot.child("Username").getValue(String.class);
                     img=snapshot.child("Image").getValue(String.class);
                    email.setText(mail);
                    username.setText(user);
                    if (img!=null){
                        Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(image);
                    }
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        skip.setVisibility(View.VISIBLE);
        image.setEnabled(true);
        skip.setText("Edit");
        username.setEnabled(true);
        ma.setVisibility(View.GONE);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString();
                if (user.isBlank()){
                    Toast.makeText(Profile.this, "Sorry their is An Error "+"\n"+"Please Enter Your User Name", Toast.LENGTH_SHORT).show();
                }else {
                    hashMap.put("Username",user);
                    hashMap.put("Image",img);
                    databaseReference.child("User").child(auth.getUid()).updateChildren(hashMap);
                    Toast.makeText(Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        return super.onOptionsItemSelected(item);
    }
}