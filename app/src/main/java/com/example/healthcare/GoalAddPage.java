package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.healthcare.fragments.DatePickerFragment;
import com.example.healthcare.fragments.TimePickerFragment;
import com.example.healthcare.model.Goal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class GoalAddPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText goal;
    private Button save;
    private TextView time,date;
    private DatabaseReference myRef;
    Date start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        time= (TextView) findViewById(R.id.calGoalTime);
        date= (TextView) findViewById(R.id.calGoalDate);
        goal= (EditText) findViewById(R.id.calgoals);
        save = (Button) findViewById(R.id.calGoalSave);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("goal");
        myRef.keepSynced(true);

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

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String gool = goal.getText().toString();
                String daat = date.getText().toString();
                String timm = time.getText().toString();

                if(gool.isEmpty()){
                    goal.setError("Please enter your desired goal");
                    goal.requestFocus();
                    return;
                }
                else if(daat.isEmpty()){
                    date.setError("Please enter date");
                    date.requestFocus();
                    return;
                }
                else if(timm.isEmpty()){
                    time.setError("Please enter time");
                    time.requestFocus();
                    return;
                }


                Goal goal1 = new Goal();
                goal1.setGoal(goal.getText().toString());
                goal1.setDate(date.getText().toString());
                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(goal1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                finish();
//                Query query = myRef.orderByChild("goal").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
//                        DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
//                        String key = nodeShot.getKey();
//                        HashMap<String, Object> update = new HashMap<>();
//                        update.put("goal", goal.getText().toString());
//                        myRef.child(key).updateChildren(update);
//                    }
//                    @Override public void onCancelled(DatabaseError databaseError) {
//
//                    }});
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
