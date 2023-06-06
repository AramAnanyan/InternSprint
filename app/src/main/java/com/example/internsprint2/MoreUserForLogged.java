package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import Models.UserModel;

public class MoreUserForLogged extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_more_user_for_logged);
        EditText name=findViewById(R.id.name);
        EditText surname=findViewById(R.id.surname);

        EditText age=findViewById(R.id.age);
        EditText phone=findViewById(R.id.phone);
        EditText workplace=findViewById(R.id.workplace);
        EditText institution=findViewById(R.id.education);
        EditText about=findViewById(R.id.about);
        Button save=findViewById(R.id.btnSave);
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName());
                surname.setText(userModel.getSurName());
                if (userModel.getPhone()!=null){
                    phone.setText(userModel.getPhone());
                }
                if (userModel.getAbout()!=null){
                    about.setText(userModel.getAbout());
                }
                if (userModel.getEducation()!=null){
                    institution.setText(userModel.getEducation());
                }
                if (userModel.getAge()!=null){
                    age.setText(userModel.getAge());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firestore.collection("Users").whereEqualTo("userId",auth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                            // Access individual documents here
                                            String documentId = documentSnapshot.getId();

                                            Log.i("asd",documentId);
                                            // Update all fields of the document
                                            Map<String, Object> updatedData = new HashMap<>();
                                            updatedData.put("userName", name.getText().toString());
                                            updatedData.put("userSurName", surname.getText().toString());
                                            updatedData.put("age", age.getText().toString());
                                            updatedData.put("phone", phone.getText().toString());
                                            updatedData.put("education", institution.getText().toString());
                                            updatedData.put("workplace", workplace.getText().toString());
                                            updatedData.put("about", about.getText().toString());

                                            Map<String, Object> updatedData2 = new HashMap<>();
                                            updatedData2.put("name", name.getText().toString());
                                            updatedData2.put("surName", surname.getText().toString());
                                            updatedData2.put("age", age.getText().toString());
                                            updatedData2.put("phone", phone.getText().toString());
                                            updatedData2.put("education", institution.getText().toString());
                                            updatedData2.put("workplace", workplace.getText().toString());
                                            updatedData2.put("about", about.getText().toString());


                                            database.getReference("Users").child(auth.getCurrentUser().getUid())
                                                    .updateChildren(updatedData2, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            if (databaseError == null) {
                                                                // Document updated successfully
                                                                // ...
                                                            } else {
                                                                Toast.makeText(MoreUserForLogged.this,"error",Toast.LENGTH_SHORT).show();
                                                                // Error updating document
                                                                // Handle the error
                                                            }
                                                        }
                                                    });


                                            // Add more fields as needed

                                            // Update the document
                                            firestore.collection("Users").document(documentId)
                                                    .update(updatedData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(MoreUserForLogged.this,"successfully updated",Toast.LENGTH_SHORT).show();
                                                                // Document updated successfully
                                                                // ...
                                                            } else {
                                                                // Error updating document
                                                                Exception exception = task.getException();
                                                                // Handle the exception
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(MoreUserForLogged.this,"not found",Toast.LENGTH_SHORT).show();
                                        // No documents found
                                    }
                                } else {
                                    // Error getting documents
                                    Toast.makeText(MoreUserForLogged.this,"error",Toast.LENGTH_SHORT).show();

                                    Exception exception = task.getException();
                                    // Handle the exception
                                }
                            }
                        });
            }

        });






    }
}