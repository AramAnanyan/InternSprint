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
import com.example.internsprint2.Profiles.EmployerProfileActivity;
import com.example.internsprint2.Profiles.UserProfileActivity;


public class EmployersActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference usersRef;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SearchView searchView;


    FirebaseAuth auth;
    RecyclerView recyclerViewEmployers;
    //RecyclerView recyclerViewWorkers;
    FirebaseDatabase database;
    EmployersAdapter employersAdapter;
    List<EmployerModel> employers;
    String currentSearchQuery = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        searchView = findViewById(R.id.searchView);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        recyclerViewEmployers = findViewById(R.id.recyclerViewEmployers);
        recyclerViewEmployers.setVisibility(View.VISIBLE);
        recyclerViewEmployers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        employers = new ArrayList<>();

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EmployersActivity.this);

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
                        Intent intent = new Intent(EmployersActivity.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.employers:
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
                                        intent = new Intent(EmployersActivity.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(EmployersActivity.this, UserProfileActivity.class);
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

        employersAdapter = new EmployersAdapter(getApplicationContext(), employers);
        recyclerViewEmployers.setAdapter(employersAdapter);

        retrieveEmployers();

        progressDialog.dismiss();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentSearchQuery.isEmpty()) {

                    retrieveEmployers(); // No search query, retrieve all
                } else {

                    retrieveEmployers(currentSearchQuery.toLowerCase(Locale.ROOT)); // Pass the search query to retrieve filtered employers

                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                retrieveEmployers(query.toLowerCase(Locale.ROOT));

                currentSearchQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(EmployersActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                retrieveEmployers(newText.toLowerCase(Locale.ROOT));
                progressDialog.dismiss();
                currentSearchQuery = newText;
                return false;
            }
        });
    }
    private void retrieveEmployers(){
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EmployersActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        firestore.collection("Employers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    employers.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        EmployerModel employerModel = documentSnapshot.toObject(EmployerModel.class);

                        employerModel.setName(documentSnapshot.getString("name"));
                        employerModel.setSurName(documentSnapshot.getString("surname"));
                        employerModel.setEmail(documentSnapshot.getString("email"));
                        employerModel.setWorkPlace(documentSnapshot.getString("workPlace"));


                        employers.add(employerModel);
                        employersAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "error։" + task.getException(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            }
        });
    }
    private void retrieveEmployers(String a) {


        firestore.collection("Employers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    employers.clear();

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        EmployerModel employerModel = documentSnapshot.toObject(EmployerModel.class);

                        employerModel.setName(documentSnapshot.getString("name"));
                        employerModel.setSurName(documentSnapshot.getString("surname"));
                        employerModel.setEmail(documentSnapshot.getString("email"));
                        employerModel.setWorkPlace(documentSnapshot.getString("workPlace"));

                        String name = documentSnapshot.getString("name");
                        String surname = documentSnapshot.getString("surname");
                        String workPlace = documentSnapshot.getString("workPlace");

                        if ((name != null && name.toLowerCase(Locale.ROOT).contains(a)) || (surname != null && surname.toLowerCase(Locale.ROOT).contains(a)) || (workPlace!= null && workPlace.toLowerCase(Locale.ROOT).contains(a)) || (name+surname).toLowerCase(Locale.ROOT).contains(a)) {

                            employers.add(employerModel);
                            employersAdapter.notifyDataSetChanged();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}