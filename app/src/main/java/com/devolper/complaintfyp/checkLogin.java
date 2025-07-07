package com.devolper.complaintfyp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.admin.adminHome;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.staffHome;
import com.devolper.complaintfyp.students.addComplanit;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class checkLogin extends Activity {
    String type;
    userAuthentications userAuth = new userAuthentications();
    DatabaseReference db;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String getGender="",getStatus="",getName="",getUsername="",getRegNo="";
    public void setRegNo(String s){
        getRegNo = s;
    }
    public void setStatus(String s){
        getStatus = s;
    }
    public void setUsername(String s){
        getUsername = s;
    }
    public void setGender(String s){
        getGender = s;
    }
    public void setName(String s){
        getName = s;
    }

    public String getStatus(){
        return this.getStatus;
    }
    public String getRegNo(){
        return this.getRegNo;
    }
    public String getUser_name(){
        return this.getUsername;
    }
    public String getName(){
        return this.getName;
    }
    public String getGender(){
        return this.getGender;
    }
    DatabaseHelper myDb = new DatabaseHelper(this);
    ArrayList admin_list;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);

        userAuth.setContext(checkLogin.this);
        db= FirebaseDatabase.getInstance().getReference();
        type = getIntent().getStringExtra("type");

        text =(TextView) findViewById(R.id.text);

       // Toast.makeText(getApplicationContext(), type,Toast.LENGTH_SHORT).show();

        if(userAuth.userData() == null){
            Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        get_admin_list();
        getUserName();




    }
    public void saveData(){
        myDb.emptyOffline();
        myDb.insertProfile(this.getName, userAuth.userEmail(),this.getUsername ,this.getGender,this.getRegNo,this.getStatus,type);
        if(type.equalsIgnoreCase("students")){
            Intent openLogin = new Intent(checkLogin.this,homeActivity.class);
            startActivity(openLogin);
        }
        else if(type.equalsIgnoreCase("admin")){
            Intent openLogin = new Intent(checkLogin.this,adminHome.class);
            startActivity(openLogin);
        }
        else{
            if(admin_list.contains(type)){
                Intent openLogin = new Intent(checkLogin.this,staffHome.class);
                openLogin.putExtra("type",type);
                startActivity(openLogin);
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid user type",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                startActivity(openLogin);
            }
        }
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
    private void getUserName(){
        DatabaseReference myRef = database.getReference("users/" + userAuth.userData() + "/" + type);

        myRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String value = dataSnapshot.getValue(String.class);
                    setName(value);

                    if(value.length() < 3){
                        Toast.makeText(getApplicationContext(),"You Cannot Login As " + type,Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                }
                catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"You Cannot Login As " + type,Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                    startActivity(openLogin);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                setName("");
                Toast.makeText(getApplicationContext(),"You Cannot Login As " + type,Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                startActivity(openLogin);
                return;
            }
        });

        myRef.child("gender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                setGender(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                setGender("");
                return;
            }
        });
        myRef.child("regno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                setRegNo(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                setRegNo("");
                return;
            }
        });
        myRef.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                setUsername(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                setUsername("");
                return;
            }
        });
        myRef.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    String value = dataSnapshot.getValue(String.class);
                    setStatus(value);
                    if(value.toString().equals("0")){
                        Toast.makeText(getApplicationContext(),"Your Account is Not Approved Yet!" ,Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                    else if(value.toString().equals("-1")){
                        Toast.makeText(getApplicationContext(),"Your Account is Deleted By the Admin!" ,Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                    else if(value.toString().equals("2")){
                        Toast.makeText(getApplicationContext(),"Your Account is Rejected By the Admin!" ,Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                    else if(value.toString().equals("1")){
                        Toast.makeText(getApplicationContext(),"Success!" ,Toast.LENGTH_SHORT).show();
                        saveData();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Cannot Login Your Account Now Try again latter!" ,Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(checkLogin.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                }
                catch (NullPointerException e){

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                setStatus("");
                return;
            }
        });

    }
}
