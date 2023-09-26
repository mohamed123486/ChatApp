package com.example.finelall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.finelall.Ad.Fragment_Ad;
import com.example.finelall.Profile.Profile;
import com.example.finelall.Register.LogIn;
import com.example.finelall.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private FirebaseAuth auth;
static HashMap<String,Object> hashMap=new HashMap<>();
boolean issend=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Fragment_Ad ad=new Fragment_Ad(getSupportFragmentManager());
        binding.pager.setAdapter(ad);
        binding.tab.setupWithViewPager(binding.pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:startActivity(new Intent(getApplicationContext(), Profile.class).putExtra("sin",20));break;
            case R.id.Logout:
                Stat("offline");
                auth.signOut();finish();startActivity(new Intent(getApplicationContext(), LogIn.class));

            ;break;
            case R.id.Find:startActivity(new Intent(getApplicationContext(),FindFriends.class));break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!= null) {
            Stat("online");
        }else {
            Stat("offline");
        }
        FirebaseDatabase.getInstance().getReference().child("Request").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d:snapshot.getChildren()){
                    Iterator i=d.getChildren().iterator();
                    String id=(((DataSnapshot)i.next()).getValue(String.class));
                    if (id.equals("Sender")){
                        stopService(new Intent(getApplicationContext(),OneTimeNotificationService.class));
                    }else if(id.equals("Reserve")) {
                        if(issend)
                        startService(new Intent(getApplicationContext(),OneTimeNotificationService.class));
                        issend=false;
                        if (!issend)
                            stopService(new Intent(getApplicationContext(),OneTimeNotificationService.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stat("offline");
    }

    public static void Stat(String stat){
        String time=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        if (stat!=null) {
            hashMap.put("user stat",stat);
            hashMap.put("time",time);
        }
        if (FirebaseAuth.getInstance().getUid()!=null)
        FirebaseDatabase.getInstance().getReference().child("stat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("typ_stat").setValue(hashMap);

   }
}