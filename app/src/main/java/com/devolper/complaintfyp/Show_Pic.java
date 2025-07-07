package com.devolper.complaintfyp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.devolper.complaintfyp.other_staff.SingleComplainStaffView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Show_Pic extends AppCompatActivity {
     String im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__pic);
        im=getIntent().getStringExtra("img");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://complainfyp-7ba04.appspot.com");

        storageRef.child("complaints").child(im).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                ImageView img=(ImageView)findViewById(R.id.img);
                Picasso.with(Show_Pic.this)
                        .load(uri)
                        .placeholder(R.drawable.loading)
                        .into(img);
                //    Toast.makeText(getApplicationContext(),""+uri,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SingleComplainStaffView.class);
        startActivity(intent);
    }
}
