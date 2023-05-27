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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import Models.EmployerModel;


public class MoreEmployerForLogged extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_more_employer_for_logged);
        EditText name=findViewById(R.id.name);
        EditText surname=findViewById(R.id.surname);
        EditText phone=findViewById(R.id.phone);
        EditText workplace=findViewById(R.id.workplace);
        EditText role=findViewById(R.id.role);
        EditText about=findViewById(R.id.about);
        Button save=findViewById(R.id.btnSave);
        database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);
                name.setText(employerModel.getName());
                surname.setText(employerModel.getSurName());
                if (employerModel.getPhone()!=null){
                    phone.setText(employerModel.getPhone());
                }
                if (employerModel.getAbout()!=null){
                    about.setText(employerModel.getAbout());
                }
                if (employerModel.getWorkPlace()!=null){
                    workplace.setText(employerModel.getWorkPlace());
                }
                if (employerModel.getRole()!=null){
                    role.setText(employerModel.getRole());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firestore.collection("Employers").whereEqualTo("id",auth.getCurrentUser().getUid())
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
                                            updatedData.put("name", name.getText().toString());
                                            updatedData.put("surname", surname.getText().toString());
                                            updatedData.put("phone", phone.getText().toString());
                                            updatedData.put("workplace", workplace.getText().toString());
                                            updatedData.put("role", role.getText().toString());
                                            updatedData.put("about", about.getText().toString());

                                            Map<String, Object> updatedData2 = new HashMap<>();
                                            updatedData2.put("name", name.getText().toString());
                                            updatedData2.put("surName", surname.getText().toString());
                                            updatedData2.put("phone", phone.getText().toString());
                                            updatedData2.put("workPlace", workplace.getText().toString());
                                            updatedData2.put("role", role.getText().toString());
                                            updatedData2.put("about", about.getText().toString());


                                            database.getReference("Employers").child(auth.getCurrentUser().getUid())
                                                    .updateChildren(updatedData2, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            if (databaseError == null) {
                                                                // Document updated successfully
                                                                // ...
                                                            } else {
                                                                Toast.makeText(MoreEmployerForLogged.this,"error",Toast.LENGTH_SHORT).show();
                                                                // Error updating document
                                                                // Handle the error
                                                            }
                                                        }
                                                    });


                                            // Add more fields as needed

                                            // Update the document
                                            firestore.collection("Employers").document(documentId)
                                                    .update(updatedData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(MoreEmployerForLogged.this,"successfully updated",Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(MoreEmployerForLogged.this,"not found",Toast.LENGTH_SHORT).show();
                                        // No documents found
                                    }
                                } else {
                                    // Error getting documents
                                    Toast.makeText(MoreEmployerForLogged.this,"error",Toast.LENGTH_SHORT).show();

                                    Exception exception = task.getException();
                                    // Handle the exception
                                }
                            }
                        });
            }

        });






    }
}