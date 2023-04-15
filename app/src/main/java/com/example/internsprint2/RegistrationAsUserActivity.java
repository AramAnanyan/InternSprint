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

import Models.UserModel;

public class RegistrationAsUserActivity extends AppCompatActivity {
    EditText name;
    EditText surName;

    EditText email;
    EditText password;
    EditText surname;
    Button btnRegister;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_as_user);




        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        surname = findViewById(R.id.surname);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userSurName = surname.getText().toString();



        if(TextUtils.isEmpty(userName)){
            //Toast.makeText(MainActivity.this, "username cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userSurName)){
            //Toast.makeText(MainActivity.this, "username cant be null", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userEmail)){
            //Toast.makeText(MainActivity.this, "email cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            //Toast.makeText(MainActivity.this, "password cant be null", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegistrationAsUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    UserModel userModel = new UserModel(userName, userEmail, userPassword, userSurName);
                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    database.getReference().child("Users").child(id).setValue(userModel);

                    final HashMap<String,Object> cartMap = new HashMap<>();
                    cartMap.put("userName", userName);
                    cartMap.put("userEmail", userEmail);
                    cartMap.put("userSurName", userSurName);
                    cartMap.put("userBiography", "es lav tgha em");


                    firestore.collection("Users").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(RegistrationAsUserActivity.this, "successfully registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


                }
                else{
                    Toast.makeText(RegistrationAsUserActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}