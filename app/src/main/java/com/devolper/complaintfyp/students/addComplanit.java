package com.devolper.complaintfyp.students;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devolper.complaintfyp.Notifications.MyFirebaseInstanceService;
import com.devolper.complaintfyp.R;
import com.devolper.complaintfyp.chooseSigninActivity;
import com.devolper.complaintfyp.objects.Tokens;
import com.devolper.complaintfyp.objects.userAuthentications;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class addComplanit extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    ArrayList list;
    ArrayList identity;
    TextView sliderUserName,sliderUserEmail;
    userAuthentications userAuth = new userAuthentications();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String getName;
    public void setName(String s){
        getName = s;
    }

    public String getName(){
        return this.getName;
    }

    DatabaseReference db;

    Spinner input_catagory,input_identity;
    EditText input_details,input_loc;
    Button add_image,add_video,btn_submit;
    String type,tokenType;

    private static final int PICK_IMAGE_REQUEST = 10;
    private static final int PERMISSION_REQUEST_CODE = 1;

    String Pic="",Video="",Identity,Cat;
    Uri Video_uri,Image_Uri;
    ProgressDialog pd;
    ArrayList<String> tokenList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://complainfyp-7ba04.appspot.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complanit);
        userAuth.setContext(addComplanit.this);
        type = getIntent().getStringExtra("type");
        tokenList = new ArrayList<>();
        if(type.equals("cafe")){
            tokenType = "cafeadmin";
        }
        else if(type.equals("dept")){
            tokenType = "cordinator";
        }
        else if(type.equals("other")){
            tokenType = "cordinator";
        }
        else if(type.equals("transport")){
            tokenType = "transportadmin";
        }
        else if(type.equals("gym")){
            tokenType = "gymadmin";
        }
        else if(type.equals("security")){
            tokenType = "securityadmin";
        }
        else {
            tokenType = "dean";
        }
        getUserName();
        if(userAuth.userData() == null){
            Intent openLogin = new Intent(addComplanit.this,chooseSigninActivity.class);
            startActivity(openLogin);
        }
        db= FirebaseDatabase.getInstance().getReference();
        initilize();

        identity = new ArrayList<>();
        identity.add("no");
        identity.add("yes");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(addComplanit.this,
                android.R.layout.simple_spinner_item, identity);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_identity.setAdapter(dataAdapter);

        if(!checkPermission()){
            requestPermission();
        }

        get_Category();
        input_catagory.setOnItemSelectedListener(this);
        input_identity.setOnItemSelectedListener(this);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermission()){
                    requestPermission();
                }
                else{
                    attach_image();
                }
            }
        });
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermission()){
                    requestPermission();
                }
                else{
                    attach_video();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String details = input_details.getText().toString();
                String loc = input_loc.getText().toString();

                if(details.length() < 3){
                    input_details.setError("Please Enter Valid Details");
                }
                else if(loc.length() < 3 ){
                    input_loc.setError("Please Enter Valid Location");
                }
                else{
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String Date = df.format(c);

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    String key = get_key(10);

                    db.child("complaints").child(userAuth.userData()).child(key).child("catagory").setValue(type);
                    db.child("complaints").child(userAuth.userData()).child(key).child("subcatagory").setValue(Cat);
                    db.child("complaints").child(userAuth.userData()).child(key).child("address").setValue(loc);
                    db.child("complaints").child(userAuth.userData()).child(key).child("identity").setValue(Identity);
                    db.child("complaints").child(userAuth.userData()).child(key).child("details").setValue(details);
                    db.child("complaints").child(userAuth.userData()).child(key).child("picture").setValue(Pic);
                    db.child("complaints").child(userAuth.userData()).child(key).child("username").setValue(sliderUserName.getText().toString());
                    db.child("complaints").child(userAuth.userData()).child(key).child("video").setValue(Video);
                    db.child("complaints").child(userAuth.userData()).child(key).child("date").setValue(Date);
                    db.child("complaints").child(userAuth.userData()).child(key).child("status").setValue("0");
                    db.child("complaints").child(userAuth.userData()).child(key).child("key").setValue(key);

                    Toast.makeText(getApplicationContext(), "Complain Submitted Successfully", Toast.LENGTH_LONG).show();
                    getTokens();
                    Intent openLogin = new Intent(addComplanit.this,homeActivity.class);
                    startActivity(openLogin);
                }
            }
        });
    }
    public void initilize(){
        input_catagory = (Spinner) findViewById(R.id.input_catagory);
        input_identity = (Spinner) findViewById(R.id.input_identity);
        input_details = (EditText) findViewById(R.id.input_details);
        input_loc = (EditText) findViewById(R.id.input_loc);
        add_image = (Button) findViewById(R.id.add_image);
        add_video = (Button) findViewById(R.id.add_video);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }
    private void get_Category() {
        db.child("catagories").child(type).child("about").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(int i=1;i<=dataSnapshot.getChildrenCount();i++){
                    list.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(addComplanit.this,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                input_catagory.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_add_complanit);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent openLogin = new Intent(addComplanit.this,homeActivity.class);
            startActivity(openLogin);
        }
    }
    private void setNavigation(){
        dl = (DrawerLayout)findViewById(R.id.activity_add_complanit);
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
                        Intent openLogin0 = new Intent(addComplanit.this,profileActivity.class);
                        startActivity(openLogin0);
                        break;
                    case R.id.nav_tips:
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent openLogin = new Intent(addComplanit.this,chooseSigninActivity.class);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    public void attach_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

    }

    public void attach_video() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);

    }
    public    String get_key(int n){
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString
                = new String(array, Charset.forName("UTF-8"));

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer();

        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < randomString.length(); k++) {

            char ch = randomString.charAt(k);

            if (((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9'))
                    && (n > 0)) {

                r.append(ch);
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(addComplanit.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {


        ActivityCompat.requestPermissions(addComplanit.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Image_Uri = data.getData();
            String path=getImagePath(Image_Uri);
            Pic=path.substring(path.lastIndexOf("/") + 1);
            pd=new ProgressDialog(addComplanit.this);
            pd.setMessage("Uploading....");
            pd.setCancelable(false);
            pd.show();
            StorageReference childRef = storageRef.child("complaints/"+Pic);

            //uploading the image

            UploadTask uploadTask = childRef.putFile(Image_Uri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(addComplanit.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    // finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(addComplanit.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });


        }
        if (requestCode == 1&& resultCode == RESULT_OK && data != null && data.getData() != null) { pd = new ProgressDialog(this);



            pd=new ProgressDialog(addComplanit.this);
            pd.setMessage("Uploading....");
            pd.setCancelable(false);
            pd.show();
            Video_uri = data.getData();
            String path=getVideoPath(Video_uri);
            Video=path.substring(path.lastIndexOf("/") + 1);

            StorageReference childRef = storageRef.child(Video);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(Video_uri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(addComplanit.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(addComplanit.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });





        }
    }
    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.input_catagory:
                Cat=adapterView.getItemAtPosition(i).toString();
                break;
            case    R.id.input_identity:
                Identity=adapterView.getItemAtPosition(i).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getVideoPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void getTokens() {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference();
        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    for (DataSnapshot d : dd.getChildren()) {
                        Log.d("Key ",d.getKey().toString());
                        if(d.getKey().equalsIgnoreCase(tokenType)){
                            try {
                                Tokens c = d.getValue(Tokens.class);
                                tokenList.add(c.getToken());
                            }
                            catch (NullPointerException e){
                            }
                        }

                    }
                }

                if(tokenList.size()>0){
                    JSONArray js = new JSONArray(tokenList);
                    new MyFirebaseInstanceService().sendMessageMulti(addComplanit.this,js,"Alert", "New Complaint Registered", null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
