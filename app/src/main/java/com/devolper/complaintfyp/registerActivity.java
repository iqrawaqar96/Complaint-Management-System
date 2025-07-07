package com.devolper.complaintfyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {
    EditText input_name,input_password,input_gender,input_username,input_regno,input_email;
    Button btn_signup;
    ProgressDialog dialog;
    userAuthentications userAuth = new userAuthentications();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Create New Account");

        initilize();

        userAuth.setContext(registerActivity.this);

        if(userAuth.userData() != null){
            Intent openLogin = new Intent(registerActivity.this,homeActivity.class);
            startActivity(openLogin);
        }
        SharedPreferences e = getSharedPreferences("token",MODE_PRIVATE);
        final String ttt = e.getString("id",null);

       // Toast.makeText(getApplicationContext(),ttt,Toast.LENGTH_SHORT).show();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator()){
                    String name = input_name.getText().toString();
                    String email = input_email.getText().toString();
                    String pass = input_password.getText().toString();
                    String gender = input_gender.getText().toString();
                    String username = input_username.getText().toString();
                    String regno = input_regno.getText().toString();

                    boolean isCreated = userAuth.createuser(email,pass,name,gender,username,regno,"students",ttt);

                    dialog= ProgressDialog.show(registerActivity.this, "",
                            "Loading. Please wait...", true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseAuth.getInstance().signOut();
                            Intent openLogin = new Intent(registerActivity.this,chooseSigninActivity.class);
                            startActivity(openLogin);
                        }
                    },3500);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Provide Correct Information",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initilize(){
        input_name = (EditText) findViewById(R.id.input_name);
        input_password = (EditText) findViewById(R.id.input_password);
        input_gender = (EditText) findViewById(R.id.input_gender);
        input_username = (EditText) findViewById(R.id.input_username);
        input_regno = (EditText) findViewById(R.id.input_regno);
        input_email = (EditText) findViewById(R.id.input_email);
        btn_signup = (Button) findViewById(R.id.btn_signup);
    }
    public boolean validator(){
        String name = input_name.getText().toString();
        String email = input_email.getText().toString();
        String pass = input_password.getText().toString();
        String gender = input_gender.getText().toString();
        String username = input_username.getText().toString();
        String regno = input_regno.getText().toString();
        int flag =0 ;
        String error = null;
        if(!isValidField(name)){
            input_name.setError("Please Enter Valid Name");
            return false;
        }
        if(!isValidGender(gender)){
            input_gender.setError("Please Enter Valid Gender");
            return false;
        }
        if(!isValidField(username)){
            input_username.setError("Please Enter user Name");
            return false;
        }
        if(!isValidEmail(email)){
            input_email.setError("Please Enter Valid Email");
            return false;
        }
        if(!isPasswordValid(pass)){
            input_password.setError("Password Must be atleast 8 character");
            return false;
        }
        if(!isValidField(regno)){
            input_regno.setError("Please Enter Valid Registration Number");
            return false;
        }
        return true;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches() && target.toString().endsWith("iiu.edu.pk"));
    }
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }
    public static boolean isValidField(String inp) {
        //TODO: Replace this with your own logic
        return inp.length() > 5;
    }
    public static boolean isValidGender(String inp) {
        //TODO: Replace this with your own logic
        if(inp.equalsIgnoreCase("male") || inp.equalsIgnoreCase("female"))
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), chooseSigninActivity.class);
        startActivity(intent);
    }
}
