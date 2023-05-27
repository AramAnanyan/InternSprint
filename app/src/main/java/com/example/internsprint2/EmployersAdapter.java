package com.example.internsprint2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internsprint2.Profiles.EmployerProfileForEmployer;
import com.example.internsprint2.Profiles.EmployerProfileForUser;
import com.example.internsprint2.Profiles.UserProfileForEmployer;
import com.example.internsprint2.Profiles.UserProfileForUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.EmployerModel;
import Models.UserModel;


public class EmployersAdapter extends RecyclerView.Adapter<EmployersAdapter.ViewHolder> {
    private Context context;
    private List<EmployerModel> employerModelList;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    public EmployersAdapter(Context context, List<EmployerModel> employerModelList) {
        this.context = context;
        this.employerModelList = employerModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(employerModelList.get(position).getName());
        holder.surName.setText(employerModelList.get(position).getSurName());
        holder.workPlace.setText(employerModelList.get(position).getWorkPlace());
        String id=employerModelList.get(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database= FirebaseDatabase.getInstance();
                auth= FirebaseAuth.getInstance();
                Log.i("ABC",id);
                database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Intent intent;
                        try {

                            if (userModel.getEmail() != null) {

                                intent = new Intent(context, EmployerProfileForUser.class);
                                intent.putExtra("EXTRA",id);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }
                        } catch (Exception ex) {


                            intent = new Intent(context, EmployerProfileForEmployer.class);
                            intent.putExtra("EXTRA",id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return employerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, surName, role, workPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            surName = itemView.findViewById(R.id.userSurName);
            workPlace = itemView.findViewById(R.id.workPlace);
            role = itemView.findViewById(R.id.role);
        }
    }
}
