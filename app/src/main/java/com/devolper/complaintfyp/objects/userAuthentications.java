package com.devolper.complaintfyp.objects;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by waqas ahmed on 4/9/2019.
 */

public class userAuthentications extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static boolean isLogin,isRegister,isSignin;
    private Context context;

    static  int size;

    public void setContext(Context ctx){
        this.context = ctx;
    }
    public String test(){
        return "Hello World";
    }
    public void userAuthentications(){
        this.mAuth = FirebaseAuth.getInstance();
    }
    public boolean checkLogin(){
        userAuthentications();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    isLogin = true;
                } else {
                    isLogin = false;
                }
                // ...
            }
        };
        return  isLogin;
    }
    public String userData() {
        userAuthentications();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        String uid = null;
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }

        return uid;
    }

    public static void setSize(int s){
        size = s;
    }
    public boolean  createuser(String email, String pass, final String name, final String gender, final String username, final String regno, final String type, final String token){
        userAuthentications();

        mAuth.createUserWithEmailAndPassword(email, pass)
        .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isRegister= true;

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/" + userData() + "/" + type);
                if(type.equals("students")){
                    myRef.child("token").setValue(token);
                }
                myRef.child("name").setValue(name);
                myRef.child("username").setValue(username);
                myRef.child("gender").setValue(gender);
                myRef.child("regno").setValue(regno);
                if(type .equalsIgnoreCase("students"))
                 myRef.child("status").setValue("0");
                else
                    myRef.child("status").setValue("1");
                if (!task.isSuccessful()) {
                    isRegister= false;
                }
            }
        });

        return isRegister;
    }
    public boolean signin(String email, String pass, final String token, final String type){
        userAuthentications();
    mAuth.signInWithEmailAndPassword(email, pass)
        .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isSignin = true;
                if (!task.isSuccessful()) {
                    isSignin = false;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/" + userData() + "/" + type);
                myRef.child("token").setValue(token);

                // ...
            }
        });

        return isSignin;
    }


    public String userEmail() {
        userAuthentications();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        String uid = null;
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getEmail();
        }

        return uid;
    }

    @Override
    public void onStart() {
        userAuthentications();
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        userAuthentications();
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void internetError(){
        new AlertDialog.Builder(context)
                .setMessage("No Internet Connection Found!!!")
                .show();
    }


}


