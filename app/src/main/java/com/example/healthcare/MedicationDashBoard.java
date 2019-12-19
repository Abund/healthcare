package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.adapter.MedicationAdapter;
import com.example.healthcare.model.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicationDashBoard extends Fragment {
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Medication> data;
    private ArrayList<String> mData;
    private DatabaseReference myRef;
    private MedicationAdapter medicationAdapter;
    private TextView alternate;
    View view;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_medication_dash_board);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_medication_dash_board,container,false);
        super.onCreate(savedInstanceState);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerViewMA1);

        data = new ArrayList<Medication>();
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        myRef = FirebaseDatabase.getInstance().getReference().child("Medication").child(FirebaseAuth.getInstance().getUid());
        myRef.keepSynced(true);
        alternate =(TextView) view.findViewById(R.id.alternateMD);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if (dataSnapshot.getChildrenCount() == 0){
                        return;
                    }
                    Medication medication = dataSnapshot1.getValue(Medication.class);
                    data.add(medication);
                }
                medicationAdapter = new MedicationAdapter(data,getActivity().getBaseContext());
                mRecycler.setAdapter(medicationAdapter);
                if(data.isEmpty()){
                    alternate.setVisibility(View.VISIBLE);
                    alternate.setText("Please add your data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getBaseContext(),"Oppss... something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButtonMD);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent at = new Intent(getActivity().getBaseContext(), MedicationAddPage.class);
                startActivity(at);
            }
        });
        return view;
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
