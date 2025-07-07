package com.devolper.complaintfyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class chooseAdminActivity extends AppCompatActivity {
    Button btnDeanLogin,btnHODLogin,btnAdminLogin,btnCordinatorLogin;
    Button btnGymAdmin,btnSecurityAdmin,btnCafeAdmin,btnTransportAdmin;
    userAuthentications userAuth = new userAuthentications();
    DatabaseHelper myDb = new DatabaseHelper(this);
    DatabaseReference db;
    ArrayList admin_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_admin);
        setTitle("Admin Login As");
        db= FirebaseDatabase.getInstance().getReference();
        initilize();
        get_admin_list();
        userAuth.setContext(chooseAdminActivity.this);

        if(userAuth.userData() != null){

            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(chooseAdminActivity.this,homeActivity.class);
                startActivity(openLogin);
            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){
                Intent openLogin = new Intent(chooseAdminActivity.this,adminHome.class);
                startActivity(openLogin);
            }
            else{
                try {
                    Intent openLogin = new Intent(chooseAdminActivity.this,staffHome.class);
                    startActivity(openLogin);

                }
                catch (NullPointerException e){
                    FirebaseAuth.getInstance().signOut();
                    Intent openLogin = new Intent(chooseAdminActivity.this,chooseSigninActivity.class);
                    startActivity(openLogin);
                }
            }
        }

        btnDeanLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","dean");
                startActivity(intent);
            }
        });
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","admin");
                startActivity(intent);
            }
        });
        btnHODLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","hod");
                startActivity(intent);
            }
        });
        btnCordinatorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","cordinator");
                startActivity(intent);
            }
        });

        btnGymAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","gymadmin");
                startActivity(intent);
            }
        });
        btnSecurityAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","securityadmin");
                startActivity(intent);
            }
        });
        btnCafeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","cafeadmin");
                startActivity(intent);
            }
        });
        btnTransportAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signinActivity.class);
                intent.putExtra("type","transportadmin");
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
        btnDeanLogin = (Button) findViewById(R.id.btnDeanLogin);
        btnAdminLogin = (Button) findViewById(R.id.btnAdminLogin);
        btnHODLogin = (Button) findViewById(R.id.btnHODLogin);
        btnCordinatorLogin = (Button) findViewById(R.id.btnCordinatorLogin);
        btnGymAdmin = (Button) findViewById(R.id.btnGymAdmin);
        btnSecurityAdmin = (Button) findViewById(R.id.btnSecurityAdmin);
        btnCafeAdmin = (Button) findViewById(R.id.btnCafeAdmin);
        btnTransportAdmin = (Button) findViewById(R.id.btnTransportAdmin);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), chooseSigninActivity.class);
        startActivity(intent);
    }
}
