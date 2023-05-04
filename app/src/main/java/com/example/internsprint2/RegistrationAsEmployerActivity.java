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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import Models.EmployerModel;

public class RegistrationAsEmployerActivity extends AppCompatActivity {

    EditText name;
    EditText surName;
    EditText workPlace;
    EditText email;
    EditText password;
    Button btnRegister;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_as_employer);




        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        workPlace = findViewById(R.id.workplace);
        surName = findViewById(R.id.surname);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String employerName = name.getText().toString();
        String employerEmail = email.getText().toString();
        String employerPassword = password.getText().toString();
        String employerSurName = surName.getText().toString();
        String employerWorkPlace = workPlace.getText().toString();


        if(TextUtils.isEmpty(employerName)){
            //Toast.makeText(MainActivity.this, "username cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(employerSurName)){
            //Toast.makeText(MainActivity.this, "username cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(employerWorkPlace)){
            //Toast.makeText(MainActivity.this, "username cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(employerEmail)){
            //Toast.makeText(MainActivity.this, "email cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(employerPassword)){
            //Toast.makeText(MainActivity.this, "password cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(employerEmail, employerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    EmployerModel employerModel = new EmployerModel(employerName, employerSurName, employerWorkPlace,employerEmail,employerPassword,id);
                    database.getReference().child("Employers").child(id).setValue(employerModel);
                    database.getReference().child("Employers").child(id).child("registeredUsers").setValue(employerModel.getRegUsers());


                    final HashMap<String,Object> cartMap = new HashMap<>();
                    cartMap.put("name", employerName);
                    cartMap.put("surname", employerSurName);
                    cartMap.put("email", employerEmail);
                    cartMap.put("workPlace", employerWorkPlace);
                    cartMap.put("id", id);
                    cartMap.put("registeredUsers", employerModel.getRegUsers());

                    firestore.collection("Employers").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(RegistrationAsEmployerActivity.this, "successfully registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    Intent intent = new Intent(RegistrationAsEmployerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegistrationAsEmployerActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}