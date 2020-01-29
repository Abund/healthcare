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

import com.example.healthcare.fragments.DatePickerFragment;
import com.example.healthcare.fragments.TimePickerFragment;
import com.example.healthcare.model.BloodSugar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class BloodSugarAddPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private EditText sugarConcentration,notes,tags;
    private AutoCompleteTextView measured;
    private TextView date,time;
    private DatabaseReference myRef;
    private ImageView measuredPointer,imgDate,imgTime;
    private static final String[] meas=new String[]{"Before breakfast","After breakfast","Before lunch","After lunch","Before dinner" +
            "","After dinner","Before sleep","After sleep","Fasting","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_add_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sugarConcentration= (EditText) findViewById(R.id.sugarConcentration);
        measuredPointer=(ImageView) findViewById(R.id.measuredPointer);
        time= (TextView) findViewById(R.id.timeBS);
        date= (TextView) findViewById(R.id.dateBS);
        notes= (EditText) findViewById(R.id.notesBS);
        tags= (EditText) findViewById(R.id.tagsBS);
        measured= (AutoCompleteTextView) findViewById(R.id.measured);
        imgDate=(ImageView) findViewById(R.id.dateImgBS);
        imgTime=(ImageView) findViewById(R.id.timeImgBS);
        ArrayAdapter<String> com=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,meas);
        measured.setAdapter(com);
        measured.setThreshold(0);

        measuredPointer.setOnClickListener(new View.OnClickListener(){
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
        myRef = database.getReference("BloodSugar").child(FirebaseAuth.getInstance().getUid()).push();
        myRef.keepSynced(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonSA);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conc =sugarConcentration.getText().toString();
                String date1 = date.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                String arm = measured.getText().toString().trim();

                if(conc.isEmpty()){
                    sugarConcentration.setError("Sugar Concentration is required");
                    sugarConcentration.requestFocus();
                    return;
                }
                else if(arm.isEmpty()){
                    measured.setError("Please enter measured value");
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

                BloodSugar bloodSugar = new BloodSugar();
                bloodSugar.setConcentrationSugar(Integer.parseInt(sugarConcentration.getText().toString()));
                bloodSugar.setDate(date.getText().toString());
                bloodSugar.setMeasured(measured.getText().toString());
                bloodSugar.setTime(time.getText().toString());
                bloodSugar.setNotes(notes.getText().toString().trim());
                bloodSugar.setTag(tags.getText().toString().trim());
                myRef.setValue(bloodSugar).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                finish();
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
        time.setText(""+i+":"+i1);
    }
}

