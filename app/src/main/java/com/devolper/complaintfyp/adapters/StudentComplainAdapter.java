package com.devolper.complaintfyp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.students.SingleComplainStudentView;
import com.devolper.complaintfyp.students.homeActivity;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StudentComplainAdapter extends BaseAdapter {

   Context context;
   ArrayList<ComplaintData> list;

    ArrayList<String> cordinator_list = new ArrayList<>();
    ArrayList<String> gym_list = new ArrayList<>();
    ArrayList<String> cafe_list = new ArrayList<>();
    ArrayList<String> security_list = new ArrayList<>();
    ArrayList<String> transport_list = new ArrayList<>();
    @SerializedName("s")
    public String s1;
    userAuthentications userAuth = new userAuthentications();
    public StudentComplainAdapter(Context context, ArrayList<ComplaintData> list){
        this.list=list;
        this.context=context;
        userAuth.setContext(context);

        cordinator_list.add("dept");
        cordinator_list.add("other");
        gym_list.add("gym");
        cafe_list.add("cafe");
        security_list.add("security");
        transport_list.add("transport");

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid=convertView;
        if (convertView == null) {

            grid = new View(context);

            grid = inflater.inflate(R.layout.listitemstudentcomplains, null);
        }
        final ComplaintData complaint=list.get(i);
        TextView date=(TextView)grid.findViewById(R.id.date);
        TextView category=(TextView)grid.findViewById(R.id.category);
        TextView detail=(TextView)grid.findViewById(R.id.detail);
        TextView status=(TextView)grid.findViewById(R.id.status);
        Button delete=(Button)grid.findViewById(R.id.delete);

        date.setText(complaint.getDate());
        category.setText(complaint.getCat());
       long s=cal_days(complaint.getDate());
    if(complaint.getStatus().equals("0")) {
        if (s <= 7){
            if(cordinator_list.contains(complaint.getCat() ))
                status.setText("Assigned to Coordinator");
            else if(gym_list.contains(complaint.getCat() ))
                status.setText("Assigned to Gym Admin");
            else if(cafe_list.contains(complaint.getCat() ))
                status.setText("Assigned to Cafe Admin");
            else if(security_list.contains(complaint.getCat() ))
                status.setText("Assigned to Security Admin");
            else if(transport_list.contains(complaint.getCat() ))
                status.setText("Assigned to Transport Admin");
            else
                status.setText("Assigned to Cordinator");
        }
        else if (s > 7 && s < 15)
            status.setText("Assigned to HOD");
        else
            status.setText("Assigned to Dean");
    }
    else{
        //status.setVisibility(View.GONE);
    }

         delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                 db.child("complaints").child(userAuth.userData()).child(complaint.getKey()).removeValue();
                 Toast.makeText(context,"Delete", Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(context, homeActivity.class);
                 context.startActivity(intent);

             }
         });
         detail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Gson g=new Gson();
                 s1=g.toJson(complaint);
                Intent intent=new Intent(context, SingleComplainStudentView.class);
                 intent.putExtra("s",s1);
                 context.startActivity(intent);
             }
         });
   return grid;
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

}
