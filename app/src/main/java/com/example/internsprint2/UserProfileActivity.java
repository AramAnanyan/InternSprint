package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import Models.UserModel;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        auth=FirebaseAuth.getInstance();


        firestore=FirebaseFirestore.getInstance();

        UserModel user=new UserModel();

        TextView tv=findViewById(R.id.profileName);


        firestore.collection("Users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    user.setName(task.getResult().getString("userName"));
                } else {
                    Toast.makeText(getApplicationContext(), "Սխալ։" + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }


        });


        tv.setText(user.getName());
        Log.i("TAG","name: "+user.getName()+" , "+user.getSurName());

    }
}