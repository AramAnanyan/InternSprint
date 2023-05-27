package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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

import Models.EmployerModel;
import Models.UserModel;


public class Invitations extends AppCompatActivity {
    // ...

    FirebaseFirestore firestore;

    FirebaseDatabase database;
    RecyclerView recyclerViewInvitations;
    InvitationsAdapter invitationsAdapter;
    List<EmployerModel> employerModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        // ...
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        employerModelList = new ArrayList<>();
        recyclerViewInvitations = findViewById(R.id.recyclerViewInvitations);
        recyclerViewInvitations.setVisibility(View.VISIBLE);
        recyclerViewInvitations.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);

        invitationsAdapter = new InvitationsAdapter(getApplicationContext(), employerModelList);
        recyclerViewInvitations.setAdapter(invitationsAdapter);

        // Initialize the adapter
        invitationsAdapter = new InvitationsAdapter(getApplicationContext(), employerModelList);
        recyclerViewInvitations.setAdapter(invitationsAdapter);

        // Retrieve invitations data and populate the list
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
        DatabaseReference ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitations");
        ArrayList<String> invitations = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invitations.clear(); // Clear the existing list

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    Log.i("asd", item);
                    invitations.add(item);
                }

                DatabaseReference employersRef = database.getReference().child("Employers");
                employersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        employerModelList.clear(); // Clear the existing list

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            EmployerModel item = childSnapshot.getValue(EmployerModel.class);

                            if (invitations.contains(childSnapshot.getKey())) {
                                item.setEmail(childSnapshot.child("email").getValue(String.class));
                                item.setName(childSnapshot.child("name").getValue(String.class));
                                item.setSurName(childSnapshot.child("surName").getValue(String.class));
                                item.setPassword(childSnapshot.child("password").getValue(String.class));
                                item.setId(childSnapshot.getKey());
                                employerModelList.add(item);
                            }
                        }

                        invitationsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle cancelled event
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event
            }
        });
    }
}

