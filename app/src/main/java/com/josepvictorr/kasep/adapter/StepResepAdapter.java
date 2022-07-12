package com.josepvictorr.kasep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josepvictorr.kasep.R;

import java.util.List;

public class StepResepAdapter extends RecyclerView.Adapter<StepResepAdapter.BahanStepHolder> {
    List<String> listStepBahan;

    public StepResepAdapter(List<String> listStep){
        this.listStepBahan = listStep;
    }

    @NonNull
    @Override
    public BahanStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_resep, parent, false);
        return new BahanStepHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BahanStepHolder holder, int position) {
        holder.tvResepStep.setText(listStepBahan.get(position));
    }

    @Override
    public int getItemCount() {
        return listStepBahan.size();
    }

    public class BahanStepHolder extends RecyclerView.ViewHolder {
        TextView tvResepStep;
        public BahanStepHolder(@NonNull View itemView) {
            super(itemView);
            tvResepStep = itemView.findViewById(R.id.tvResepStep);
        }
    }
}
