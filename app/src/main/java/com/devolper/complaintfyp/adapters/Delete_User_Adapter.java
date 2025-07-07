package com.devolper.complaintfyp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.objects.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Delete_User_Adapter extends BaseAdapter {

    android.content.Context context;
    ArrayList<Users> user_list;

    public Delete_User_Adapter(android.content.Context context, ArrayList<Users> user_list) {
        this.context=context;
        this.user_list = user_list;
    }

    @Override
    public int getCount() {
        return user_list.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        grid=convertView;
        if (convertView == null) {

            grid = new View(context);

            grid = inflater.inflate(R.layout.item_user_list, null);
        }
        final Users users=user_list.get(i);
        TextView Name=(TextView)grid.findViewById(R.id.name);
        TextView Reg=(TextView)grid.findViewById(R.id.reg);
        TextView Email=(TextView)grid.findViewById(R.id.email);

        final TextView approved=(TextView) grid.findViewById(R.id.approve);
        final TextView reject=(TextView) grid.findViewById(R.id.reject);

        approved.setText("Delete");
        reject.setVisibility(View.GONE);

        Name.setText(users.getName());
        Reg.setText(users.getReg());
        Email.setText(users.getEmail());
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(approved.getText().toString().equalsIgnoreCase("delete")){
                    users.setStatus("-1");
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                    db.child("users").child(users.getKey()).child("students").child("status").setValue("-1");
                    Toast.makeText(context,"User " + users.getName() + " is Deleted", Toast.LENGTH_LONG).show();
                    //  ((Activity)context).finish();
                    approved.setText("Deleted");
                }
                else{
                    Toast.makeText(context,"Account Already Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });

        return grid;
    }
}
