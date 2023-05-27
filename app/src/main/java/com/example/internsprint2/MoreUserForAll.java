package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.internsprint2.Profiles.EmployerProfileForEmployer;
import com.example.internsprint2.Profiles.EmployerProfileForUser;
import com.example.internsprint2.Profiles.UserProfileForEmployer;
import com.example.internsprint2.Profiles.UserProfileForUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import Models.EmployerModel;
import Models.UserModel;



public class MoreUserForAll extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        setContentView(R.layout.activity_more_user_for_all);
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        EditText name = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText age = findViewById(R.id.age);
        EditText phone = findViewById(R.id.phone);
        EditText workplace = findViewById(R.id.workplace);
        EditText institution = findViewById(R.id.education);
        EditText about = findViewById(R.id.about);

        name.setEnabled(false);
        surname.setEnabled(false);
        phone.setEnabled(false);
        age.setEnabled(false);
        workplace.setEnabled(false);
        institution.setEnabled(false);
        about.setEnabled(false);
        Button back=findViewById(R.id.btnBack);
        database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName());
                surname.setText(userModel.getSurName());
                if (userModel.getPhone() != null) {
                    phone.setText(userModel.getPhone());
                }
                if (userModel.getAbout() != null) {
                    about.setText(userModel.getAbout());
                }
                if (userModel.getEducation() != null) {
                    institution.setText(userModel.getEducation());
                }
                if (userModel.getAge() != null) {
                    age.setText(userModel.getAge());
                }
                if (userModel.getWorkplace() != null) {
                    workplace.setText(userModel.getWorkplace());
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
                                intent = new Intent(MoreUserForAll.this, UserProfileForEmployer.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception ex) {
                            intent = new Intent(MoreUserForAll.this, UserProfileForUser.class);
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