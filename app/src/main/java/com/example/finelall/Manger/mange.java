package com.example.finelall.Manger;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finelall.Profile.Friends_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mange {
   static AppCompatButton add;
 public static   String  request="new";
 public static   DatabaseReference database;
   public static String  Request="Request";
  static private FirebaseAuth auth;
  AppCompatButton accept;
   public mange(String userid, AppCompatButton add,AppCompatButton accept){
       this.add=add;
       this.accept=accept;
       auth=FirebaseAuth.getInstance();
       database= FirebaseDatabase.getInstance().getReference();
       getdata(userid);
       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (request.equals("new")){
                   SendRequest(userid);

               }else if(request.equals("Cancel")){
                   CancelRequest(userid);
               }else if (request.equals("unfriend")){
                   UnFriend(userid);
                   CancelRequest(userid);
               }else if (request.equals("Reject")){
                   add.setText("Send Request");
                   CancelRequest(userid);
                   accept.setVisibility(View.GONE);
                   request="new";
               }
           }
       });


   }


    private void CancelRequest(String userid) {
        database.child(Request).child(auth.getUid()).child(userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    database.child(Request).child(userid).child(auth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                request="new";
                                add.setText("Send Request");
                                FirebaseDatabase.getInstance().getReference().child("Nonfiction").child(FirebaseAuth.getInstance().getUid()).child("Send_to").child(userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                })
                                ;
                            }
                        }
                    });
                }

            }
        });
    }

    private void SendRequest(String  userid){
       database.child(Request).child(auth.getUid()).child(userid).child("Request typ").setValue("Sender").addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   database.child(Request).child(userid).child(auth.getUid()).child("Request typ").setValue("Reserve").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               request="Cancel";
                               add.setText("Cancel Request");
                               FirebaseDatabase.getInstance().getReference().child("Nonfiction").child(FirebaseAuth.getInstance().getUid()).child("Send_to").child(userid).setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {

                                   }
                               })
                               ;
                           }
                       }
                   });
               }

           }
       });

    }
    private void getdata(String userid){
       if (database!=null)
        database.child(mange.Request).child(FirebaseAuth.getInstance().getUid()).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String id=snapshot.child("Request typ").getValue(String.class);
                    if (id.equals("Sender")){
                        add.setText("Cancel Request");
                        request="Cancel";

                    }else if(id.equals("Reserve")){
                        add.setText("Reject Request");
                        accept.setVisibility(View.VISIBLE);
                        request="Reject";
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Accept(userid);
                                request="unfriend";

                            }
                        });


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("Contacts").child(auth.getUid()).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String un=snapshot.child("type").getValue(String.class);
                    if (un.equals("Saved")){
                        accept.setVisibility(View.GONE);
                        add.setText("Un Friend");
                        request="unfriend";
                        database.child(Request).child(auth.getUid()).child(userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.child(Request).child(userid).child(auth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void Accept(String userid){
       database.child("Contacts").child(auth.getUid()).child(userid).child("type").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               database.child("Contacts").child(userid).child(auth.getUid()).child("type").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           accept.setVisibility(View.GONE);
                           add.setText("Un Friend");
                           request="unfriend";
                       }
                   }
               });
           }
       });

    }
    private void UnFriend(String userid){
        database.child("Contacts").child(auth.getUid()).child(userid).child("type").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                database.child("Contacts").child(userid).child(auth.getUid()).child("type").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            request="new";
                        }
                    }
                });
            }
        });

    }
}
