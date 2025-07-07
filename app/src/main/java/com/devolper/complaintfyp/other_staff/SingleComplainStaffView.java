package com.devolper.complaintfyp.other_staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.Notifications.MyFirebaseInstanceService;
import com.devolper.complaintfyp.PlayVideo;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.Show_Pic;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.students.SingleComplainStudentView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class SingleComplainStaffView extends AppCompatActivity {
    ComplaintData complaint;
    TextView user;
    DatabaseReference db;
    EditText remarks;
    Button saveRemarks,resolve,dec;
    String type,ttt=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseHelper myDb = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_complain_staff_view);

        String s=getIntent().getStringExtra("s");
        db= FirebaseDatabase.getInstance().getReference();
        Gson gson = new Gson();
        complaint=gson.fromJson(s,ComplaintData.class);


        Button play=(Button)findViewById(R.id.play);
        Button img=(Button)findViewById(R.id.img);
        dec=(Button)findViewById(R.id.dec);


        type = myDb.getProfileData().get(6).toString();
        if(type.equalsIgnoreCase("dean")){
            SharedPreferences e = getSharedPreferences("admin",MODE_PRIVATE);
            ttt = e.getString("type",null);
        }



        saveRemarks=(Button)findViewById(R.id.saveRemarks);
        resolve=(Button)findViewById(R.id.resolve);

         remarks=(EditText)findViewById(R.id.remarks);

        if(!complaint.getVideo().isEmpty()){
            play.setVisibility(View.VISIBLE);
        }

        TextView detail=(TextView)findViewById(R.id.detail);
        detail.setText("Details : "  + complaint.getDetail());
        TextView category=(TextView)findViewById(R.id.category);
        category.setText("Catagory : " + complaint.getCat() + "\r\n\r\n" + "Sub Catagory : " + complaint.getSubcatagory());
        TextView date=(TextView)findViewById(R.id.date);
        date.setText("Date : " + complaint.getDate());
        try {
            if(ttt.equalsIgnoreCase("dean")){
                resolve.setVisibility(View.GONE);
                dec.setVisibility(View.GONE);
                remarks.setVisibility(View.GONE);
                saveRemarks.setVisibility(View.GONE);
            }
            else{
                resolve.setVisibility(View.VISIBLE);
                dec.setVisibility(View.VISIBLE);
                remarks.setVisibility(View.VISIBLE);
                saveRemarks.setVisibility(View.VISIBLE);
            }
        }
        catch (NullPointerException e0){
            resolve.setVisibility(View.VISIBLE);
            remarks.setVisibility(View.VISIBLE);
            dec.setVisibility(View.VISIBLE);
            saveRemarks.setVisibility(View.VISIBLE);
        }
        user=(TextView)findViewById(R.id.user);
        if(complaint.getIdentity().equalsIgnoreCase("no")){
            getUserName();
        }

        saveRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String remark = remarks.getText().toString();
                if(remark.length() < 5){
                    remarks.setError("Too Short Remarks!");
                }
                else{
                    saveRemark(remark);
                }
            }
        });

        resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("complaints/" + complaint.getUserID() + "/" + complaint.getKey());
                myRef.child("status").setValue("1");
                Toast.makeText(getApplicationContext(),"This Complaint Marked as resolved",Toast.LENGTH_SHORT).show();
                getToken("approve");

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("complaints/" + complaint.getUserID() + "/" + complaint.getKey());
                myRef.child("status").setValue("2");
                Toast.makeText(getApplicationContext(),"This Complaint Marked as Declined",Toast.LENGTH_SHORT).show();
                getToken("declined");
            }
        });
    }

    private void saveRemark(String remark) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("complaints/" + complaint.getUserID() + "/" + complaint.getKey());
        myRef.child("remarks").setValue(remark);
        Toast.makeText(getApplicationContext(),"Remarks Added Successfully",Toast.LENGTH_SHORT).show();
    }

    public void play(View view) {
        if(complaint.getVideo().isEmpty()){
            Toast.makeText(getApplicationContext(),"No Video found",Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), PlayVideo.class);
            intent.putExtra("video", complaint.getVideo());
            startActivity(intent);
        }
    }

    public void pic(View view) {
        if(complaint.getPic().isEmpty()){
            Toast.makeText(getApplicationContext(),"No Picture found",Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Show_Pic.class);
            intent.putExtra("img", complaint.getPic());
            startActivity(intent);
        }
    }
    private void getUserName(){
        DatabaseReference myRef = database.getReference("users/" +  complaint.getUserID() + "/students");

        myRef.child("regno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                user.setVisibility(View.VISIBLE);
                user.setText("Sender Name: " + complaint.getUsername() + "\r\nSender Reg# " + value);
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
    public void onBackPressed() {
        Intent openLogin = new Intent(SingleComplainStaffView.this,staffHome.class);
        startActivity(openLogin);

    }

    private void getToken(final String flag){
        DatabaseReference myRef = database.getReference("users/" + complaint.getUserID() + "/students");

        myRef.child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
              //  setToken(value);

                if(flag.equals("approve")){
                    new MyFirebaseInstanceService().sendMessageSingle(SingleComplainStaffView.this,value,"Alert", "Your Complaint is Resolved", null);
                }
                else if(flag.equals("declined")){
                    new MyFirebaseInstanceService().sendMessageSingle(SingleComplainStaffView.this,value,"Alert", "Your Complaint is Declined", null);
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Database Error",error.toString());
                return;
            }
        });
    }
}
