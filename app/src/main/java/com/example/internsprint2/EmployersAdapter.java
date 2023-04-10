package com.example.internsprint2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Models.EmployerModel;


public class EmployersAdapter extends RecyclerView.Adapter<EmployersAdapter.ViewHolder> {
    private Context context;
    private List<EmployerModel> employerModelList;

    public EmployersAdapter(Context context, List<EmployerModel> employerModelList) {
        this.context = context;
        this.employerModelList = employerModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(employerModelList.get(position).getName());
        holder.surName.setText(employerModelList.get(position).getSurName());

    }

    @Override
    public int getItemCount() {
        return employerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, surName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            surName = itemView.findViewById(R.id.userSurName);
        }
    }
}
