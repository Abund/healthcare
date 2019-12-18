package com.example.healthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.healthcare.R;
import com.example.healthcare.model.Goal;

import java.util.ArrayList;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ArrayList<Goal> Data;
    private Context ct1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView goalReading;
        public View layout;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            time = (TextView) itemView.findViewById(R.id.timeRealDate);
            goalReading = (TextView) itemView.findViewById(R.id.measurementGoalCard);
            card = (CardView) itemView.findViewById(R.id.lineChart);
        }
    }
    public GoalAdapter(ArrayList<String> Data1){

        this.mData=Data1;
    }

    public GoalAdapter(ArrayList<Goal> Data, Context ct)
    {
        this.Data=Data;
        this.ct1 =ct;
    }

    @Override
    public GoalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goalcard,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final GoalAdapter.ViewHolder holder, final int position) {
        holder.time.setText(Data.get(position).getDate()+"");
        holder.goalReading.setText(Data.get(position).getGoal()+"");

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }
}
