package com.example.finelall.Ad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finelall.Group_Chat;
import com.example.finelall.R;
import com.example.finelall.model.group_model;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class group_Ad extends RecyclerView.Adapter<group_Ad.ViewHolder> {
    ArrayList<group_model>ll;

    public group_Ad(ArrayList<group_model> ll){
        this.ll=ll;
    }
    @NonNull
    @Override
    public group_Ad.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull group_Ad.ViewHolder holder, int position) {
        group_model m=ll.get(position);
        holder.item_name.setText(m.getGroupName());
        Picasso.get().load(m.getImage()).placeholder(R.drawable.ic_baseline_person_24).into(holder.item_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("PLease Enter Room Password ");
                EditText e=new EditText(holder.itemView.getContext());
                builder.setView(e);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String p=e.getText().toString();
                        if (p.equals(m.getPassword())){
                            Toast.makeText(holder.itemView.getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                            holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), Group_Chat.class).putExtra("name",m.getGroupName()));
                        }else {
                            Toast.makeText(holder.itemView.getContext(), "Pleas Enter right Password ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ll.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView item_img;
        TextView item_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img=itemView.findViewById(R.id.item_img);
            item_name=itemView.findViewById(R.id.item_name);
        }
        public  void pasDilog(String pas){


        }
    }

}
