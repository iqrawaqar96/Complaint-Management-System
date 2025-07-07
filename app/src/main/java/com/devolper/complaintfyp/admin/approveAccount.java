package com.devolper.complaintfyp.admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.adapters.User_List_Adapter;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.Users;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.SingleComplainStaffView;
import com.devolper.complaintfyp.other_staff.ViewAllComplaintsStaff;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class approveAccount extends AppCompatActivity {
    userAuthentications userAuth = new userAuthentications();
    DatabaseHelper myDb = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_account);
        userAuth.setContext(approveAccount.this);
        setTitle("Approve Account");
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(approveAccount.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        else{
            if(myDb.getProfileData().get(6).toString().equalsIgnoreCase("students")){
                Intent openLogin = new Intent(approveAccount.this,homeActivity.class);
                startActivity(openLogin);
            }
            else{

            }
        }
        get_data();
    }

    private void get_data() {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference();
        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Users> users=new ArrayList<>();

                for(DataSnapshot d:dataSnapshot.getChildren()){
                    for(DataSnapshot s:d.getChildren()){
                        String ss = d.getKey();
                        if(s.getKey().equalsIgnoreCase("students")){
                            Users us=s.getValue(Users.class);
                            try{
                                us.setKey(ss);

                                if(us.getStatus().equals("0")) {
                                    users.add(us);
                                }
                            }
                            catch (NullPointerException e){

                            }
                        }
                    }


                }
                User_List_Adapter adapter=new User_List_Adapter(getApplicationContext(),users);
                ListView listView=(ListView)findViewById(R.id.list);
                listView.setAdapter(adapter);
                if(users.size()==0){
                    TextView t=(TextView)findViewById(R.id.t);
                    t.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent openLogin = new Intent(approveAccount.this,adminHome.class);
        startActivity(openLogin);

    }
}
