package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView bloodp,bloods,cal,goal,clientName,profileName;
    Button medi;
    private ImageView imageViewCalories,imageViewBloodPressure,imageViewBloodSugar,imageViewProfile,imageViewHomePageProfile;
    FirebaseAuth firebaseAuth;
    FirebaseUser userf;
    DatabaseReference myRef;
    private int TAKE_IMAGE_CODE=10001;
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
        imageViewBloodPressure = (ImageView) findViewById(R.id.imageViewBloodPressure);
        imageViewBloodSugar = (ImageView) findViewById(R.id.imageViewBloodSugar);
        imageViewHomePageProfile = (ImageView) findViewById(R.id.imageViewHomePageProfile);
        cal = (TextView) findViewById(R.id.calories);
        goal = (TextView) findViewById(R.id.goals);
        medi=(Button) findViewById(R.id.medibutton);
        clientName = (TextView) findViewById(R.id.clientName);
        firebaseAuth =FirebaseAuth.getInstance();
        userf=firebaseAuth.getCurrentUser();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            if(user.getPhotoUrl()!=null){
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageViewProfile);
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageViewHomePageProfile);
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
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,TAKE_IMAGE_CODE);
                }
            }
        });

        bloodp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodpressureActivity()).commit();
            }
        });

        bloods.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodSugarActivity()).commit();
            }
        });


        imageViewCalories.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new CaloriesActivity()).commit();
            }
        });
        imageViewBloodPressure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodpressureActivity()).commit();
            }
        });
        imageViewBloodSugar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new BloodSugarActivity()).commit();
            }
        });

        cal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new CaloriesActivity()).commit();
            }
        });

        goal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new GoalActivity()).commit();
            }
        });

        medi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new MedicationDashBoard()).commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        } else if (id == R.id.nav_bsugar) {
            Fragment newFragment =  new BloodSugarActivity();
            replaceFragment(newFragment);

        } else if (id == R.id.nav_calorie) {
            Fragment newFragment =  new CaloriesActivity();
            replaceFragment(newFragment);

        } else if (id == R.id.nav_mreminder) {
            Fragment newFragment =  new MedicationDashBoard();
            replaceFragment(newFragment);

        } else if (id == R.id.nav_goal) {
            Fragment newFragment =  new GoalActivity();
            replaceFragment(newFragment);

        }else if (id == R.id.sign_out) {

            firebaseAuth = FirebaseAuth.getInstance();
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            Intent at = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(at);
        }

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
        fragmentTransaction.commit();
    }
}
