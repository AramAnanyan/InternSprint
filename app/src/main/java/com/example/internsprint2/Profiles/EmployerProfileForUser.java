package com.example.internsprint2.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import Models.EmployerModel;


public class EmployerProfileForUser extends AppCompatActivity {
    FirebaseDatabase database;


    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile_for_user);
        Intent intent = getIntent();
        String id = intent.getStringExtra("EXTRA");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        drawerLayout=findViewById(R.id.drawerLayout);
        ImageView image=findViewById(R.id.profileImage);
        Button register = findViewById(R.id.btnRegister);
        register.setEnabled(true);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.email);
        TextView workplace = findViewById(R.id.workplace);
        TextView phone = findViewById(R.id.phone);
        TextView about = findViewById(R.id.about);
        TextView role = findViewById(R.id.role);

        navigationView = findViewById(R.id.nav);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
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

                        Intent intent = new Intent(EmployerProfileForUser.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent2 = new Intent(EmployerProfileForUser.this, EmployersActivity.class);
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
                                        intent = new Intent(EmployerProfileForUser.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(EmployerProfileForUser.this, UserProfileActivity.class);
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


                name.setText(employerModel.getName() + " " + employerModel.getSurName());
                email.setText(employerModel.getEmail());

                if (employerModel.getPhone() != null) {
                    phone.setText(employerModel.getPhone());
                }
                if (employerModel.getAbout() != null) {
                    about.setText(employerModel.getAbout());
                }
                if (employerModel.getWorkPlace() != null) {
                    workplace.setText(employerModel.getWorkPlace());
                }
                if (employerModel.getRole() != null) {
                    role.setText(employerModel.getRole());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        DatabaseReference ref = database.getReference().child("Employers").child(id).child("registeredUsers");


        ArrayList<String> registeredUsers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    registeredUsers.add(item);

                }
                if (registeredUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    register.setText("Registered");
                    register.setBackgroundColor(Color.GRAY);
                    register.setEnabled(false);

                } else {

                    register.setEnabled(true);
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


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatabaseReference ref = database.getReference().child("Employers").child(id).child("registeredUsers");


                ArrayList<String> registeredUsers = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String item = childSnapshot.getValue(String.class);
                            registeredUsers.add(item);
                        }
                        if (!registeredUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            registeredUsers.add(currentUserUid);
                            ref.setValue(registeredUsers);

                            //EmailSender.sendEmail(email.getText().toString(),"Request for Interview Registration","I hope this message finds you well. I am writing to kindly request your assistance in scheduling an interview for a candidate who has expressed keen interest in joining your company.\n" + "Candidate's Name: " + name[0] + " " + surname[0]+"\ncheck Registered Users in InternSprint to find him(her)"+"\n Thank you very much for your attention to this matter. We genuinely appreciate your time and consideration.");

                            Toast.makeText(EmployerProfileForUser.this, "successfully registered", Toast.LENGTH_SHORT).show();
                            register.setText("Registered");
                            register.setBackgroundColor(Color.GRAY);
                            register.setEnabled(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }

        });

    }
}