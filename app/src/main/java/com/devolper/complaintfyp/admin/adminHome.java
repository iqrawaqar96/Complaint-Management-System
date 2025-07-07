package com.devolper.complaintfyp.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.noConnection;
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

public class adminHome extends AppCompatActivity {
    userAuthentications userAuth = new userAuthentications();
    Button admin_approve,admin_add,admin_del,admin_logout;
    DatabaseHelper myDb = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        userAuth.setContext(adminHome.this);
        setTitle("Admin Panel");
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(adminHome.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        else{
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(adminHome.this,homeActivity.class);
                startActivity(openLogin);
            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){

            }
            else{
                String type = myDb.getProfileData().get(6).toString();
                Intent openLogin = new Intent(adminHome.this,staffHome.class);
                openLogin.putExtra("type",type);
                startActivity(openLogin);
            }
        }
        initilize();

        admin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent openLogin = new Intent(adminHome.this,chooseSigninActivity.class);
                startActivity(openLogin);
            }
        });
        admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(adminHome.this,AddAccount.class);
                startActivity(openLogin);
            }

        });
        admin_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(adminHome.this,approveAccount.class);
                startActivity(openLogin);
            }
        });
        admin_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(adminHome.this,delAccount.class);
                startActivity(openLogin);
            }
        });
    }

    public void initilize(){
        admin_approve = (Button) findViewById(R.id.admin_approve);
        admin_add = (Button) findViewById(R.id.admin_add);
        admin_del = (Button) findViewById(R.id.admin_del);
        admin_logout = (Button) findViewById(R.id.admin_logout);
    }

    @Override
    public void onBackPressed() {
        show_dialog();
    }

    private void show_dialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Are you sure to logout?");

        // set dialog message
        alertDialogBuilder

                .setCancelable(false)
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                        dialog.cancel();

                    }
                })
                .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(adminHome.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                        dialog.cancel();
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


}
