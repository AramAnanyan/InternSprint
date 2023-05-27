package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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



public class ConfirmedUsers extends AppCompatActivity {
    FirebaseFirestore firestore;

    FirebaseDatabase database;
    RecyclerView recyclerViewUsers;
    UsersAdapters usersAdapters;
    List<UserModel> userModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_users);

        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userModelList = new ArrayList<>();
        recyclerViewUsers = findViewById(R.id.recyclerViewConfirmedUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);
        recyclerViewUsers.setAdapter(usersAdapters);
        retrieveInvitations();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the invitations list
                retrieveInvitations();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void retrieveInvitations() {
        DatabaseReference ref = database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("confirmedUsers");
        ArrayList<String> confirmedUsers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                confirmedUsers.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    Log.i("asd", item);
                    confirmedUsers.add(item);
                }

                DatabaseReference ref1 = database.getReference().child("Users");
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModelList.clear();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            UserModel item = childSnapshot.getValue(UserModel.class);
                            if (confirmedUsers.contains(childSnapshot.getKey())) {
                                item.setEmail(childSnapshot.child("email").getValue(String.class));
                                item.setName(childSnapshot.child("name").getValue(String.class));
                                item.setSurName(childSnapshot.child("surName").getValue(String.class));
                                item.setPassword(childSnapshot.child("password").getValue(String.class));
                                item.setId(childSnapshot.getKey());

                                userModelList.add(item);
                            }
                        }

                        usersAdapters.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}

