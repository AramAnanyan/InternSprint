package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.EmployerModel;
import com.example.internsprint2.Profiles.EmployerProfileActivity;
import com.example.internsprint2.Profiles.UserProfileActivity;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseDatabase database;
    EditText email, password;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.btnLogin);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "please write your email", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "please write your password", Toast.LENGTH_SHORT);
            return;
        }
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "you are logged in", Toast.LENGTH_SHORT).show();
                    database.getReference().child("Employers").child(auth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                            Intent intent;
                            try {

                                if (employerModel.getEmail() != null) {
                                    intent = new Intent(LoginActivity.this, EmployerProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception ex) {


                                intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "error:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
