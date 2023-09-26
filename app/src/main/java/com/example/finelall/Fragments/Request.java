package com.example.finelall.Fragments;

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

import com.example.finelall.Manger.mange;
import com.example.finelall.R;
import com.example.finelall.model.FindFriends_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class Request extends Fragment {
RecyclerView res;
    String img,user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_request, container, false);
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
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getUid()),FindFriends_model.class)
                .build();
        FirebaseRecyclerAdapter<FindFriends_model,Ad>adapter=new FirebaseRecyclerAdapter<FindFriends_model, Ad>(model) {
            @Override
            protected void onBindViewHolder(@NonNull Ad holder, int position, @NonNull FindFriends_model model) {
                holder.request_accept.setVisibility(View.GONE);
                String  id=getRef(position).getKey();
                DatabaseReference date=getRef(position).getRef();
                date.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String type=snapshot.child("Request typ").getValue(String.class);
                        if (type!=null)
                            FirebaseDatabase.getInstance().getReference().child("User").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (type.equals("Sender")){
                                        img=snapshot.child("Image").getValue(String.class);
                                        user=snapshot.child("Username").getValue(String.class);
                                        Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(holder.request_img);
                                        holder.request_name.setText(user);
                                        holder.request_reject.setText("Cancel Request");


                                    }else if(type.equals("Reserve")){
                                        img=snapshot.child("Image").getValue(String.class);
                                        user=snapshot.child("Username").getValue(String.class);
                                        Picasso.get().load(img).placeholder(R.drawable.ic_baseline_person_24).into(holder.request_img);
                                        holder.request_name.setText(user);
                                        holder.request_accept.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        holder.request_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Accept(id);
                            }
                        });
                        holder.request_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Cancel_Reject(id);
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
                return new Ad(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item,parent,false));
            }
        };
        res.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
    class Ad extends RecyclerView.ViewHolder{
        CircularImageView request_img;
        TextView request_name;
        AppCompatButton request_reject,request_accept;
        public Ad(@NonNull View itemView) {
            super(itemView);
            request_img=itemView.findViewById(R.id.request_imag);
            request_name=itemView.findViewById(R.id.request_name);
            request_accept=itemView.findViewById(R.id.request_accept);
            request_reject=itemView.findViewById(R.id.request_reject);


        }
    }
    private void Accept(String  id){
        FirebaseDatabase.getInstance().getReference().child("Contacts").child(FirebaseAuth.getInstance().getUid()).child(id).child("type").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("Contacts").child(id).child(FirebaseAuth.getInstance().getUid()).child("type").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference().child("Request").child(id).child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });
    }
    private void Cancel_Reject(String id){
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Request").child(id).child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mange.request="new";

                        }
                    });
                }
            }
        });

    }

}