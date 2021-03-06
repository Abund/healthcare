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

import com.example.healthcare.adapter.BloodSugarAdapter;
import com.example.healthcare.model.BloodSugar;
import com.example.healthcare.popups.BloodSugarPopUp;
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

public class BloodSugarActivity extends Fragment {

    private LineChart lineChart;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<BloodSugar> data;
    private ArrayList<String> mData;
    private DatabaseReference myRef;
    private BloodSugarAdapter bloodSugarAdapter;
    private Button suggestionBS,clearBS;
    private TextView alternate;
    private ArrayList<String> bloodSugarKey;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_blood_sugar,container,false);
        super.onCreate(savedInstanceState);

        lineChart =(LineChart) view.findViewById(R.id.lineChart2);
        alternate =(TextView) view.findViewById(R.id.alternate);
        clearBS  =(Button) view.findViewById(R.id.clearBS);
        suggestionBS =(Button) view.findViewById(R.id.suggestionBS);
        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerViewBSA);
        //lineChart.setOnChartGestureListener(BloodpressureActivity.this);
        //lineChart.setOnChartValueSelectedListener(BloodpressureActivity.this);


        data = new ArrayList<BloodSugar>();
        bloodSugarKey= new ArrayList<>();

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRef = FirebaseDatabase.getInstance().getReference().child("BloodSugar").child(FirebaseAuth.getInstance().getUid());
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot.getChildrenCount() == 0){
                        return;
                    }
                    BloodSugar bloodSugar = dataSnapshot1.getValue(BloodSugar.class);
                    data.add(bloodSugar);
                    bloodSugarKey.add(dataSnapshot1.getKey());
                }
                lineChart=sendata(data,lineChart);
                if(data.isEmpty()){
                    alternate.setVisibility(View.VISIBLE);
                    alternate.setText("Please add your data");
                }else {
                    alternate.setVisibility(View.INVISIBLE);
                    alternate.setText("");
                }
                bloodSugarAdapter = new BloodSugarAdapter(data,getActivity());
                new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecycler);
                mRecycler.setAdapter(bloodSugarAdapter);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
                mRecycler.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getBaseContext(),"Oppss... something went wrong",Toast.LENGTH_SHORT).show();
            }
        });



        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent at = new Intent(getActivity().getBaseContext(), BloodSugarAddPage.class);
                startActivity(at);
            }
        });
        suggestionBS.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (data.isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext(), "Please enter a reading", Toast.LENGTH_SHORT).show();
                } else{
                    startActivity(new Intent(getActivity().getBaseContext(), BloodSugarPopUp.class));
                }
            }
        });

        clearBS.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodSugar").child(FirebaseAuth.getInstance().getUid());
                databaseReference.removeValue();
                Intent at = new Intent(getActivity().getBaseContext(), HomeScreen.class);
                startActivity(at);
            }
        });
        return view;
    }

    private LineChart sendata(ArrayList<BloodSugar> data1,LineChart lineChart) {
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
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        final String[] months = new String[data1.size()+1000];
        for(int i =0;i<data1.size();i++){
            months[i]=data1.get(i).getDate();
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

        if(data1.size()==1) {
            yValues.add(new Entry(-1, 120));
        }
        for(int i =0;i<data1.size();i++){
            float f1 = (float)data1.get(i).getConcentrationSugar();
            yValues.add(new Entry(i, f1));
            Log.e("eeeeeeeeeeeeeeeeee",""+data1.get(i).getConcentrationSugar());
        }

        LineDataSet set1 = new LineDataSet(yValues,"Data set 1");
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

    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
//            myRef = FirebaseDatabase.getInstance().getReference().child("BloodSugar").child(FirebaseAuth.getInstance().getUid())
//                    .child(bloodSugarKey.get(viewHolder.getAdapterPosition()));
//            Query mQuery1 = myRef;
//            mQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    dataSnapshot.getRef().removeValue();
//                    data.remove(viewHolder.getAdapterPosition());
//                    bloodSugarAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                    bloodSugarAdapter.notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodSugar").child(FirebaseAuth.getInstance().getUid())
                    .child(bloodSugarKey.get(viewHolder.getAdapterPosition()));

            bloodSugarKey.remove(viewHolder.getAdapterPosition());
            data.remove(viewHolder.getAdapterPosition());
            bloodSugarAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            bloodSugarAdapter.notifyDataSetChanged();
            databaseReference.removeValue();

        }
    };
}
