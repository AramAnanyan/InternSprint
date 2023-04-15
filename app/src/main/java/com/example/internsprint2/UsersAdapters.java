package com.example.internsprint2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Models.UserModel;
/*
public class UsersAdapters extends FirestoreRecyclerAdapter<UserModel, UsersAdapters.UserHolder> {

    public UsersAdapters(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull UserModel model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        TextView name, email;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.userEmail);
        }
    }
}*/

public class UsersAdapters extends RecyclerView.Adapter<UsersAdapters.ViewHolder> {
    private Context context;
    private List<UserModel> userModelList;

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

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, userSurName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            userSurName = itemView.findViewById(R.id.userSurName);
        }
    }
}
