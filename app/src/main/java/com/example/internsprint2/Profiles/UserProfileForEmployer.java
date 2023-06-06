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
import Models.UserModel;

public class UserProfileForEmployer extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_for_employer);
        Intent intent=getIntent();
        String id=intent.getStringExtra("EXTRA");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Button invite=findViewById(R.id.invite);
        ImageView image=findViewById(R.id.profileImage);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.email);
        TextView age = findViewById(R.id.age);
        TextView phone = findViewById(R.id.phone);
        TextView institution = findViewById(R.id.institution);
        TextView about = findViewById(R.id.about);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        ImageView navigBar = findViewById(R.id.navigationBar);

        invite.setEnabled(true);

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

                        Intent intent=new Intent(UserProfileForEmployer.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent2=new Intent(UserProfileForEmployer.this, EmployersActivity.class);
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
                                        intent = new Intent(UserProfileForEmployer.this, EmployerProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    intent = new Intent(UserProfileForEmployer.this, UserProfileActivity.class);
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
                name.setText(userModel.getName()+" "+userModel.getSurName());

                if (userModel.getPhone()!=null && !userModel.getPhone().isEmpty()){
                    phone.setText(userModel.getPhone());
                }
                if (userModel.getEmail()!=null && !userModel.getEmail().isEmpty()){
                    email.setText(userModel.getEmail());
                }
                if (userModel.getAbout()!=null && !userModel.getAbout().isEmpty()){
                    about.setText(userModel.getAbout());
                }
                if (userModel.getEducation()!=null && !userModel.getEducation().isEmpty()){
                    institution.setText(userModel.getEducation());
                }
                if (userModel.getAge()!=null && !userModel.getAge().isEmpty()){
                    age.setText(userModel.getAge());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Users")
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

        DatabaseReference ref = database.getReference().child("Users").child(id).child("invitations");
        ArrayList<String> invitations = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    invitations.add(item);

                }
                if (invitations.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    invite.setText("Invited");
                    invite.setBackgroundColor(Color.GRAY);
                    invite.setEnabled(false);
                }else{
                    invite.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Users")
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
                database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        name.setText(userModel.getName()+" "+userModel.getSurName());

                        if (userModel.getPhone()!=null && !userModel.getPhone().isEmpty()){
                            phone.setText(userModel.getPhone());
                        }
                        if (userModel.getEmail()!=null && !userModel.getEmail().isEmpty()){
                            email.setText(userModel.getEmail());
                        }
                        if (userModel.getAbout()!=null && !userModel.getAbout().isEmpty()){
                            about.setText(userModel.getAbout());
                        }
                        if (userModel.getEducation()!=null && !userModel.getEducation().isEmpty()){
                            institution.setText(userModel.getEducation());
                        }
                        if (userModel.getAge()!=null && !userModel.getAge().isEmpty()){
                            age.setText(userModel.getAge());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                invite.setText("Invited");
                invite.setBackgroundColor(Color.GRAY);
                invite.setEnabled(false);
                DatabaseReference ref = database.getReference().child("Users").child(id).child("invitations");
                ArrayList<String> invitations = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String item = childSnapshot.getValue(String.class);
                            invitations.add(item);

                        }
                        if (!invitations.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            invitations.add(currentUserUid);
                            ref.setValue(invitations);

                            //EmailSender.sendEmail(email.getText().toString(),"Request for Interview Registration","I hope this message finds you well. I am writing to kindly request your assistance in scheduling an interview for a candidate who has expressed keen interest in joining your company.\n" + "Candidate's Name: " + name[0] + " " + surname[0]+"\ncheck Registered Users in InternSprint to find him(her)"+"\n Thank you very much for your attention to this matter. We genuinely appreciate your time and consideration.");

                            Toast.makeText(UserProfileForEmployer.this,"successfully invited",Toast.LENGTH_SHORT).show();
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