package com.example.finelall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finelall.Ad.chat_Ad;
import com.example.finelall.model.FindFriends_model;
import com.example.finelall.model.chat_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Chat extends AppCompatActivity {
HashMap<String ,Object>hashMap=new HashMap<>();
RecyclerView res;
ImageView send;
EditText msg;
String  userid;
chat_Ad ad;
ArrayList<chat_model>ll=new ArrayList<>();
String img;
String resver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        res=findViewById(R.id.res);
        msg=findViewById(R.id.msg);
        userid=getIntent().getStringExtra("id");
        res.setLayoutManager(new LinearLayoutManager(this));
        ad=new chat_Ad(ll);
        res.setAdapter(ad);
        send=findViewById(R.id.send);
        getSupportActionBar().hide();
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                img=snapshot.child("Image").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=FirebaseDatabase.getInstance().getReference().push().getKey();
                String  m=msg.getText().toString();
                if (!m.isEmpty()){
                    hashMap.put("msg",m);
                    hashMap.put("from",FirebaseAuth.getInstance().getUid());
                    AddMsgS(userid,key);
                }
                msg.setText(" ");
            }
        });
    }
    private void AddMsgS(String  id,String key){
        FirebaseDatabase.getInstance().getReference().child("Msg").child(id).child(FirebaseAuth.getInstance().getUid()).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Msg").child(FirebaseAuth.getInstance().getUid()).child(id).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){


                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Msg").child(userid).child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String from=snapshot.child("from").getValue(String.class);
                String msg=snapshot.child("msg").getValue(String.class);
                String img=snapshot.child("img").getValue(String.class);
                String time=new SimpleDateFormat("HH::mm:ss").format(Calendar.getInstance().getTime());
                ll.add(new chat_model(msg,from,time,img));
                ad.notifyDataSetChanged();
                res.smoothScrollToPosition(ad.getItemCount());
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
}