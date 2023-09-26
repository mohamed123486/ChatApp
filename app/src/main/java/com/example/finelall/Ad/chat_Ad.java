package com.example.finelall.Ad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelall.R;
import com.example.finelall.model.chat_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chat_Ad extends RecyclerView.Adapter<chat_Ad.ViewHolder> {
    ArrayList<chat_model>s;

    public chat_Ad(ArrayList<chat_model> s) {
        this.s = s;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (FirebaseAuth.getInstance().getUid().equals(s.get(position).getFrom())){
            holder.resever.setVisibility(View.INVISIBLE);
            holder.sender.setText(s.get(position).getMsg());

        }else {
            holder.sender.setVisibility(View.INVISIBLE);
            holder.resever.setText(s.get(position).getMsg());
        }

    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sender,resever;
        CircularImageView userimg,reseverimg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sender=itemView.findViewById(R.id.sender);
            resever=itemView.findViewById(R.id.resever);
        }
    }
}
