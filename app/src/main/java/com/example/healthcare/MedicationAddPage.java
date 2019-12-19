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
import com.example.healthcare.model.Medication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class MedicationAddPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText medicationName,notes;
    private AutoCompleteTextView instructionMedi,repeat,pills,unitsMd,EndDate;
    private DatabaseReference myRef;
    private TextView startDate;
    private ImageView medImageDrop,medImageUnitDrop,medImageEndDateDrop;
    private static final String[] meas=new String[]{"Before food","After food","With food","No matter"};
    private static final String[] meas1=new String[]{"pill","gm","mg","ml","pieces","tablet","units","strips","tablespoon","drops","mcg","cream","carton","spray"};
    private static final String[] meas2=new String[]{"Number of days","Continuous",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_add_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medicationName= (EditText) findViewById(R.id.medicationName);
        startDate= (TextView) findViewById(R.id.dateMedi);
        EndDate= (AutoCompleteTextView) findViewById(R.id.endDateMedi);
        notes= (EditText) findViewById(R.id.notesMD);
        instructionMedi= (AutoCompleteTextView) findViewById(R.id.instructionMedi);
        repeat= (AutoCompleteTextView) findViewById(R.id.repeat);
        pills= (AutoCompleteTextView) findViewById(R.id.pills);
        unitsMd= (AutoCompleteTextView) findViewById(R.id.unitsMd);
        medImageDrop= (ImageView) findViewById(R.id.medImageDrop);
        medImageUnitDrop= (ImageView) findViewById(R.id.medImageUnitDrop);
        medImageEndDateDrop= (ImageView) findViewById(R.id.medImageEndDateDrop);

        ArrayAdapter<String> com=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,meas);
        ArrayAdapter<String> com1=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,meas1);
        ArrayAdapter<String> com2=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,meas2);
        instructionMedi.setAdapter(com);
        instructionMedi.setThreshold(0);

        pills.setAdapter(com1);
        pills.setThreshold(0);

        EndDate.setAdapter(com2);
        EndDate.setThreshold(0);

        medImageDrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                instructionMedi.showDropDown();
            }
        });

        medImageUnitDrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pills.showDropDown();
            }
        });
        medImageEndDateDrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EndDate.showDropDown();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Medication").child(FirebaseAuth.getInstance().getUid()).push();
        myRef.keepSynced(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonMAD);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String medicationN= medicationName.getText().toString();
                String startD = startDate.getText().toString();
                String instruct = instructionMedi.getText().toString();
                String reapt = repeat.getText().toString();

                if(medicationN.isEmpty()){
                    medicationName.setError("Please enter the medication Name");
                    medicationName.requestFocus();
                    return;
                }
                else if(startD.isEmpty()){
                    startDate.setError("Please enter the start date");
                    startDate.requestFocus();
                    return;
                }
                else if(instruct.isEmpty()){
                    instructionMedi.setError("Please enter the instructions");
                    instructionMedi.requestFocus();
                    return;
                }
                else if(reapt.isEmpty()){
                    repeat.setError("Please enter time intervals");
                    repeat.requestFocus();
                    return;
                }

                Medication medication = new Medication();
                medication.setEndDate(EndDate.getText().toString());
                medication.setInstructions(instructionMedi.getText().toString());
                medication.setMedicationName(medicationName.getText().toString());
                medication.setNotes(notes.getText().toString());
                medication.setNumberOfDays(unitsMd.getText().toString());
                medication.setRepeats(repeat.getText().toString());
                medication.setUnits(pills.getText().toString());
                medication.setStarteDate(startDate.getText().toString());

                myRef.setValue(medication).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
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
        startDate.setText(currentDatePicker);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
