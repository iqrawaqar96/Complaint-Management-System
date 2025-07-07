package com.devolper.complaintfyp.adapters;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.devolper.complaintfyp.Notifications.MyFirebaseInstanceService;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.objects.ComplaintData;
import com.devolper.complaintfyp.objects.Users;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;

public class User_List_Adapter extends BaseAdapter {
    userAuthentications userAuth = new userAuthentications();
   android.content.Context context;
    ArrayList<Users> user_list;
    Users users;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String getToken;
    public void setToken(String s){
        getToken = s;
    }


    public User_List_Adapter(android.content.Context context, ArrayList<Users> user_list) {
        this.context=context;
        this.user_list = user_list;
        userAuth.setContext(context);
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
       users=user_list.get(i);
        TextView Name=(TextView)grid.findViewById(R.id.name);
        TextView Reg=(TextView)grid.findViewById(R.id.reg);
        TextView Email=(TextView)grid.findViewById(R.id.email);
        final TextView reject=(TextView)grid.findViewById(R.id.reject);

     final TextView approved=(TextView) grid.findViewById(R.id.approve);

        Name.setText(users.getName());
        Reg.setText(users.getReg());
        Email.setText(users.getGender().substring(0,1).toUpperCase());
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(users.getStatus().equalsIgnoreCase("0")){
                    users.setStatus("1");
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                    db.child("users").child(users.getKey()).child("students").child("status").setValue("1");
                    Toast.makeText(context,"User " + users.getName() + " is Approved", Toast.LENGTH_LONG).show();
                    //  ((Activity)context).finish();
                    approved.setText("Approved");

                    getToken("approve");
                }
                else{
                    Toast.makeText(context,"Account Approved!!!", Toast.LENGTH_LONG).show();
                    getToken("approve");
                }

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(users.getStatus().equalsIgnoreCase("0")){
                    users.setStatus("1");
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                    db.child("users").child(users.getKey()).child("students").child("status").setValue("2");
                    Toast.makeText(context,"User " + users.getName() + " is Rejected", Toast.LENGTH_LONG).show();
                    //  ((Activity)context).finish();
                    reject.setText("Rejected");
                    getToken("declined");
                }
                else{
                    Toast.makeText(context,"Account Rejected!!!", Toast.LENGTH_LONG).show();
                    getToken("declined");
                }

            }
        });

        return grid;
    }

    private void getToken(final String flag){
        DatabaseReference myRef = database.getReference("users/" + users.getKey() + "/students");

        myRef.child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                setToken(value);

                if(flag.equals("approve")){
                    new MyFirebaseInstanceService().sendMessageSingle(context,value,"Alert", "Your Account is approved", null);
                }
                else if(flag.equals("declined")){
                    new MyFirebaseInstanceService().sendMessageSingle(context,value,"Alert", "Your Account is Rejected", null);
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
