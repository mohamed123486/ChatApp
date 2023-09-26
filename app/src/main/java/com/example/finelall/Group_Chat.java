package com.example.finelall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.finelall.Ad.chat_Ad;
import com.example.finelall.Ad.chat_group_Ad;
import com.example.finelall.databinding.ActivityGroupChatBinding;
import com.example.finelall.model.chat_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Group_Chat extends AppCompatActivity {
ActivityGroupChatBinding binding;
HashMap<String,Object>hashMap=new HashMap<>();
chat_group_Ad ad;
    String name;
ArrayList<chat_model>ll=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.res.setLayoutManager(new LinearLayoutManager(this));
        name=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m=binding.msg.getText().toString();
                String time=new SimpleDateFormat("HH:MM:SS").format(Calendar.getInstance().getTime());
                String key= FirebaseDatabase.getInstance().getReference().push().getKey();
                hashMap.put("msg",m);
                hashMap.put("time",time);
                hashMap.put("from", FirebaseAuth.getInstance().getUid());
                if (!m.isEmpty())
                FirebaseDatabase.getInstance().getReference().child("Groups").child(name).child("Group_Msg").child(key).setValue(hashMap);
                binding.msg.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Groups").child(name).child("Group_Msg").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg=snapshot.child("msg").getValue(String.class);
                String from=snapshot.child("from").getValue(String.class);
                String time=snapshot.child("time").getValue(String.class);
                ll.add(new chat_model(msg,from,time));
                ad=new chat_group_Ad(ll);
                binding.res.setAdapter(ad);
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
}