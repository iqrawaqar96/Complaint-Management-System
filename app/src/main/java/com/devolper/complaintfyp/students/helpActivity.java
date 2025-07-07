package com.devolper.complaintfyp.students;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class helpActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    DatabaseHelper myDb = new DatabaseHelper(this);
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
        setContentView(R.layout.activity_help);

        userAuth.setContext(helpActivity.this);

        getUserName();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.actiity_help);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent openLogin = new Intent(helpActivity.this,homeActivity.class);
            startActivity(openLogin);
        }
    }
    private void setNavigation(){
        dl = (DrawerLayout)findViewById(R.id.actiity_help);
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
                        Intent openLogin0 = new Intent(helpActivity.this,profileActivity.class);
                        startActivity(openLogin0);
                        break;
                    case R.id.nav_tips:
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(helpActivity.this,chooseSigninActivity.class);
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
