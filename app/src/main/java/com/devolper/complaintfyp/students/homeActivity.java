package com.devolper.complaintfyp.students;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devolper.complaintfyp.admin.adminHome;
import com.devolper.complaintfyp.noConnection;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.Tokens;
import com.devolper.complaintfyp.objects.Users;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.staffHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class homeActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ArrayList<ComplaintData> progress,resolved,all,dec;
    Button totalCmp,progressComp,resolvedCmp,changePwd;
    FloatingActionButton addCmp;
    TextView sliderUserName,sliderUserEmail;
    userAuthentications userAuth = new userAuthentications();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String getName;
    public void setName(String s){
        getName = s;
    }

    public String getName(){
        return this.getName;
    }
    DatabaseHelper myDb = new DatabaseHelper(this);
    DatabaseReference db;

    @SerializedName("s")
    public String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userAuth.setContext(homeActivity.this);

        getUserName();
        db= FirebaseDatabase.getInstance().getReference();
        initilize();
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(homeActivity.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        else{
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){

            }
            else if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("admin")){
                Intent openLogin = new Intent(homeActivity.this,adminHome.class);
                startActivity(openLogin);
            }
            else{
                String type = myDb.getProfileData().get(6).toString();
                Intent openLogin = new Intent(homeActivity.this,staffHome.class);
                openLogin.putExtra("type",type);
                startActivity(openLogin);
            }
        }
        addCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addComplanit = new Intent(homeActivity.this,complanitCatagory.class);
                startActivity(addComplanit);
            }
        });
        progress=new ArrayList<>();
        resolved=new ArrayList<>();
        all=new ArrayList<>();
        dec=new ArrayList<>();
        get_data();
        //Toast.makeText(getApplicationContext(),myDb.getProfileData().toString(),Toast.LENGTH_SHORT).show();

        SharedPreferences e = getSharedPreferences("token",MODE_PRIVATE);
        String ttt = e.getString("id",null);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + userAuth.userData() + "/students");
        myRef.child("token").setValue(ttt);

        totalCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g=new Gson();
                s=g.toJson(all);
                Intent ii=new Intent(homeActivity.this, ViewAllComplaintsStudent.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });

        progressComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g=new Gson();
                String s=g.toJson(progress);
                Intent ii=new Intent(homeActivity.this, ViewAllComplaintsStudent.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });

        resolvedCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g=new Gson();
                String s=g.toJson(resolved);
                Intent ii=new Intent(homeActivity.this, ViewAllComplaintsStudent.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson g=new Gson();
                String s=g.toJson(dec);
                Intent ii=new Intent(homeActivity.this, ViewAllComplaintsStudent.class);
                ii.putExtra("s",s);
                startActivity(ii);
            }
        });
    }

    public void initilize(){
        totalCmp = (Button) findViewById(R.id.totalCmp);
        progressComp = (Button) findViewById(R.id.progressComp);
        resolvedCmp = (Button) findViewById(R.id.resolvedCmp);
        changePwd = (Button) findViewById(R.id.changePwd);
        addCmp = (FloatingActionButton) findViewById(R.id.addCmp);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.actiity_home);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit_app();
        }
    }
    private void setNavigation(){
        dl = (DrawerLayout)findViewById(R.id.actiity_home);
        t = new ActionBarDrawerToggle(this, dl,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_profile:
                        Intent openLogin0 = new Intent(homeActivity.this,profileActivity.class);
                        startActivity(openLogin0);
                        break;
                    case R.id.nav_tips:
                        Intent openLogin00 = new Intent(homeActivity.this,helpActivity.class);
                        startActivity(openLogin00);
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(homeActivity.this,chooseSigninActivity.class);
                        startActivity(openLogin);
                        break;
                    case R.id.nav_exit:
                        exit_app();
                        break;
                    case R.id.nav_share:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT,"Hey! Download This Amazing Job App For Android");
                        i.putExtra(Intent.EXTRA_TEXT, "\n Click Link Below To Downoad \n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                        startActivity(Intent.createChooser(i, "choose one"));
                        break;
                    case R.id.nav_rate:
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;

            }
        });

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
    public void setNavDetails(){
        sliderUserName = (TextView)findViewById(R.id.sliderUserName);
        sliderUserEmail = (TextView)findViewById(R.id.sliderUserEmail);
        sliderUserEmail.setText(userAuth.userEmail());
        sliderUserName.setText(this.getName);
    }
    private void getUserName(){
        DatabaseReference myRef = database.getReference("users/" + userAuth.userData() + "/students");

        myRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                setName(value);
                setNavigation();
                setNavDetails();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                return;
            }
        });
    }


    private void get_data() {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference();
        db.child("complaints").child(userAuth.userData()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    ComplaintData c = d.getValue(ComplaintData.class);
                    all.add(c);
                    if (c.getStatus().equals("1")) {
                        resolved.add(c);
                    } else if(c.getStatus().equals("0")) {
                        progress.add(c);
                    }
                    else{
                        dec.add(c);
                    }


                }
                if (all.size() >= 0) {
                    progressComp.setText("In Progress " + progress.size());
                    totalCmp.setText("Total Complaints " + all.size());
                    resolvedCmp.setText("Resolved " + resolved.size());
                    changePwd.setText("Declined " + dec.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
