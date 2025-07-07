package com.devolper.complaintfyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devolper.complaintfyp.admin.adminHome;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.staffHome;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chooseSigninActivity extends AppCompatActivity {
    Button btnStdLogin,btnAdminLogin,btnStdRegister;
    userAuthentications userAuth = new userAuthentications();
    DatabaseHelper myDb = new DatabaseHelper(this);
    DatabaseReference db;
    ArrayList admin_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_signin);
        initilize();
        db= FirebaseDatabase.getInstance().getReference();
        setTitle("Login As");
        userAuth.setContext(chooseSigninActivity.this);
        get_admin_list();
        if(userAuth.userData() != null){

            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(chooseSigninActivity.this,homeActivity.class);
                startActivity(openLogin);
            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){
                Intent openLogin = new Intent(chooseSigninActivity.this,adminHome.class);
                startActivity(openLogin);
            }
            else{
                try {
                    Intent openLogin = new Intent(chooseSigninActivity.this,staffHome.class);
                    startActivity(openLogin);

                }
                catch (NullPointerException e){
                    FirebaseAuth.getInstance().signOut();
                    Intent openLogin = new Intent(chooseSigninActivity.this,chooseSigninActivity.class);
                    startActivity(openLogin);
                }
            }
        }

        btnStdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","students");
                startActivity(intent);
            }
        });
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), chooseAdminActivity.class);
                startActivity(intent);
            }
        });
        btnStdRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
                startActivity(intent);
            }
        });
    }
    private void get_admin_list() {
        db.child("Admin").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                admin_list=new ArrayList<>();
                for(int i=1;i<=dataSnapshot.getChildrenCount();i++){
                    admin_list.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void initilize(){
        btnStdLogin = (Button) findViewById(R.id.btnStdLogin);
        btnAdminLogin = (Button) findViewById(R.id.btnAdminLogin);
        btnStdRegister = (Button) findViewById(R.id.btnStdRegister);
    }
    @Override
    public void onBackPressed() {
        exit_app();
    }
    private void exit_app() {
        new AlertDialog.Builder(this,R.style.MyDialogTheme).setIcon(android.R.drawable.btn_star_big_on).setTitle("Rate US")
                .setMessage("We're glad you're enjoying using our app! Would you mind giving us a review in the play store? it really help us out! Thanks For Your Support :-) \n Have a Good Day")
                .setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNeutralButton("Exit!",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).setPositiveButton("Rate US", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }).show();
    }

}
