package com.devolper.complaintfyp.students;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class complanitCatagory extends AppCompatActivity {
    Button btnCmpDept,btnCmpCafe,btnCmpTransport,btnCmpGym,btnCmpSecurity,btnCmpOther;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complanit_catagory);

        initilize();
        userAuth.setContext(complanitCatagory.this);

        getUserName();
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(complanitCatagory.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        btnCmpCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","cafe");
                startActivity(submitComplant);
            }
        });

        btnCmpDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","dept");
                startActivity(submitComplant);
            }
        });

        btnCmpTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","transport");
                startActivity(submitComplant);
            }
        });

        btnCmpGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","gym");
                startActivity(submitComplant);
            }
        });


        btnCmpSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","security");
                startActivity(submitComplant);
            }
        });

        btnCmpOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitComplant = new Intent(complanitCatagory.this,addComplanit.class);
                submitComplant.putExtra("type","other");
                startActivity(submitComplant);
            }
        });
    }

    public void initilize(){
        btnCmpDept = (Button) findViewById(R.id.btnCmpDept);
        btnCmpCafe = (Button) findViewById(R.id.btnCmpCafe);
        btnCmpTransport = (Button) findViewById(R.id.btnCmpTransport);
        btnCmpGym = (Button) findViewById(R.id.btnCmpGym);
        btnCmpSecurity = (Button) findViewById(R.id.btnCmpSecurity);
        btnCmpOther = (Button) findViewById(R.id.btnCmpOther);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.actiity_complanit_catagory);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent openLogin = new Intent(complanitCatagory.this,homeActivity.class);
            startActivity(openLogin);
        }
    }
    private void setNavigation(){
        dl = (DrawerLayout)findViewById(R.id.actiity_complanit_catagory);
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
                        Intent openLogin0 = new Intent(complanitCatagory.this,profileActivity.class);
                        startActivity(openLogin0);
                        break;
                    case R.id.nav_tips:
                        Intent openLogin00 = new Intent(complanitCatagory.this,helpActivity.class);
                        startActivity(openLogin00);
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(complanitCatagory.this,chooseSigninActivity.class);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
