package com.devolper.complaintfyp.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.SingleComplainStaffView;
import com.devolper.complaintfyp.other_staff.ViewAllComplaintsStaff;
import com.devolper.complaintfyp.registerActivity;
import com.devolper.complaintfyp.students.addComplanit;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAccount extends AppCompatActivity {
    private static String type="dean";
    Button btDean,btnHod,btnOthers,btn_signup;
    ProgressDialog dialog;
    DatabaseReference db;
    userAuthentications userAuth = new userAuthentications();
    EditText input_name,input_username,input_email,input_password;
    DatabaseHelper myDb = new DatabaseHelper(this);
    Spinner input_type;
    ArrayList list;
    String spinner_val;
    TextView adminTypeLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        userAuth.setContext(AddAccount.this);
        setTitle("ADD " + type.toUpperCase() + " Account");
        db= FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();

        if(userAuth.userData() == null){
            Intent openLogin = new Intent(AddAccount.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        else{
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(AddAccount.this,homeActivity.class);
                startActivity(openLogin);
            }
            else{

            }
        }
        initilize();
        getList();
        Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
        adminTypeLabel = (TextView)findViewById(R.id.adminTypeLabel);
        //input_type.setOnItemSelectedListener(this);
        btDean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_type.setVisibility(View.GONE);
                adminTypeLabel.setVisibility(View.GONE);
                type = "dean";
                setTitle("ADD " + type.toUpperCase() + " Account");
                Toast.makeText(getApplicationContext(),"Dean",Toast.LENGTH_SHORT).show();
            }
        });
        btnHod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_type.setVisibility(View.GONE);
                adminTypeLabel.setVisibility(View.GONE);
                type = "hod";
                setTitle("ADD " + type.toUpperCase() + " Account");
                Toast.makeText(getApplicationContext(),"hod",Toast.LENGTH_SHORT).show();
            }
        });
        btnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="others";
                input_type.setVisibility(View.VISIBLE);
                adminTypeLabel.setVisibility(View.VISIBLE);
                setTitle("ADD Staff Account");
                Toast.makeText(getApplicationContext(),"staff",Toast.LENGTH_SHORT).show();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = input_name.getText().toString();
                final String email = input_email.getText().toString();
                final String pass = input_password.getText().toString();
                final String username = input_username.getText().toString();
                int flag = 0;
                final String[] type1 = {null};


                if(!isValidField(name)){
                    flag =1;
                    input_name.setError("Please Enter Valid Name");
                }
                if(!isValidEmail(email)){
                    flag =1;
                    input_email.setError("Please Enter Valid Email");
                }
                if(!isPasswordValid(pass)){
                    flag =1;
                    input_password.setError("Password Must Be between 8 to 15 characters");
                }
                if(!isValidField(username)){
                    flag =1;
                    input_username.setError("Please Enter Valid Username");
                }
                if(flag == 0){
                    dialog= ProgressDialog.show(AddAccount.this, "",
                            "Loading. Please wait...", true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(type.equalsIgnoreCase("others")){
                                type1[0] = input_type.getItemAtPosition(input_type.getSelectedItemPosition()).toString();
                                userAuth.createuser(email,pass,name,"null",username,"null",type1[0],"");
                            }
                            else
                                userAuth.createuser(email,pass,name,"null",username,"null",type,"");
                        }
                    },3500);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Account Created Successfully ",Toast.LENGTH_SHORT).show();
                    Intent inte = new Intent(AddAccount.this,adminHome.class);
                    startActivity(inte);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Povide Correct Information ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initilize(){
        btDean = (Button) findViewById(R.id.btDean);
        btnHod = (Button) findViewById(R.id.btnHod);
        btnOthers = (Button) findViewById(R.id.btnOthers);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        input_name = (EditText) findViewById(R.id.input_name);
        input_username = (EditText) findViewById(R.id.input_username);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        input_type = (Spinner) findViewById(R.id.input_list);
    }
    private void getList() {
        db.child("Admin").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //list=new ArrayList<>();
                for(int i=1;i<=dataSnapshot.getChildrenCount();i++){
                    if(dataSnapshot.child(String.valueOf(i)).getValue().toString().equalsIgnoreCase("dean") || dataSnapshot.child(String.valueOf(i)).getValue().toString().equalsIgnoreCase("hod")){

                    }
                    else{
                        list.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());
                    }

                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddAccount.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                input_type.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    @Override
    public void onBackPressed() {
        Intent openLogin = new Intent(AddAccount.this,adminHome.class);
        startActivity(openLogin);

    }
}
