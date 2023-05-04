package com.example.internsprint2.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.internsprint2.EmployersActivity;
import com.example.internsprint2.R;
import com.example.internsprint2.UsersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Models.EmployerModel;
import Models.UserModel;







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
        Button register = findViewById(R.id.reg);
        Button more = findViewById(R.id.more);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView surname = findViewById(R.id.profileSurName);
        TextView topName = findViewById(R.id.profileNameTop);
        TextView workplace = findViewById(R.id.workplace);
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


                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Employers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);


                name.setText(employerModel.getName());
                surname.setText(employerModel.getSurName());
                email.setText(employerModel.getEmail());
                topName.setText(employerModel.getName());
                //workplace.setText(employerModel.getName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


                        if(!registeredUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            registeredUsers.add(currentUserUid);
                            ref.setValue(registeredUsers);
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