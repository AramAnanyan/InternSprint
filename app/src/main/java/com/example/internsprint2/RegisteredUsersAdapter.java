package com.example.internsprint2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Models.UserModel;

import com.example.internsprint2.Profiles.UserProfileForEmployer;
import com.example.internsprint2.Profiles.UserProfileForUser;
import com.squareup.picasso.Picasso;


public class RegisteredUsersAdapter extends RecyclerView.Adapter<RegisteredUsersAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userModelList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;


    public RegisteredUsersAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(userModelList.get(position).getName());
        holder.userSurName.setText(userModelList.get(position).getSurName());
        String id=userModelList.get(position).getId();
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Users")
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
                userModelList.remove(position);
                notifyItemRemoved(position);
                database = FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                DatabaseReference ref = database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("registeredUsers");
                ArrayList<String> regUsers = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String item = childSnapshot.getValue(String.class);
                            regUsers.add(item);
                        }
                        regUsers.remove(id);
                        ref.setValue(regUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                Log.i("ABC",id);
                database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        ImageView delete, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.userName);
            userSurName = itemView.findViewById(R.id.userSurName);
            image = itemView.findViewById(R.id.profileImage);
        }
    }
}
