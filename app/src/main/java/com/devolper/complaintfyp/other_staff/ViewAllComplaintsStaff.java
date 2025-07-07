package com.devolper.complaintfyp.other_staff;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.adapters.StaffComplainAdapter;
import com.devolper.complaintfyp.adapters.StudentComplainAdapter;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.students.addComplanit;
import com.devolper.complaintfyp.students.homeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.R.attr.data;

public class ViewAllComplaintsStaff extends AppCompatActivity {
    ListView listView;
    ArrayList<ComplaintData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_complaints_staff);

        listView=(ListView)findViewById(R.id.listview);

        String s=getIntent().getStringExtra("s");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ComplaintData>>(){}.getType();
         list = gson.fromJson(s, type);
        //  Toast.makeText(getApplicationContext(),""+list.size(),Toast.LENGTH_LONG).show();

        StaffComplainAdapter adapter=new StaffComplainAdapter(this,list);
        listView.setAdapter(adapter);
        if(list.size()==0){
            TextView t=(TextView)findViewById(R.id.t);
            t.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent openLogin = new Intent(ViewAllComplaintsStaff.this,staffHome.class);
        startActivity(openLogin);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    StaffComplainAdapter adapter=new StaffComplainAdapter(ViewAllComplaintsStaff.this,list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    StaffComplainAdapter adapter=new StaffComplainAdapter(ViewAllComplaintsStaff.this,list);
                    ArrayList<ComplaintData> data =   adapter.filter(query);
//                    ArrayList<jobData> data = new ArrayList<jobData>(Arrays.asList());
//                    progressBar.setVisibility(View.GONE);
//                    txtWait.setVisibility(View.GONE);
                    listView.setAdapter(new StaffComplainAdapter(ViewAllComplaintsStaff.this,data));
                    adapter.notifyDataSetChanged();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    StaffComplainAdapter adapter=new StaffComplainAdapter(ViewAllComplaintsStaff.this,list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
                }
                else{
                    StaffComplainAdapter adapter=new StaffComplainAdapter(ViewAllComplaintsStaff.this,list);
                    ArrayList<ComplaintData> data1 =   adapter.filter(newText);
//                    ArrayList<jobData> data = new ArrayList<jobData>(Arrays.asList());
//                    progressBar.setVisibility(View.GONE);
//                    txtWait.setVisibility(View.GONE);
                    listView.setAdapter(new StaffComplainAdapter(ViewAllComplaintsStaff.this,data1));
                    adapter.notifyDataSetChanged();

                }
                return true;
            }
        });

        return true;
    }
}
