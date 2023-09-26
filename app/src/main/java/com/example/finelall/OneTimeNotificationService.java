package com.example.finelall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finelall.Fragments.Request;
import com.example.finelall.Profile.Friends_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;


public class OneTimeNotificationService extends Service {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private String name,img;
    Bitmap largeIcon;
    Bitmap smallIconBitmap;
    String s;
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences id=getSharedPreferences("id",MODE_PRIVATE);
        s=id.getString("id","non");
        name=id.getString("name","non");
        FirebaseDatabase.getInstance().getReference().child("User").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name =snapshot.child("Username").getValue(String.class);
                img =snapshot.child("Image").getValue(String.class);
                // استدعاء مكتبة Picasso لتحميل الصورة من URL
                Picasso.get().load(img).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        smallIconBitmap=bitmap;
                        // تم تحميل الصورة بنجاح
                        // يمكنك استخدام الصورة ك Small Icon هنا
                        createNotification(smallIconBitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // فشل تحميل الصورة، يمكنك التعامل مع الخطأ هنا
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // تحضير للتحميل، يمكنك تنفيذ إجراءات إضافية هنا إذا كنت بحاجة إلى ذلك
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // إظهار الخدمة في الـ Foreground باستخدام إشعار
        createNotification(smallIconBitmap);
        // قم بأداء المهام الخاصة بالخدمة هنا

        return START_STICKY;
    }

    private void createNotification(Bitmap smallIconBitmap) {

        // إنشاء بناء الإشعار
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(smallIconBitmap) // تعيين الصورة النموذجية هنا
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_baseline_person_24) // هنا تعيين الأيقونة الصغيرة

                .setContentTitle("Request")
       .setContentText("A friend request has been sent to you ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // عند النقر على الإشعار، قم بفتح نشاط (Activity) معين أو اتخاذ إجراء معين
        Intent intent = new Intent(this, Request.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // عرض الإشعار
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
