package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference usersRef;


    DrawerLayout drawerLayout;
    NavigationView navigationView;


    FirebaseAuth auth;
    RecyclerView recyclerViewUsers;
    RecyclerView recyclerViewWorkers;

    UsersAdapters usersAdapters;
    List<UserModel> userModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        //setUpRecycleView();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case
                            R.id.menuHome:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case
                            R.id.menuProfile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                       // startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;


                }
                return true;
            }
        });


        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        //recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerViewWorkers = findViewById(R.id.recyclerViewWorkers);
        recyclerViewWorkers.setVisibility(View.VISIBLE);



        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        userModelList = new ArrayList<>();
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        userModel.setName(documentSnapshot.getString("userName"));
                        userModel.setEmail(documentSnapshot.getString("userEmail"));
                        userModelList.add(userModel);

                        usersAdapters.notifyDataSetChanged();



                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Սխալ։" + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });


        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);

        recyclerViewUsers.setAdapter(usersAdapters);

        recyclerViewWorkers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        userModel.setName(documentSnapshot.getString("userName"));

                        userModel.setEmail(documentSnapshot.getString("userEmail"));
                        userModelList.add(userModel);


                        usersAdapters.notifyDataSetChanged();



                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Սխալ։" + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);

        recyclerViewWorkers.setAdapter(usersAdapters);


    }
    }
    /*private void setUpRecycleView(){
        Query query =usersRef.orderBy("userName",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        usersAdapters=new UsersAdapters(options);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setAdapter(usersAdapters);
    }
    protected void onStart(){
        super.onStart();
        usersAdapters.startListening();
    }
    protected void onStop(){
        super.onStop();
        usersAdapters.stopListening();
    }*/


