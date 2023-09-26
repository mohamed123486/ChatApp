package com.example.finelall.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finelall.Chat;
import com.example.finelall.R;
import com.example.finelall.model.FindFriends_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class Contctes extends Fragment {
RecyclerView res;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contctes, container, false);
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
        FirebaseRecyclerOptions<FindFriends_model>model=new FirebaseRecyclerOptions.Builder<FindFriends_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contacts").child(FirebaseAuth.getInstance().getUid()), FindFriends_model.class)
                .build();
        FirebaseRecyclerAdapter<FindFriends_model,Ad>adapter=new FirebaseRecyclerAdapter<FindFriends_model, Ad>(model) {
            @Override
            protected void onBindViewHolder(@NonNull Ad holder, int position, @NonNull FindFriends_model model) {
                String id=getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference().child("User").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String name=snapshot.child("Username").getValue(String.class);
                            String img=snapshot.child("Image").getValue(String.class);
                            holder.contactes_name.setText(name);
                            Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(holder.contactes_img);
                            holder.Send_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), Chat.class).putExtra("id",id));
                                }
                            });                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public Ad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Ad(LayoutInflater.from(parent.getContext()).inflate(R.layout.contactes_item,parent,false));
            }
        };
        res.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }
    class Ad extends RecyclerView.ViewHolder{
        CircularImageView contactes_img;
        TextView contactes_name;
        AppCompatButton Send_msg;
        public Ad(@NonNull View itemView) {
            super(itemView);
            contactes_img=itemView.findViewById(R.id.contactes_imag);
            contactes_name=itemView.findViewById(R.id.contactes_name);
            Send_msg=itemView.findViewById(R.id.send_msg);
        }
    }
}