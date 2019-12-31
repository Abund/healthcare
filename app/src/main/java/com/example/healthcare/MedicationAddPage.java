package com.example.healthcare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
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
import com.example.healthcare.model.Medication;
import com.example.healthcare.notification.AlarmReceiver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MedicationAddPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText medicationName,notes;
    private AutoCompleteTextView instructionMedi,pills,unitsMd,EndDate;
    private DatabaseReference myRef;
    private TextView startDate,medicationTime,repeat;
    private ImageView medImageDrop,medImageUnitDrop,medImageEndDateDrop;
    private static final String[] meas=new String[]{"Before food","After food","With food","No matter"};
    private static final String[] meas1=new String[]{"pill","gm","mg","ml","pieces","tablet","units","strips","tablespoon","drops","mcg","cream","carton","spray"};
    private static final String[] meas2=new String[]{"Number of days","Continuous",};
    Calendar reminder;
    boolean[] checkedItems;
    String[] listItems;
    ArrayList<Integer> mUserItems= new ArrayList<>();

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_add_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medicationName= (EditText) findViewById(R.id.medicationName);
        startDate= (TextView) findViewById(R.id.dateMedi);
        medicationTime= (TextView) findViewById(R.id.medicationTime);
        EndDate= (AutoCompleteTextView) findViewById(R.id.endDateMedi);
        notes= (EditText) findViewById(R.id.notesMD);
        instructionMedi= (AutoCompleteTextView) findViewById(R.id.instructionMedi);
        repeat= (TextView) findViewById(R.id.repeat);
        pills= (AutoCompleteTextView) findViewById(R.id.pills);
        unitsMd= (AutoCompleteTextView) findViewById(R.id.unitsMd);
        medImageDrop= (ImageView) findViewById(R.id.medImageDrop);
        medImageUnitDrop= (ImageView) findViewById(R.id.medImageUnitDrop);
        medImageEndDateDrop= (ImageView) findViewById(R.id.medImageEndDateDrop);
        listItems= getResources().getStringArray(R.array.days_of_week);
        checkedItems= new boolean[listItems.length];
        createNotificationChanel();

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

        EndDate.setOnClickListener(new View.OnClickListener(){
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
        medicationTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        repeat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MedicationAddPage.this);
                mBuilder.setTitle("Medication Days");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }
                        }else if(mUserItems.contains(position)){
                            mUserItems.remove(position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for(int i = 0; i<mUserItems.size();i++){
                            item=item+listItems[mUserItems.get(i)];
                            if(i!=mUserItems.size()-1){
                                item = item + ", ";
                            }
                        }
                        repeat.setText(item);
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i =0;i<checkedItems.length;i++){
                            checkedItems[i]= false;
                            mUserItems.clear();
                            repeat.setText("");
                        }
                    }
                });
                AlertDialog mDialog =mBuilder.create();
                mDialog.show();
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
                String medicationTime1 = medicationTime.getText().toString();

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
                }else if(medicationTime1.isEmpty()){
                    medicationTime.setError("Please the time of your medication");
                    medicationTime.requestFocus();
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
                medication.setMedicationTime(medicationTime.getText().toString());

                myRef.setValue(medication).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        //getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new MedicationDashBoard()).commit();
                    }
                });

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent broadcast=PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);
                //alarmManager.set(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(),broadcast);
            }
        });

    }

    private  void createNotificationChanel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Health Care";
            String description = "a channel for user updates";
            int inportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyme",name,inportance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        reminder=Calendar.getInstance();
        reminder.set(Calendar.HOUR_OF_DAY,i);
        reminder.set(Calendar.MINUTE,i1);
        medicationTime.setText(""+i+":"+i1);
    }



}
