package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Models.UserModel;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference usersRef;


    DrawerLayout drawerLayout;
    NavigationView navigationView;


    FirebaseAuth auth;
    RecyclerView recyclerViewUsers;
    //RecyclerView recyclerViewWorkers;

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

        ImageView navigBar = findViewById(R.id.navigationBar);


        navigBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                navigationView.setItemIconTintList(null);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuHome:
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;

                            case R.id.menuProfile:
                                drawerLayout.closeDrawer(GravityCompat.START);
                                // startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                                break;


                        }
                        return true;
                    }
                });
            }
        });


        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        userModelList = new ArrayList<>();
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);

                        userModel.setName(documentSnapshot.getString("userName"));
                        userModel.setSurName(documentSnapshot.getString("userEmail"));
                        userModel.setEmail(documentSnapshot.getString("userEmail"));
                        //userModel.setBiography(documentSnapshot.getString("userEmail"));
                        userModelList.add(userModel);

                        usersAdapters.notifyDataSetChanged();


                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Սխալ։" + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });


        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);

        recyclerViewUsers.setAdapter(usersAdapters);


    }
}



