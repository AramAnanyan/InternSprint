package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import Models.EmployerModel;

import com.example.internsprint2.Profiles.EmployerProfileActivity;
import com.example.internsprint2.Profiles.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRegisterUser;
    Button btnRegisterEmployer;
    Button btnLogin;
    Button btnLoginUser;
    Button btnLoginEmployer;
    ProgressDialog progressDialog;


    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        if (auth.getCurrentUser() != null) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            database.getReference().child("Employers").child(auth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                    Intent intent;
                    try {
                        if (employerModel.getEmail() != null) {
                            intent = new Intent(MainActivity.this, EmployerProfileActivity.class);

                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }

                    } catch (Exception ex) {
                        intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                }

            });

        }

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        btnRegisterEmployer = findViewById(R.id.btnRegisterAsEmployer);
        btnRegisterUser = findViewById(R.id.btnRegisterAsUser);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginEmployer = findViewById(R.id.btnLoginEmployer);
        btnLoginUser = findViewById(R.id.btnLoginUser);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationAsUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegisterEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationAsEmployerActivity.class);
                startActivity(intent);
                finish();
            }
        });



        btnLoginEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword("aramo.ananyan@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                            database.getReference().child("Employers").child(auth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                                    Intent intent;
                                    try {
                                        if (employerModel.getEmail() != null) {
                                            intent = new Intent(MainActivity.this, EmployerProfileActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (Exception ex) {
                                        intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword("anush@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                            database.getReference().child("Employers").child(auth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                                    Intent intent;
                                    try {
                                        if (employerModel.getEmail() != null) {
                                            intent = new Intent(MainActivity.this, EmployerProfileActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (Exception ex) {
                                        intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


}
