package com.example.finelall.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finelall.Ad.group_Ad;
import com.example.finelall.R;
import com.example.finelall.model.group_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Groups extends Fragment {
RecyclerView res;
group_Ad ad;
ArrayList<group_model>ll=new ArrayList<>();
AppCompatButton create;
TextInputEditText name,pass;
CircularImageView imageView;
String img;
ActivityResultLauncher<Intent>launcher;
HashMap<String ,Object>hashMap=new HashMap<>();
FloatingActionButton add;
ProgressDialog progressDialog;
String na;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FirebaseDatabase.getInstance().getReference().child("Groups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name=snapshot.child("Name").getValue(String.class);
                img=snapshot.child("Image").getValue(String.class);
                String p=snapshot.child("Password").getValue(String.class);
                ll.add(new group_model(img,p,name));
                ad.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK){
                    Uri i=result.getData().getData();
                    progressDialog.show();
                    if (i!=null){
                        FirebaseStorage.getInstance().getReference().child("GroupsImage").child(FirebaseDatabase.getInstance().getReference().push().getKey()).putFile(i).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){

                                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            img=task.getResult().toString();
                                            Picasso.get().load(img).into(imageView);
                                            try {
                                                Thread.sleep(5000);
                                                progressDialog.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

            }
        });
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading.....");
        progressDialog.setMessage("Please Wait until Downloading Group Image....");
        progressDialog.setCanceledOnTouchOutside(false);
        res=view.findViewById(R.id.res);
        res.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ad=new group_Ad(ll);
        res.setAdapter(ad);
        ad.notifyDataSetChanged();

        add=view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayDislodge();
            }
        });

    }
    @SuppressLint("MissingInflatedId")
    private void DisplayDislodge(){
        AlertDialog.Builder a=new AlertDialog.Builder(getContext());
        View v=LayoutInflater.from(getContext()).inflate(R.layout.groups_dilog,null,false);
        create=v.findViewById(R.id.create);
        name=v.findViewById(R.id.name);
        pass=v.findViewById(R.id.password);
        imageView=v.findViewById(R.id.img);
        a.setView(v);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("Image/*"));
            }
        });
        AlertDialog dialog=a.create();
        dialog.show();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                na=name.getText().toString();
                String pas=pass.getText().toString();
                if (na.isBlank()&&pas.isBlank()){
                    Toast.makeText(getContext(), "Please Enter Full Data", Toast.LENGTH_SHORT).show();
                }else {
                   // ll.add(new group_model(img,pas,na));
                    hashMap.put("Name",na);
                    hashMap.put("Image",img);
                    hashMap.put("Password",pas);
                    FirebaseDatabase.getInstance().getReference().child("Groups").child(na).setValue(hashMap).isSuccessful();
                    dialog.dismiss();
                }

            }
        });


    }
}