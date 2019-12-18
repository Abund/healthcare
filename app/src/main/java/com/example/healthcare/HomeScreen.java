package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.healthcare.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
