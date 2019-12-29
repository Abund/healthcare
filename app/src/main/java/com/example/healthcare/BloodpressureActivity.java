package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import com.example.healthcare.adapter.Bloodpressureadapter;
import com.example.healthcare.model.BloodPressure;
import com.example.healthcare.popups.BloodPressurePopUp;
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

public class BloodpressureActivity extends Fragment {

    private LineChart lineChart;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<BloodPressure> data;
    private ArrayList<String> mData;
    private DatabaseReference myRef;
    private Bloodpressureadapter bloodpressureadapter;
    View view;
    private DatabaseReference myRefOnline;
    private TextView alternate;
    private Button suggestionBP,clearBP;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_bloodpressure,container,false);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bloodpressure);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerViewBP);
        alternate = (TextView) view.findViewById(R.id.alternateBP);
        suggestionBP =(Button) view.findViewById(R.id.suggestionBP);
        clearBP=(Button) view.findViewById(R.id.clearBP);
        //lineChart.setOnChartGestureListener(BloodpressureActivity.this);
        //lineChart.setOnChartValueSelectedListener(BloodpressureActivity.this);
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        data = new ArrayList<BloodPressure>();

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        //  myRef = FirebaseDatabase.getInstance().getReference().child("BloodPressure").child(FirebaseAuth.getInstance().getUid()).push();
        myRefOnline = FirebaseDatabase.getInstance().getReference().child("BloodPressure").child(FirebaseAuth.getInstance().getUid());
        myRefOnline.keepSynced(true);

        myRefOnline.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    return;
                }
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    BloodPressure bloodPressure = dataSnapshot1.getValue(BloodPressure.class);
                    data.add(bloodPressure);
                }
                lineChart=setUpChart(data,lineChart);

                Log.e("eeeeeeeeeeeeeeeeee",data.toString());
                if(data.isEmpty()){
                    alternate.setVisibility(View.VISIBLE);
                    alternate.setText("Please enter your readings");
                }
                //Toast.makeText(getActivity().getBaseContext(),data.toString(),Toast.LENGTH_SHORT).show();
                bloodpressureadapter = new Bloodpressureadapter(data,getActivity().getBaseContext());
                bloodpressureadapter.notifyDataSetChanged();
                //new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecycler);
                mRecycler.setAdapter(bloodpressureadapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getBaseContext(),"Oppss... something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent at = new Intent(getActivity().getBaseContext(), BloodPressureAddPage.class);
                startActivity(at);
            }
        });

        suggestionBP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getBaseContext(), BloodPressurePopUp.class));
            }
        });

        clearBP.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                myRef= FirebaseDatabase.getInstance().getReference().child("BloodPressure").child(FirebaseAuth.getInstance().getUid());
                //Query mQuery = myRef.orderByChild("diastolicPressure").equalTo(data.get(viewHolder.getAdapterPosition()).getDiastolicPressure());
                //Query mQuery1 = myRef;
                myRefOnline.addValueEventListener(new ValueEventListener() {
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

    private LineChart setUpChart(ArrayList<BloodPressure> data1, LineChart lineChart) {
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        LimitLine upperLimit = new LimitLine(130f,"DANGER");
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

//        final String[] months = new String[]{"Feb", "Feb", "Mar", "Apr", "Mar", "Apr"};

        final String[] months = new String[data.size()+1];
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

        float f= (float)data1.get(0).getSystolicPressure();
        Log.e("eeeeeeeeeeeeeeeeee",""+f);

        if(data.size()==1) {
            yValues.add(new Entry(-1, 120));
        }
        for(int i =0;i<data1.size();i++){
            float f1 = (float)data1.get(i).getSystolicPressure();
            yValues.add(new Entry(i, f1));
            Log.e("eeeeeeeeeeeeeeeeee",""+data1.get(i).getSystolicPressure());
        }

        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");
        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
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

//    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            myRefOnline = FirebaseDatabase.getInstance().getReference().child("BloodPressure").child(FirebaseAuth.getInstance().getUid());
//            Query mQuery = myRefOnline.orderByChild("diastolicPressure").equalTo(data.get(viewHolder.getAdapterPosition()).getDiastolicPressure());
//            Query mQuery1 = myRefOnline;
//            mQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    //for(DataSnapshot ds:dataSnapshot.getChildren()){
//                        dataSnapshot.getRef().removeValue();
//                    //}
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            data.remove(viewHolder.getAdapterPosition());
//            bloodpressureadapter.notifyDataSetChanged();
//        }
//    };

}
