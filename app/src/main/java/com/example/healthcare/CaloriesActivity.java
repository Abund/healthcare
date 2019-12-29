package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.adapter.CalorieAdapter;
import com.example.healthcare.model.Calorie;
import com.example.healthcare.popups.CaloriePopUp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CaloriesActivity extends Fragment {

    private LineChart lineChart;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Calorie> data;
    private ArrayList<String> mData;
    private DatabaseReference myRef;
    private CalorieAdapter calorieAdapter;
    private Button suggestion,clearCS;
    private TextView alternate;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_calories,container,false);
        super.onCreate(savedInstanceState);
        lineChart =(LineChart) view.findViewById(R.id.lineChart3);
        alternate =(TextView) view.findViewById(R.id.alternateCA);
        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerViewCA);
        suggestion = (Button) view.findViewById(R.id.suggestionCAS);
        clearCS=(Button) view.findViewById(R.id.clearCS);
        //lineChart.setOnChartGestureListener(BloodpressureActivity.this);
        //lineChart.setOnChartValueSelectedListener(BloodpressureActivity.this);

        data = new ArrayList<Calorie>();
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        myRef = FirebaseDatabase.getInstance().getReference().child("Calorie").child(FirebaseAuth.getInstance().getUid());
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot.getChildrenCount() == 0){
                        return;
                    }
                    Calorie calorie = dataSnapshot1.getValue(Calorie.class);
                    data.add(calorie);
                }
                lineChart=sendata(lineChart,data);
                calorieAdapter = new CalorieAdapter(data,getActivity().getBaseContext());
                mRecycler.setAdapter(calorieAdapter);
                if(data.isEmpty()){
                    alternate.setVisibility(View.VISIBLE);
                    alternate.setText("Please enter your readings");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getBaseContext(),"Oppss... something went wrong",Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent at = new Intent(getActivity().getBaseContext(), CaloriesAddPage.class);
                startActivity(at);
            }
        });

        suggestion.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getBaseContext(), CaloriePopUp.class));
            }
        });

        clearCS.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                myRef= FirebaseDatabase.getInstance().getReference().child("Calorie").child(FirebaseAuth.getInstance().getUid());
                //Query mQuery = myRef.orderByChild("diastolicPressure").equalTo(data.get(viewHolder.getAdapterPosition()).getDiastolicPressure());
                //Query mQuery1 = myRef;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Intent at = new Intent(getActivity().getBaseContext(), HomeScreen.class);
                        startActivity(at);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }

    private LineChart sendata(LineChart lineChart,ArrayList<Calorie> data) {
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        LimitLine upperLimit = new LimitLine(2100f,"DANGER");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f,10f,0);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(15f);

        LimitLine lowerLimit = new LimitLine(112f,"Too Low");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f,10f,0);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        upperLimit.setTextSize(15f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimit);
        leftAxis.addLimitLine(lowerLimit);
        //leftAxis.setAxisMaximum(100f);
        //leftAxis.setAxisMinimum(25f);
        leftAxis.enableGridDashedLine(10f,10f,0);
        leftAxis.setDrawLimitLinesBehindData(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

//        final String[] months = new String[]{"Feb", "Feb", "Mar", "Apr", "Mar", "Apr"};

        final String[] months = new String[data.size()];
        for(int i =0;i<data.size();i++){
            months[i]=data.get(i).getDate();
            //yValues.add(new Entry(i, data.get(i).getDiastolicPressure()));
        }

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if(value==-1){
                    return months[(int) 0.0];
                }
                return months[(int) value];
            }
        };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        ArrayList<Entry> yValues = new ArrayList<>();

        //data.get(0).getCalorieUnits();
        //Log.e("eeeeeeeeeeeeeeeeee",""+data.get(0).getCalorieUnits());

        if(data.size()==1) {
            yValues.add(new Entry(-1, 120));
        }
        for(int i =0;i<data.size();i++){
            float f1 = (float)data.get(i).getCalorieUnits();
            yValues.add(new Entry(i, f1));
            Log.e("eeeeeeeeeeeeeeeeee",""+data.get(i).getCalorieUnits());
        }

        LineDataSet set1 = new LineDataSet(yValues,"Data set 1");
        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data1 = new LineData(dataSets);
        lineChart.setData(data1);
        return lineChart;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("HealthCare");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment : Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}
