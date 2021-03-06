package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.healthcare.model.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView bloodp,bloods,cal,goal,clientName,profileName;
    Button medi;
    private ImageView imageViewCalories,imageViewBloodPressure,imageViewBloodSugar,imageViewProfile,imageViewHomePageProfile,imageViewGo;
    FirebaseAuth firebaseAuth;
    FirebaseUser userf;
    DatabaseReference myRef;
    boolean closeApp;
    private int TAKE_IMAGE_CODE=10001;
    private static  final  int PICK_IMAGE=1;
    Uri imageuri;
    private  static final String TAG="HomeScreenActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference("users");
        databaseReference.keepSynced(true);
        myRef = FirebaseDatabase.getInstance().getReference().child("users");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        profileName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        imageViewProfile = navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bloodp = (TextView) findViewById(R.id.bloodPressureT);
        bloods = (TextView) findViewById(R.id.bloodSugarT);
        imageViewCalories = (ImageView) findViewById(R.id.imageViewCalories);
        imageViewGo = (ImageView) findViewById(R.id.imageViewGo);
        imageViewBloodPressure = (ImageView) findViewById(R.id.imageViewBloodPressure);
        imageViewBloodSugar = (ImageView) findViewById(R.id.imageViewBloodSugar);
        imageViewHomePageProfile = (ImageView) findViewById(R.id.imageViewHomePageProfile);
        cal = (TextView) findViewById(R.id.calories);
        goal = (TextView) findViewById(R.id.goals);
        medi=(Button) findViewById(R.id.medibutton);
        clientName = (TextView) findViewById(R.id.clientName);
        firebaseAuth =FirebaseAuth.getInstance();
        userf=firebaseAuth.getCurrentUser();

        ListView listView = new ListView(this);
        List<String> data = new ArrayList<>();
        data.add("Camera");
        data.add("Gallery");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setCancelable(true);
        builder.setView(listView);
        final AlertDialog dialog=builder.create();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            if(user.getPhotoUrl()!=null){
//                Glide.with(this)
//                        .load(user.getPhotoUrl())
//                        .into(imageViewProfile);
//                Glide.with(this)
//                        .load(user.getPhotoUrl())
//                        .into(imageViewHomePageProfile);
                Picasso.get().load(user.getPhotoUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(imageViewProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(user.getPhotoUrl()).into(imageViewProfile);
                    }
                });

                Picasso.get().load(user.getPhotoUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(imageViewHomePageProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(user.getPhotoUrl()).into(imageViewHomePageProfile);
                    }
                });
            }
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                User user= new User();
                user=dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(User.class);
                clientName.setText(user.getFirstName());
                profileName.setText(user.getFirstName()+" "+user.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeScreen.this,"Oppss... something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.show();
//                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if(intent.resolveActivity(getPackageManager())!=null){
//                    startActivityForResult(intent,TAKE_IMAGE_CODE);
//                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String choice = adapter.getItem(i);
                if(choice.equalsIgnoreCase("Camera")){
                    Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!=null){
                        startActivityForResult(intent,TAKE_IMAGE_CODE);
                    }
                    dialog.dismiss();
                }else{
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery,"select picture"),PICK_IMAGE);
                    dialog.dismiss();
                }
            }
        });

        bloodp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodpressureActivity()).addToBackStack(null).commit();
            }
        });

        bloods.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodSugarActivity()).addToBackStack(null).commit();
            }
        });

        imageViewCalories.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new CaloriesActivity()).addToBackStack(null).commit();
            }
        });

        imageViewGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new GoalActivity()).addToBackStack(null).commit();
            }
        });
        imageViewBloodPressure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodpressureActivity()).addToBackStack(null).commit();
            }
        });
        imageViewBloodSugar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodSugarActivity()).addToBackStack(null).commit();
            }
        });

        cal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new CaloriesActivity()).addToBackStack(null).commit();
            }
        });

        goal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new GoalActivity()).addToBackStack(null).commit();
            }
        });

        medi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new MedicationDashBoard()).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
////        if(closeApp==true){
////            Intent intent = new Intent(Intent.ACTION_MAIN);
////            intent.addCategory(Intent.CATEGORY_HOME);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(intent);
////            finish();
////            System.exit(0);
////        }
////        closeApp=true;
//        //Toast.makeText(HomeScreen.this,"Please press back again to exit",Toast.LENGTH_SHORT).show();
//            super.onBackPressed();
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                closeApp=false;
////            }
////        },3000);
//
//        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count==0){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                    imageViewProfile.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }else if(requestCode==PICK_IMAGE){
            //if(data.getData()!= null){

            //}
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = null;
                    try {
                        imageuri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                        imageViewProfile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e.getCause());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e(TAG,"onSuccess:"+uri);
                        setUserProfile(uri);
                    }
                });
    }

    private void setUserProfile(Uri uri){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri).build();
        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeScreen.this,"Updated successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeScreen.this,"profile image failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent at = new Intent(HomeScreen.this, HomeScreen.class);
            startActivity(at);
        } else if (id == R.id.nav_bpressure) {
            Fragment newFragment =  new BloodpressureActivity();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.add(R.id.content_frame, newFragment).commit();
            replaceFragment(newFragment);

        }else if (id == R.id.nav_bsugar) {
            Fragment newFragment =  new BloodSugarActivity();
            replaceFragment(newFragment);

        }else if (id == R.id.nav_calorie) {
            Fragment newFragment =  new CaloriesActivity();
            replaceFragment(newFragment);

        } else if (id == R.id.nav_mreminder) {
            Fragment newFragment =  new MedicationDashBoard();
            replaceFragment(newFragment);

        } else if (id == R.id.nav_goal) {
            Fragment newFragment =  new GoalActivity();
            replaceFragment(newFragment);

        }
        else if (id == R.id.sign_out) {

            firebaseAuth = FirebaseAuth.getInstance();
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            Intent at = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(at);
        }
//
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.content_frame, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.addToBackStack(null).commit();
    }
}
