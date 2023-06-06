package com.example.internsprint2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internsprint2.Profiles.EmployerProfileForEmployer;
import com.example.internsprint2.Profiles.EmployerProfileForUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Models.EmployerModel;
import Models.UserModel;



public class InvitationsAdapter extends RecyclerView.Adapter<InvitationsAdapter.ViewHolder> {
    private Context context;
    private List<EmployerModel> employerModelList;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    public InvitationsAdapter(Context context, List<EmployerModel> employerModelList) {
        this.context = context;
        this.employerModelList = employerModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_invitation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(employerModelList.get(position).getName());
        holder.surName.setText(employerModelList.get(position).getSurName());
        holder.workPlace.setText(employerModelList.get(position).getWorkPlace());
        holder.role.setText(employerModelList.get(position).getRole());
        String id=employerModelList.get(position).getId();

        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Employers")
                .child(id);

        employerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profilePictureUrl = snapshot.child("profilePicture").getValue(String.class);
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        // Load the profile picture using the URL using your preferred image loading library
                        // For example, you can use Picasso or Glide
                        // Here's an example using Picasso:
                        Picasso.get().load(profilePictureUrl).into(holder.image);
                    } else {
                        // Handle the case when the profile picture URL is empty or not available
                    }
                } else {
                    // Handle the case when the employer's data does not exist in the database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the database operation
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerModelList.remove(position);
                notifyItemRemoved(position);
                database = FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                DatabaseReference ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitations");
                ArrayList<String> invitations1 = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String item = childSnapshot.getValue(String.class);
                            invitations1.add(item);
                        }
                        invitations1.remove(id);
                        ref.setValue(invitations1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!holder.confirm.getText().toString().equals("confirmed")) {
                    database = FirebaseDatabase.getInstance();
                    auth=FirebaseAuth.getInstance();
                    holder.confirm.setText("confirmed");
                    holder.confirm.setBackgroundColor(Color.GRAY);
                    DatabaseReference ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitations");
                    ArrayList<String> invitations1 = new ArrayList<>();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String item = childSnapshot.getValue(String.class);
                                invitations1.add(item);
                            }
                            invitations1.remove(id);
                            ref.setValue(invitations1);
                            DatabaseReference ref1 = database.getReference().child("Employers").child(id).child("confirmedUsers");
                            ArrayList<String> confirmedUsers = new ArrayList<>();
                            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String item = childSnapshot.getValue(String.class);
                                        confirmedUsers.add(item);
                                    }
                                    if(!confirmedUsers.contains(auth.getCurrentUser().getUid())) {
                                        confirmedUsers.add(auth.getCurrentUser().getUid());
                                        ref1.setValue(confirmedUsers);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        });
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
        Button confirm;
        ImageView delete,image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            delete = itemView.findViewById(R.id.delete);
            surName = itemView.findViewById(R.id.userSurName);
            workPlace = itemView.findViewById(R.id.workPlace);
            role = itemView.findViewById(R.id.role);
            confirm=itemView.findViewById(R.id.confirm);
            image=itemView.findViewById(R.id.profileImage);
        }
    }
}
