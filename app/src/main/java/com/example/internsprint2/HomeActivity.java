package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference usersRef;



    FirebaseAuth auth;
    RecyclerView recyclerViewUsers;

    UsersAdapters usersAdapters;
    List<UserModel> userModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        //setUpRecycleView();


        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);


        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        userModelList = new ArrayList<>();
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        userModelList.add(userModel);
                        usersAdapters.notifyDataSetChanged();


                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Սխալ։" + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);

        recyclerViewUsers.setAdapter(usersAdapters);


    }
    }
    /*private void setUpRecycleView(){
        Query query =usersRef.orderBy("userName",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        usersAdapters=new UsersAdapters(options);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setAdapter(usersAdapters);
    }
    protected void onStart(){
        super.onStart();
        usersAdapters.startListening();
    }
    protected void onStop(){
        super.onStop();
        usersAdapters.stopListening();
    }*/


