package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.healthcare.fragments.DatePickerFragment;
import com.example.healthcare.fragments.TimePickerFragment;
import com.example.healthcare.model.BloodPressure;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class BloodPressureAddPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText systolicPressure,diastolicPressure,notes,tags;
    private AutoCompleteTextView measured;
    private DatabaseReference myRef;
    private ImageView pointer;
    TextView date,time;
    Date dob_var;
    String str;
    private static final String[] meas=new String[]{"Left","Right"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_add_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        systolicPressure= (EditText) findViewById(R.id.systolicPressure);
        pointer=(ImageView) findViewById(R.id.measuredPointerBP);
        diastolicPressure= (EditText) findViewById(R.id.diastolicPressure);
        date= (TextView) findViewById(R.id.pressuredate);
        notes= (EditText) findViewById(R.id.notesBP);
        tags= (EditText) findViewById(R.id.tags);
        measured= (AutoCompleteTextView) findViewById(R.id.measuredArm);
        time= (TextView) findViewById(R.id.pressureTime);
        ArrayAdapter<String> com=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,meas);
        measured.setAdapter(com);
        measured.setThreshold(0);

        pointer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                measured.showDropDown();
            }
        });

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BloodPressure").child(FirebaseAuth.getInstance().getUid()).push();
        myRef.keepSynced(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonA);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String syst = systolicPressure.getText().toString().trim();
                String diast = diastolicPressure.getText().toString().trim();
                String arm = measured.getText().toString().trim();
                String date1 = date.getText().toString().trim();
                String time1 = time.getText().toString().trim();

                if(syst.isEmpty()){
                    systolicPressure.setError("Systolic pressure is required");
                    systolicPressure.requestFocus();
                    return;
                }
                else if(diast.isEmpty()){
                    diastolicPressure.setError("diastolic is required");
                    diastolicPressure.requestFocus();
                    return;
                }
                else if(arm.isEmpty()){
                    measured.setError("Please enter measured arm");
                    measured.requestFocus();
                    return;
                }
                else if(date1.isEmpty()){
                    date.setError("Please enter date");
                    date.requestFocus();
                    return;
                }
                else if(time1.isEmpty()){
                    time.setError("Please enter time");
                    time.requestFocus();
                    return;
                }

                BloodPressure bloodPressure = new BloodPressure();
                bloodPressure.setDate(date.getText().toString());
                bloodPressure.setSystolicPressure(Integer.parseInt(systolicPressure.getText().toString()));
                bloodPressure.setDiastolicPressure(Integer.parseInt(diastolicPressure.getText().toString()));
                bloodPressure.setMeasuredArm(measured.getText().toString());
                bloodPressure.setTime(time.getText().toString());
                bloodPressure.setTag(tags.getText().toString().trim());
                bloodPressure.setNotes(notes.getText().toString().trim());


                myRef.setValue(bloodPressure).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BloodPressureAddPage.this,"Successful",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
//                Intent at = new Intent(BloodPressureAddPage.this, BloodpressureActivity.class);
//                startActivity(at);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        //c.set(Calendar.DAY_OF_MONTH,i2);
        String currentDatePicker = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        date.setText(currentDatePicker);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        //String currentDatePicker =DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        time.setText(""+i+":"+i1);
    }
}
