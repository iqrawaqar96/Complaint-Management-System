package com.devolper.complaintfyp.students;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.DatabaseHelper;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.devolper.complaintfyp.other_staff.ViewAllComplaintsStaff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class SingleComplainStudentView extends AppCompatActivity {
    ComplaintData complaint;
    DatabaseReference db;
    EditText feed;
    Button feedback;
    TextView remarks,key,name,email,reg,cat,subcat,date,details,forwardTo,forwardTo1,date1,date2,date3,status,remarksTitle;
    GridLayout l1,l2,l3;
    ArrayList<String> cordinator_list = new ArrayList<>();
    ArrayList<String> gym_list = new ArrayList<>();
    ArrayList<String> cafe_list = new ArrayList<>();
    ArrayList<String> security_list = new ArrayList<>();
    ArrayList<String> transport_list = new ArrayList<>();
    userAuthentications userAuth = new userAuthentications();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseHelper myDb = new DatabaseHelper(this);
    @SerializedName("s")
    public String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_complain_student_view);

        db= FirebaseDatabase.getInstance().getReference();
        s=getIntent().getStringExtra("s");
        userAuth.setContext(SingleComplainStudentView.this);
     //   Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        Gson gson = new Gson();
        complaint=gson.fromJson(s,ComplaintData.class);

        cordinator_list.add("dept");
        cordinator_list.add("other");
        gym_list.add("gym");
        cafe_list.add("cafe");
        security_list.add("security");
        transport_list.add("transport");
        initilize();

            remarks.setVisibility(View.GONE);
            remarksTitle.setVisibility(View.GONE);
            long ss = cal_days(complaint.getDate());
            if (ss <= 7){
                if(cordinator_list.contains(complaint.getCat() )){
                    forwardTo1.setText("Forwarded To Coordinator");
                    forwardTo.setText("Coordinator");
                }

                else if(gym_list.contains(complaint.getCat() )){
                    forwardTo1.setText("Forwarded Gym Admin");
                    forwardTo.setText("Gym Admin");
                }

                else if(cafe_list.contains(complaint.getCat() )){
                    forwardTo1.setText("Forwarded to Cafe Admin");
                    forwardTo.setText("Cafe Admin");
                }

                else if(security_list.contains(complaint.getCat() )){
                    forwardTo1.setText("Forwarded to Security Admin");
                    forwardTo.setText("Security Admin");
                }

                else if(transport_list.contains(complaint.getCat() )){
                    forwardTo1.setText("Forwarded to Transport Admin");
                    forwardTo.setText("Transport Admin");
                }

                else{
                    forwardTo1.setText("Forwarded to Cordinator");
                    forwardTo.setText("Cordinator");
                }

            }
            else if (ss > 7 && ss < 15) {
                l2.setVisibility(View.VISIBLE);
                date2.setText(convert(7, complaint.getDate()));
                forwardTo.setText("Assigned to HOD");
            } else {
                //status.setText("Assigned to Dean");
                //LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
                l2.setVisibility(View.VISIBLE);
                date2.setText(convert(7, complaint.getDate()));
                forwardTo.setText("Assigned to HOD");
                //LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
                l3.setVisibility(View.VISIBLE);
                date3.setText(convert(14, complaint.getDate()));
            }

        if(complaint.getStatus().equalsIgnoreCase("0")) {

            status.setText("Pending");
            //status.setText("Your Complaint is Resolved!");
        }
        else if(complaint.getStatus().equalsIgnoreCase("1")) {
            getRemarks();
            status.setText("Resolved");
            //status.setText("Your Complaint is Resolved!");
        }
        else {
            getRemarks();
            status.setText("Declined");
        }

        key.setText("Complaint No " + complaint.getKey());
        name.setText(complaint.getUsername());
        email.setText(userAuth.userEmail());
        reg.setText(myDb.getProfileData().get(4).toString());
        cat.setText(complaint.getCat());
        subcat.setText(complaint.getSubcatagory());
        date.setText(complaint.getDate());
        date1.setText(complaint.getDate());
        details.setText(complaint.getDetail());
}
    public void initilize(){
        remarks = (TextView) findViewById(R.id.remarks);
        key = (TextView) findViewById(R.id.key);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        reg = (TextView) findViewById(R.id.reg);
        cat = (TextView) findViewById(R.id.cat);
        subcat = (TextView) findViewById(R.id.subcat);
        date = (TextView) findViewById(R.id.date);
        details = (TextView) findViewById(R.id.details);
        forwardTo = (TextView) findViewById(R.id.forwardTo);
        forwardTo1 = (TextView) findViewById(R.id.forwardTo1);
        status = (TextView) findViewById(R.id.status);
        remarksTitle = (TextView) findViewById(R.id.remarksTitle);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        date3 = (TextView) findViewById(R.id.date3);
        l1 = (GridLayout) findViewById(R.id.l1);
        l2= (GridLayout) findViewById(R.id.l2);
        l3 = (GridLayout) findViewById(R.id.l3);
    }
    private void getRemarks() {
        DatabaseReference myRef = database.getReference("complaints/" +userAuth.userData() + "/" + complaint.getKey());

        myRef.child("remarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    remarks.setVisibility(View.VISIBLE);
                    remarksTitle.setVisibility(View.VISIBLE);
                    String value = dataSnapshot.getValue(String.class);
                    remarks.setText(value);
                }
                catch (NullPointerException e){
                    //remarks.setText("Remarsks:\r\nRemarks Not Submitted");
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
    public String convert(int days,String oldDate){


        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, days);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }
    @Override
    public void onBackPressed() {
        Intent openLogin = new Intent(SingleComplainStudentView.this,ViewAllComplaintsStaff.class);
        startActivity(openLogin);

    }


}
