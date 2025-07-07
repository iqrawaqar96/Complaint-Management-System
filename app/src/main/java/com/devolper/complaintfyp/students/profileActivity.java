package com.devolper.complaintfyp.students;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.registerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    ProgressDialog dialog;
    DatabaseHelper myDb = new DatabaseHelper(this);
    TextView sliderUserName,sliderUserEmail,name,gender,username,reg;
    userAuthentications userAuth = new userAuthentications();
    Button changePwd;
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
        setContentView(R.layout.activity_profile);

        userAuth.setContext(profileActivity.this);

        getUserName();

        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        username = (TextView) findViewById(R.id.username);
        reg = (TextView) findViewById(R.id.reg);
        changePwd = (Button) findViewById(R.id.changePwd);

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog("Change Password");
            }
        });

        name.setText(myDb.getProfileData().get(0).toString());
        username.setText(myDb.getProfileData().get(2).toString());
        gender.setText(myDb.getProfileData().get(3).toString());
        reg.setText(myDb.getProfileData().get(4).toString());
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.actiity_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent openLogin = new Intent(profileActivity.this,homeActivity.class);
            startActivity(openLogin);
        }
    }
    private void setNavigation(){
        dl = (DrawerLayout)findViewById(R.id.actiity_profile);
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
                        break;
                    case R.id.nav_tips:
                        Intent openLogin0 = new Intent(profileActivity.this,helpActivity.class);
                        startActivity(openLogin0);
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(profileActivity.this,chooseSigninActivity.class);
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
public int changePassword(String oldpass, final String newPass){
        final FirebaseUser user;
    final int[] flag = {0};
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                //Error
                                flag[0] = 1;
                            }else {
                                flag[0] = 2;
                            }
                        }
                    });
                }else {
                    //Old Password Not Matched
                    flag[0] = 0;
                }
            }
        });
    return  flag[0] ;
    }

    public void setDialog(String topTitle){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_box, null);

        final EditText oldPass = (EditText) dialogView.findViewById(R.id.oldPass);
        final EditText newPass = (EditText) dialogView.findViewById(R.id.newPass);
        Button button_submit = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button_cancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        TextView topTitle1 = (TextView) dialogView.findViewById(R.id.textView);

        topTitle1.setText(topTitle);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pass = oldPass.getText().toString();
                final String newPass0 = newPass.getText().toString();

                if(isPasswordValid(pass)){
                    if(isPasswordValid(newPass0)){
                        final int[] res = {1};
                        dialog= ProgressDialog.show(profileActivity.this, "",
                                "Loading. Please wait...", true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                res[0] = changePassword(pass,newPass0);
                            }
                        },3500);
                        if( res[0] == 0){
                            oldPass.setError("Old Password Does Not Match With Our System");
                        }
                        else if( res[0] == 2){
                            Toast.makeText(getApplicationContext(),"Password Changed Successfully!",Toast.LENGTH_SHORT).show();
                            dialogBuilder.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Cannot Change Password Now Try again Latter",Toast.LENGTH_SHORT).show();
                            dialogBuilder.dismiss();
                        }
                        dialog.dismiss();
                    }
                    else{
                        newPass.setError("Password Must be atleast 8 characters");
                    }
                }
                else{
                    oldPass.setError("Password Must be atleast 8 characters");
                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
