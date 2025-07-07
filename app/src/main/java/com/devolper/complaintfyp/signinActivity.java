package com.devolper.complaintfyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.Notifications.MyFirebaseInstanceService;
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

public class signinActivity extends AppCompatActivity {
    EditText input_email,input_password;
    Button btn_login;
    TextView link_signup;
    ImageView login_image;
    String type;
    ProgressDialog dialog;
    userAuthentications userAuth = new userAuthentications();
    DatabaseHelper myDb = new DatabaseHelper(this);
    DatabaseReference db;
    ArrayList admin_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        type = getIntent().getStringExtra("type");
        db= FirebaseDatabase.getInstance().getReference();
        initilize();
        get_admin_list();
        userAuth.setContext(signinActivity.this);

        if(userAuth.userData() != null){
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(signinActivity.this,homeActivity.class);
                startActivity(openLogin);
            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){
                Intent openLogin = new Intent(signinActivity.this,adminHome.class);
                startActivity(openLogin);
            }
            else{
                try {
                    if(admin_list.contains(myDb.getProfileData().get(6))){
                        Intent openLogin = new Intent(signinActivity.this,staffHome.class);
                        startActivity(openLogin);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid user type",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(signinActivity.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                }
                catch (NullPointerException e){
                    FirebaseAuth.getInstance().signOut();
                    Intent openLogin = new Intent(signinActivity.this,chooseSigninActivity.class);
                    startActivity(openLogin);
                }
            }
        }
        /*SharedPreferences e = getSharedPreferences("token",MODE_PRIVATE);
        String ttt = e.getString("id",null);

        Toast.makeText(getApplicationContext(),ttt,Toast.LENGTH_SHORT).show();*/

        if(!isOnline()){
            userAuth.internetError();
        }
        if(type.isEmpty()){
            Intent openHome = new Intent(signinActivity.this,chooseSigninActivity.class);
            startActivity(openHome);
        }
        else{
            setTitle(type.toUpperCase() + " LOG IN");
        }


        if(type.equalsIgnoreCase("students")){
            login_image.setVisibility(View.VISIBLE);
        }
        else{
            login_image.setVisibility(View.GONE);
            link_signup.setVisibility(View.GONE);
        }

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openHome = new Intent(signinActivity.this,registerActivity.class);
                startActivity(openHome);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = input_email.getText().toString();
                final String pass = input_password.getText().toString();
                if(registerActivity.isValidEmail(email)){
                    if(registerActivity.isPasswordValid(pass)){
                        int flag = 0;
                        SharedPreferences e = getSharedPreferences("token",MODE_PRIVATE);
                        final String ttt = e.getString("id",null);
                        userAuth.signin(email,pass,ttt,type);
                        dialog= ProgressDialog.show(signinActivity.this, "",
                                "Loading. Please wait...", true);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(userAuth.userData() != null){
                                    Intent openLogin = new Intent(signinActivity.this,checkLogin.class);
                                    openLogin.putExtra("type",type);
                                    startActivity(openLogin);
                                }
                                else{
                                    input_password.setError("Password is invalid / Try again latter!");
                                    dialog.dismiss();
                                }
                            }
                        },2500);



                    }
                    else{
                        input_password.setError("Password Must Be Atleast 8 Characters");
                    }
                }
                else{
                    input_email.setError("Invalid Email Address");
                }
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
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        login_image = (ImageView) findViewById(R.id.login_image);

    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}
