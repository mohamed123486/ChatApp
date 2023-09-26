package com.example.finelall.Ad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelall.R;
import com.example.finelall.model.chat_model;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class chat_group_Ad extends RecyclerView.Adapter<chat_group_Ad.ViewHolder>{
    ArrayList<chat_model> s;

    public chat_group_Ad(ArrayList<chat_model> s) {
        this.s = s;
    }

    @NonNull
    @Override
    public chat_group_Ad.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new chat_group_Ad.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false));
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
