package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.internsprint2.Profiles.EmployerProfileActivity;
import com.example.internsprint2.Profiles.EmployerProfileForEmployer;
import com.example.internsprint2.Profiles.EmployerProfileForUser;
import com.example.internsprint2.Profiles.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import Models.EmployerModel;


public class MoreEmployerForAll extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        setContentView(R.layout.activity_more_employer_for_all);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        EditText name = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText phone = findViewById(R.id.phone);
        EditText workplace = findViewById(R.id.workplace);
        EditText about = findViewById(R.id.about);
        Button back=findViewById(R.id.btnBack);


        name.setEnabled(false);
        surname.setEnabled(false);
        phone.setEnabled(false);
        workplace.setEnabled(false);
        about.setEnabled(false);
        database.getReference().child("Employers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                name.setText(employerModel.getName());
                surname.setText(employerModel.getSurName());
                if (employerModel.getPhone() != null) {
                    phone.setText(employerModel.getPhone());
                }
                if (employerModel.getAbout() != null) {
                    about.setText(employerModel.getAbout());
                }
                
                if (employerModel.getWorkPlace() != null) {
                    workplace.setText(employerModel.getWorkPlace());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Employers").child(auth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                        Intent intent;
                        try {

                            if (employerModel.getEmail() != null) {
                                intent = new Intent(MoreEmployerForAll.this, EmployerProfileForEmployer.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception ex) {
                            intent = new Intent(MoreEmployerForAll.this, EmployerProfileForUser.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
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