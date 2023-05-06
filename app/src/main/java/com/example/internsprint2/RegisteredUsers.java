package com.example.internsprint2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Models.UserModel;

public class RegisteredUsers extends AppCompatActivity {
    FirebaseFirestore firestore;

    FirebaseDatabase database;
    RecyclerView recyclerViewUsers;
    UsersAdapters usersAdapters;
    List<UserModel> userModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users);

        database=FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userModelList=new ArrayList<>();
        recyclerViewUsers = findViewById(R.id.recyclerViewRegUsers);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        DatabaseReference ref = database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("registeredUsers");
        ArrayList<String> registeredUsers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    Log.i("asd",item);
                    registeredUsers.add(item);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref=database.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    UserModel item = childSnapshot.getValue(UserModel.class);


                    if(registeredUsers.contains(childSnapshot.getKey())){
                        item.setEmail(childSnapshot.child("email").getValue(String.class));
                        item.setName(childSnapshot.child("name").getValue(String.class));
                        item.setSurName(childSnapshot.child("surName").getValue(String.class));
                        item.setPassword(childSnapshot.child("password").getValue(String.class));
                        item.setId(childSnapshot.getKey());

                        userModelList.add(item);
                        usersAdapters.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersAdapters = new UsersAdapters(getApplicationContext(), userModelList);
        recyclerViewUsers.setAdapter(usersAdapters);
    }
}
