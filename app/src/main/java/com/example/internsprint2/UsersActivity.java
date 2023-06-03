package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Models.EmployerModel;
import Models.UserModel;
import com.example.internsprint2.Profiles.EmployerProfileActivity;
import com.example.internsprint2.Profiles.UserProfileActivity;

public class UsersActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference usersRef;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    SearchView searchView;
    FirebaseDatabase database;

    FirebaseAuth auth;
    RecyclerView recyclerViewUsers;


    UsersAdapters usersAdapters;
    List<UserModel> userModelList;
    String currentSearchQuery = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.searchView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        userModelList = new ArrayList<>();

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UsersActivity.this);

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        ImageView navigBar = findViewById(R.id.navigationBar);
        navigBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.users:

                        //Intent intent=new Intent(UsersActivity.this, UsersActivity.class);

                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent=new Intent(UsersActivity.this, EmployersActivity.class);
                        startActivity(intent);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        // startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;
                    case R.id.profile:
                        database.getReference().child("Employers").child(auth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                                Intent intent;
                                try {
                                    if (employerModel.getEmail() != null) {
                                        intent = new Intent(UsersActivity.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(UsersActivity.this, UserProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                }
                return true;
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);
        recyclerViewUsers.setAdapter(usersAdapters);

        retrieveUsers();
        progressDialog.dismiss();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentSearchQuery.isEmpty()) {
                    retrieveUsers(); // No search query, retrieve all employers
                } else {
                    retrieveUsers(currentSearchQuery); // Pass the search query to retrieve filtered employers
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                retrieveUsers(query);
                currentSearchQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(UsersActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                retrieveUsers(newText);
                progressDialog.dismiss();
                currentSearchQuery = newText;
                return false;
            }
        });
    }

    private void retrieveUsers(){
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UsersActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userModelList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        userModel.setName(documentSnapshot.getString("userName"));
                        userModel.setSurName(documentSnapshot.getString("userSurName"));
                        userModel.setEmail(documentSnapshot.getString("userEmail"));
                        userModel.setId(documentSnapshot.getString("userId"));
                        userModelList.add(userModel);
                        usersAdapters.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "error։ " + task.getException(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void retrieveUsers(String a){
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    userModelList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        userModel.setName(documentSnapshot.getString("userName"));
                        userModel.setSurName(documentSnapshot.getString("userSurName"));
                        userModel.setEmail(documentSnapshot.getString("userEmail"));
                        userModel.setId(documentSnapshot.getString("userId"));

                        if((documentSnapshot.getString("userName")+documentSnapshot.getString("userSurName")).toLowerCase(Locale.ROOT).replaceAll("\\s", "").contains(a)) {
                            userModelList.add(userModel);
                            usersAdapters.notifyDataSetChanged();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "error։ " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}