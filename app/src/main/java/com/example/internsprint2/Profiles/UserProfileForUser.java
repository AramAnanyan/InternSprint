package com.example.internsprint2.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.internsprint2.EmployersActivity;
import com.example.internsprint2.MoreUserForAll;
import com.example.internsprint2.R;
import com.example.internsprint2.UsersActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import Models.EmployerModel;
import Models.UserModel;


public class UserProfileForUser extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_for_user);
        Intent intent=getIntent();
        String id=intent.getStringExtra("EXTRA");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ImageView navigBar = findViewById(R.id.navigationBar);
        Button more=findViewById(R.id.more);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView surname = findViewById(R.id.profileSurName);
        TextView topName = findViewById(R.id.profileNameTop);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);



        navigBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.users:

                        Intent intent=new Intent(UserProfileForUser.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent2=new Intent(UserProfileForUser.this, EmployersActivity.class);
                        startActivity(intent2);
                        finish();
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
                                        intent = new Intent(UserProfileForUser.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(UserProfileForUser.this, UserProfileActivity.class);
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
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);

                name.setText(userModel.getName());
                surname.setText(userModel.getSurName());
                email.setText(userModel.getEmail());
                topName.setText(userModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileForUser.this, MoreUserForAll.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

}