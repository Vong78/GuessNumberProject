package com.example.GuessNumberApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.LinkedList;

class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private LinkedList<HashMap<String,String>> answer;
    private Context context;
    private String[] from = {"order","guess","result"};

    public ViewAdapter(LinkedList<HashMap<String,String>> answer, Context context) {
        this.answer = answer;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> sol = answer.get(position);
        holder.tvNo.setText(sol.get(from[0]));
        holder.tvInputNum.setText(sol.get(from[1]));
        holder.tvGrade.setText(sol.get(from[2]));

    }

    @Override
    public int getItemCount() {
        return answer.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNo;
        private TextView tvInputNum;
        private TextView tvGrade;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           tvNo = itemView.findViewById(R.id.tv_no);
           tvInputNum = itemView.findViewById(R.id.tv_inputnum);
           tvGrade = itemView.findViewById(R.id.tv_grade);

            rootView = itemView;
        }
    }
}
