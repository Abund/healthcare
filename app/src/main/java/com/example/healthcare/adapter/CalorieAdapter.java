package com.example.healthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;
import com.example.healthcare.model.Calorie;

import java.util.ArrayList;

public class CalorieAdapter extends RecyclerView.Adapter<CalorieAdapter.ViewHolder>  {

    private ArrayList<String> mData;
    private ArrayList<Calorie> Data;
    private Context ct1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView time;
        public TextView timeType;
        public TextView calorieUnits;
        public View layout;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            date = (TextView) itemView.findViewById(R.id.dateCalorieCard);
            time = (TextView) itemView.findViewById(R.id.timeCalorie);
            timeType = (TextView) itemView.findViewById(R.id.timeTypeCalorieCard);
            calorieUnits = (TextView) itemView.findViewById(R.id.calorieUnits);
            card = (CardView) itemView.findViewById(R.id.lineChart);
        }
    }
    public CalorieAdapter(ArrayList<String> Data1){

        this.mData=Data1;
    }

    public CalorieAdapter(ArrayList<Calorie> Data, Context ct)
    {
        this.Data=Data;
        this.ct1 =ct;
    }

    @Override
    public CalorieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caloriecard,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CalorieAdapter.ViewHolder holder, final int position) {
        holder.date.setText(Data.get(position).getDate()+"");
        holder.time.setText(Data.get(position).getTime()+"");
        holder.timeType.setText(Data.get(position).getFoodType()+"");
        holder.calorieUnits.setText(Data.get(position).getCalorieUnits()+"");

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

}
