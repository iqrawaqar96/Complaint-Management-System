package com.devolper.complaintfyp.other_staff;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.admin.adminHome;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.noConnection;
import com.devolper.complaintfyp.objects.Complaint;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.students.ViewAllComplaintsStudent;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class staffHome extends AppCompatActivity {
    userAuthentications userAuth = new userAuthentications();
    DatabaseHelper myDb = new DatabaseHelper(this);
    DatabaseReference db;
    ArrayList admin_list;
    String type;
    ArrayList<ComplaintData> all;
    ArrayList<ComplaintData> total;
    Button totalCmp,btnLogout,viewAll;

    ArrayList<String> cordinator_list = new ArrayList<>();
    ArrayList<String> gym_list = new ArrayList<>();
    ArrayList<String> cafe_list = new ArrayList<>();
    ArrayList<String> security_list = new ArrayList<>();
    ArrayList<String> transport_list = new ArrayList<>();
    ArrayList<String> page_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        db= FirebaseDatabase.getInstance().getReference();
        try{
            type = getIntent().getStringExtra("type");
        }catch (NullPointerException e){
            type = myDb.getProfileData().get(6).toString();
        }
        all = new ArrayList<>();
        total = new ArrayList<>();
        userAuth.setContext(staffHome.this);
        get_admin_list();

        try{
            type = getIntent().getStringExtra("type");
            setTitle(type.toUpperCase() + " Panel");
        }catch (NullPointerException e){
            type = myDb.getProfileData().get(6).toString();
            setTitle(type.toUpperCase() + " Panel");
        }


        cordinator_list.add("dept");
        cordinator_list.add("other");
        gym_list.add("gym");
        cafe_list.add("cafe");
        security_list.add("security");
        transport_list.add("transport");

        if(type.equalsIgnoreCase("cordinator")){
            page_list = cordinator_list;
        }
        else if(type.equalsIgnoreCase("gymAdmin")){
            page_list = gym_list;
        }
        else if(type.equalsIgnoreCase("securityAdmin")){
            page_list = security_list;
        }
        else if(type.equalsIgnoreCase("cafeAdmin")){
            page_list = cafe_list;
        }
        else if(type.equalsIgnoreCase("transportAdmin")){
            page_list = transport_list;
        }
        else{

        }
        initilize();
        get_data();
        if(type.equalsIgnoreCase("dean")) {
            viewAll.setVisibility(View.VISIBLE);
        }
        else{
            viewAll.setVisibility(View.GONE);
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor e = getSharedPreferences("admin",MODE_PRIVATE).edit();
                e.remove("type");
                e.clear();
                e.commit();
                FirebaseAuth.getInstance().signOut();
                Intent openLogin = new Intent(staffHome.this,chooseSigninActivity.class);
                startActivity(openLogin);
            }
        });

        totalCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equalsIgnoreCase("dean")){

                    SharedPreferences.Editor e = getSharedPreferences("admin",MODE_PRIVATE).edit();
                    e.remove("type");
                    e.clear();
                    e.commit();
                }
                Gson g=new Gson();
                String s=g.toJson(all);
                Intent ii=new Intent(staffHome.this, ViewAllComplaintsStaff.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equalsIgnoreCase("dean")){

                    SharedPreferences.Editor e = getSharedPreferences("admin",MODE_PRIVATE).edit();
                    e.putString("type","dean");
                    e.apply();
                }
                Gson g=new Gson();
                String s=g.toJson(total);
                Intent ii=new Intent(staffHome.this, ViewAllComplaintsStaff.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });
    }
    public void initilize(){
        totalCmp = (Button) findViewById(R.id.totalCmp);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        viewAll = (Button) findViewById(R.id.viewAll);
    }
    private void get_admin_list() {
        db.child("Admin").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                admin_list=new ArrayList<>();
                for(int i=1;i<=dataSnapshot.getChildrenCount();i++){
                    admin_list.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());
                }
                checkLogin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void checkLogin(){
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(staffHome.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        else{
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(staffHome.this,homeActivity.class);
                startActivity(openLogin);
            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){
                Intent openLogin = new Intent(staffHome.this,adminHome.class);
                startActivity(openLogin);
            }
            else{
                String type1 = myDb.getProfileData().get(6).toString();
                if(type1.equalsIgnoreCase(type)){
                    if(!admin_list.contains(type)){
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(staffHome.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                    }
                }
                else{
                    FirebaseAuth.getInstance().signOut();
                    Intent openLogin = new Intent(staffHome.this,chooseSigninActivity.class);
                    startActivity(openLogin);
                }
            }
        }
    }
    private void get_data() {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference();
        db.child("complaints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    for (DataSnapshot d : dd.getChildren()) {
                        String uID = dd.getKey();
                        ComplaintData c = d.getValue(ComplaintData.class);
                        //   all.add(c);
                        c.setUserID(uID);
                        total.add(c);
                        if(type.equals("hod")){
                            if ((cal_days(c.getDate()) > 7 && cal_days(c.getDate()) < 15) && c.getStatus().equals("0")) {
                                all.add(c);
                            }
                        }
                        else if(type.equals("dean")){
                            if ((cal_days(c.getDate())  > 14) && c.getStatus().equals("0")) {
                                all.add(c);
                            }
                        }
                        else{
                            if ((cal_days(c.getDate())  < 8 && c.getStatus().equals("0")) && page_list.contains(c.getCat())) {
                                all.add(c);
                            }
                        }
                    }
                }
                if (all.size() > -1) {
                    totalCmp.setText("Complaints " + all.size());
                }
                if(total.size() >= 0){
                    viewAll.setText("View All " + total.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public long cal_days(String starting){
        long days=0;
        Date cc = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String end = df.format(cc);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date s_date = sdf.parse(starting);
            Date e_date = sdf.parse(end);
            days = e_date.getTime() - s_date.getTime();
            days= TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
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
                        Intent openLogin = new Intent(staffHome.this,chooseSigninActivity.class);
                        startActivity(openLogin);

                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

}
