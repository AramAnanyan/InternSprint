package com.example.internsprint2.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.internsprint2.EmployersActivity;
import com.example.internsprint2.R;
import com.example.internsprint2.UsersActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import Models.EmployerModel;


public class EmployerProfileForEmployer extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile_for_employer);
        Intent intent = getIntent();
        String id=intent.getStringExtra("EXTRA");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ImageView image=findViewById(R.id.profileImage);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.email);
        TextView workplace = findViewById(R.id.workplace);
        TextView phone = findViewById(R.id.phone);
        TextView about = findViewById(R.id.about);
        TextView role = findViewById(R.id.role);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);

        ImageView navigBar = findViewById(R.id.navigationBar);


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

                        Intent intent=new Intent(EmployerProfileForEmployer.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent2=new Intent(EmployerProfileForEmployer.this, EmployersActivity.class);
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
                                        intent = new Intent(EmployerProfileForEmployer.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(EmployerProfileForEmployer.this, UserProfileActivity.class);
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
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Employers")
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
                        Picasso.get().load(profilePictureUrl).into(image);
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
        database.getReference().child("Employers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);

                name.setText(employerModel.getName()+" "+employerModel.getSurName());
                email.setText(employerModel.getEmail());

                if (employerModel.getPhone()!=null){
                    phone.setText(employerModel.getPhone());
                }
                if (employerModel.getAbout()!=null){
                    about.setText(employerModel.getAbout());
                }
                if (employerModel.getWorkPlace()!=null){
                    workplace.setText(employerModel.getWorkPlace());
                }
                if (employerModel.getRole()!=null){
                    role.setText(employerModel.getRole());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the invitations list
                DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Employers")
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
                                Picasso.get().load(profilePictureUrl).into(image);
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
                database.getReference().child("Employers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EmployerModel employerModel = snapshot.getValue(EmployerModel.class);

                        name.setText(employerModel.getName()+" "+employerModel.getSurName());
                        email.setText(employerModel.getEmail());

                        if (employerModel.getPhone()!=null){
                            phone.setText(employerModel.getPhone());
                        }
                        if (employerModel.getAbout()!=null){
                            about.setText(employerModel.getAbout());
                        }
                        if (employerModel.getWorkPlace()!=null){
                            workplace.setText(employerModel.getWorkPlace());
                        }
                        if (employerModel.getRole()!=null){
                            role.setText(employerModel.getRole());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

}