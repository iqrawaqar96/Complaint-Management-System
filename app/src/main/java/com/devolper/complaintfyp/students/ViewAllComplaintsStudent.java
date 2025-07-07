package com.devolper.complaintfyp.students;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.adapters.StudentComplainAdapter;
import com.devolper.complaintfyp.other_staff.ViewAllComplaintsStaff;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewAllComplaintsStudent extends AppCompatActivity {
    @SerializedName("s")
    public String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_complaints_student);

        ListView listView=(ListView)findViewById(R.id.listview);

        s=getIntent().getStringExtra("s");

       // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ComplaintData>>(){}.getType();
        ArrayList<ComplaintData> list = gson.fromJson(s, type);
        //  Toast.makeText(getApplicationContext(),""+list.size(),Toast.LENGTH_LONG).show();
        StudentComplainAdapter adapter=new StudentComplainAdapter(this,list);

        listView.setAdapter(adapter);
        if(list.size()==0){
            TextView t=(TextView)findViewById(R.id.t);
            t.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent openLogin = new Intent(ViewAllComplaintsStudent.this,homeActivity.class);
        startActivity(openLogin);

    }
}
