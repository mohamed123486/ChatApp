package com.example.finelall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finelall.Profile.Friends_Profile;
import com.example.finelall.databinding.ActivityFindFriendsBinding;
import com.example.finelall.model.FindFriends_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class FindFriends extends AppCompatActivity {
ActivityFindFriendsBinding binding;
private DatabaseReference databaseReference;
private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFindFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        binding.res.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<FindFriends_model> options =new FirebaseRecyclerOptions.Builder<FindFriends_model>()
                .setQuery(databaseReference.child("User"),FindFriends_model.class)
                .build();
        FirebaseRecyclerAdapter<FindFriends_model,Ad>adapter=new FirebaseRecyclerAdapter<FindFriends_model, Ad>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Ad holder, int position, @NonNull FindFriends_model model) {
                String  userid=getRef(position).getKey();
                if (userid.equals(auth.getUid())){
                    holder.name.setVisibility(View.GONE);
                    holder.img.setVisibility(View.GONE);
                    holder.email.setVisibility(View.GONE);
                }
                holder.email.setText(model.getEmail());
                holder.name.setText(model.getUsername());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_baseline_person_24).into(holder.img);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences s=getSharedPreferences("id",MODE_PRIVATE);
                        SharedPreferences.Editor e=s.edit();
                        e.putString("id",userid);
                        e.putString("name",model.getUsername());
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), Friends_Profile.class).putExtra("model",model).putExtra("id",userid));
                    }
                });


            }

            @NonNull
            @Override
            public Ad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Ad(LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,parent,false));
            }
        };
        binding.res.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
    private class Ad extends RecyclerView.ViewHolder{
        CircularImageView img;
        TextView name, email;
        public Ad(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
        }
    }
}