package com.example.finelall.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finelall.R;
import com.example.finelall.model.FindFriends_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class Chat extends Fragment {
RecyclerView res;
    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        res=view.findViewById(R.id.res);
        res.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions <FindFriends_model>options=new FirebaseRecyclerOptions.Builder<FindFriends_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contacts").child(FirebaseAuth.getInstance().getUid()), FindFriends_model.class)
                .build();
        FirebaseRecyclerAdapter<FindFriends_model,Ad>adapter=new FirebaseRecyclerAdapter<FindFriends_model, Ad>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Ad holder, int position, @NonNull FindFriends_model model) {
                DatabaseReference data=getRef(position).getRef();
                String id=getRef(position).getKey();
                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String type=snapshot.child("type").getValue(String.class);
                        if (type!=null)
                        if (type.equals("Saved")){
                            FirebaseDatabase.getInstance().getReference().child("Msg").child(id).child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        FirebaseDatabase.getInstance().getReference().child("User").child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String img=snapshot.child("Image").getValue(String.class);
                                                name=snapshot.child("Username").getValue(String.class);
                                                Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(holder.chat_img);
                                                holder.chat_name.setText(name);

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(new Intent(getContext(), com.example.finelall.Chat.class).putExtra("id",id).putExtra("img",img));
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }else {
                                        holder.chat_time.setVisibility(View.GONE);
                                        holder.stat.setVisibility(View.GONE);
                                        holder.chat_img.setVisibility(View.GONE);
                                        holder.chat_name.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        FirebaseDatabase.getInstance().getReference().child("stat").child(id).child("typ_stat").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String time=snapshot.child("time").getValue(String.class);
                                    String stat=snapshot.child("user stat").getValue(String.class);
                                    if (stat.equals("online")){
                                        holder.chat_time.setText("Online");
                                        holder.stat.setBackgroundColor(Color.GREEN);
                                    }else {
                                        holder.chat_time.setText("Last seen At : "+time);
                                        holder.stat.setBackgroundColor(Color.WHITE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public Ad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Ad(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false));
            }
        };
        res.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
    class Ad extends RecyclerView.ViewHolder{
        CircularImageView chat_img;
        TextView chat_name,chat_time;
        LinearLayout stat;
        public Ad(@NonNull View itemView) {
            super(itemView);
            chat_img=itemView.findViewById(R.id.chat_imag);
            chat_time=itemView.findViewById(R.id.chat_time);
            chat_name=itemView.findViewById(R.id.chat_name);
            stat=itemView.findViewById(R.id.stat);
        }
    }
}