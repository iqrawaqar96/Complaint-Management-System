package com.devolper.complaintfyp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.other_staff.SingleComplainStaffView;
import com.devolper.complaintfyp.other_staff.ViewAllComplaintsStaff;

import com.google.gson.Gson;

import java.util.ArrayList;

public class StaffComplainAdapter extends BaseAdapter {

    Context context;
    ArrayList<ComplaintData> list;
    ArrayList<ComplaintData> itemsCopy;
    private static ArrayList<ComplaintData> data;
    public StaffComplainAdapter(Context context, ArrayList<ComplaintData> list) {
        this.list=list;
        this.data = list;
        this.context= context;
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
        //status.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        status.setText(complaint.getAddress());

        date.setText(complaint.getDate());
        try {
            category.setText(complaint.getCat().substring(0,1).toUpperCase() + complaint.getCat().substring(1) + "/" + complaint.getSubcatagory().substring(0,1).toUpperCase() + complaint.getSubcatagory().substring(1));

        }
        catch (NullPointerException e){

        }
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Gson g=new Gson();
                String s=g.toJson(complaint);
                Intent intent=new Intent(context, SingleComplainStaffView.class);
                intent.putExtra("s",s);
                context.startActivity(intent);
            }
        });

        return grid;
    }

    public ArrayList filter(String text) {
        if(text.isEmpty()){
            //
        } else{
            text = text.toLowerCase();
            itemsCopy = new ArrayList<>();
            int count = 0;
            for(ComplaintData item: data){
                if(item.getSubcatagory().toLowerCase().contains(text) || item.getUsername().toLowerCase().contains(text) || item.getCat().toLowerCase().contains(text)){
                    itemsCopy.add(data.get(count));
                }

                count++;
            }

            if(count == 0)
                Toast.makeText(context,"No Results Found!",Toast.LENGTH_SHORT).show();

//            filtered(itemsCopy);
        }
        return itemsCopy;
    }
}
