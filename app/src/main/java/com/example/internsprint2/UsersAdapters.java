package com.example.internsprint2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Models.UserModel;

import com.example.internsprint2.Profiles.UserProfileForEmployer;
import com.example.internsprint2.Profiles.UserProfileForUser;


public class UsersAdapters extends RecyclerView.Adapter<UsersAdapters.ViewHolder> {
    private Context context;
    private List<UserModel> userModelList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;


    public UsersAdapters(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(userModelList.get(position).getName());
        holder.userSurName.setText(userModelList.get(position).getSurName());
        String id=userModelList.get(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                Log.i("ABC",id);
                database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Intent intent;
                        try {

                            if (userModel.getEmail() != null) {

                                intent = new Intent(context, UserProfileForUser.class);
                                intent.putExtra("EXTRA",id);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }
                        } catch (Exception ex) {


                            intent = new Intent(context, UserProfileForEmployer.class);
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
        return userModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, userSurName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            userSurName = itemView.findViewById(R.id.userSurName);
        }
    }
}
