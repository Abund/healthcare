package com.example.healthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;
import com.example.healthcare.model.Medication;

import java.util.ArrayList;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {


    private ArrayList<String> mData;
    private ArrayList<Medication> Data;
    private Context ct1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView drugName;
        public TextView infoMD;
        public View layout;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            date = (TextView) itemView.findViewById(R.id.dateMediCard);
            drugName = (TextView) itemView.findViewById(R.id.drugName);
            infoMD = (TextView) itemView.findViewById(R.id.infoMD);
            card = (CardView) itemView.findViewById(R.id.lineChart);
        }
    }
    public MedicationAdapter(ArrayList<String> Data1){

        this.mData=Data1;
    }

    public MedicationAdapter(ArrayList<Medication> Data, Context ct)
    {
        this.Data=Data;
        this.ct1 =ct;
    }

    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicationcard,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MedicationAdapter.ViewHolder holder, final int position) {
        holder.date.setText(Data.get(position).getStarteDate()+"");
        holder.drugName.setText(Data.get(position).getMedicationName()+"");
        holder.infoMD.setText(Data.get(position).getInstructions()+"");

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

}
