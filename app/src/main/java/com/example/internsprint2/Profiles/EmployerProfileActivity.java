package com.example.internsprint2.Profiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internsprint2.ConfirmedUsersAdapter;
import com.example.internsprint2.EmployersActivity;
import com.example.internsprint2.MainActivity;
import com.example.internsprint2.MoreEmployerForLogged;
import com.example.internsprint2.R;
import com.example.internsprint2.RegisteredUsersAdapter;
import com.example.internsprint2.UsersActivity;
import com.example.internsprint2.UsersAdapters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Models.EmployerModel;
import Models.UserModel;

public class EmployerProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerViewRegUsers;
    RecyclerView recyclerViewConfUsers;
    RegisteredUsersAdapter usersAdapterReg;
    ConfirmedUsersAdapter usersAdapterConfirmed;
    List<UserModel> userModelListReg;
    List<UserModel> userModelListConfirmed;
    private Uri imagePath;

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employer_profile);
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        userModelListReg = new ArrayList<>();
        recyclerViewRegUsers = findViewById(R.id.recyclerViewRegUsers);
        recyclerViewRegUsers.setVisibility(View.VISIBLE);
        recyclerViewRegUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        usersAdapterReg = new RegisteredUsersAdapter(getApplicationContext(), userModelListReg);
        recyclerViewRegUsers.setAdapter(usersAdapterReg);

        userModelListConfirmed = new ArrayList<>();
        recyclerViewConfUsers = findViewById(R.id.recyclerViewConfirmedUsers);
        recyclerViewConfUsers.setVisibility(View.VISIBLE);
        recyclerViewConfUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        usersAdapterConfirmed = new ConfirmedUsersAdapter(getApplicationContext(), userModelListConfirmed);
        recyclerViewConfUsers.setAdapter(usersAdapterConfirmed);


        Button logout = findViewById(R.id.logout);
         image = findViewById(R.id.profileImage);
        Button more = findViewById(R.id.update);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.email);
        TextView about = findViewById(R.id.about);
        TextView phone = findViewById(R.id.phone);
        TextView role = findViewById(R.id.role);
        TextView workplace = findViewById(R.id.workplace);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        ImageView navigBar = findViewById(R.id.navigationBar);

        navigBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.users:
                        Intent intent = new Intent(EmployerProfileActivity.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.employers:
                        Intent intent2 = new Intent(EmployerProfileActivity.this, EmployersActivity.class);
                        startActivity(intent2);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        // startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        database.getReference().child("Employers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerModel employerModel = snapshot.getValue(EmployerModel.class);


                name.setText(employerModel.getName() + " " + employerModel.getSurName());
                email.setText(employerModel.getEmail());
                workplace.setText(employerModel.getWorkPlace());

                if (employerModel.getPhone() != null) {
                    phone.setText(employerModel.getPhone());
                }
                if (employerModel.getAbout() != null) {
                    about.setText(employerModel.getAbout());
                }
                if (employerModel.getWorkPlace() != null) {
                    workplace.setText(employerModel.getWorkPlace());
                }
                if (employerModel.getRole() != null) {
                    role.setText(employerModel.getRole());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Assuming you have a reference to the current employer's data in the Firebase Realtime Database
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Employers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        employerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profilePictureUrl = snapshot.child("profilePicture").getValue(String.class);
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        // Load the profile picture using the URL using your preferred image loading library
                        // For example, you can use Picasso or Glide
                        // Here's an example using Picasso:
                        Picasso.get().load(profilePictureUrl).into(image);
                    } else {
                        // Handle the case when the profile picture URL is empty or not available
                    }
                } else {
                    // Handle the case when the employer's data does not exist in the database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the database operation
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });


        retrieveData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the invitations list
                retrieveData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(EmployerProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerProfileActivity.this, MoreEmployerForLogged.class);
                intent.putExtra("id", auth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        }catch (IOException exception){
            exception.printStackTrace();
        }
        image.setImageBitmap(bitmap);
        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            FirebaseDatabase.getInstance().getReference("Employers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePicture").setValue(task.getResult().toString());

                        }
                    });
                    Toast.makeText(EmployerProfileActivity.this,"successfully updated",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(EmployerProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrieveData() {
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Employers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        employerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profilePictureUrl = snapshot.child("profilePicture").getValue(String.class);
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        // Load the profile picture using the URL using your preferred image loading library
                        // For example, you can use Picasso or Glide
                        // Here's an example using Picasso:
                        Picasso.get().load(profilePictureUrl).into(image);
                    } else {
                        // Handle the case when the profile picture URL is empty or not available
                    }
                } else {
                    // Handle the case when the employer's data does not exist in the database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during the database operation
            }
        });
        DatabaseReference ref = database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("confirmedUsers");
        DatabaseReference ref2 = database.getReference().child("Employers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("registeredUsers");
        ArrayList<String> confirmedUsers = new ArrayList<>();
        ArrayList<String> regUsers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                confirmedUsers.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    confirmedUsers.add(item);
                }

                DatabaseReference ref1 = database.getReference().child("Users");
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModelListConfirmed.clear();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            UserModel item = childSnapshot.getValue(UserModel.class);
                            if (confirmedUsers.contains(childSnapshot.getKey())) {
                                item.setName(childSnapshot.child("name").getValue(String.class));
                                item.setEmail(childSnapshot.child("email").getValue(String.class));
                                item.setSurName(childSnapshot.child("surName").getValue(String.class));
                                item.setPassword(childSnapshot.child("password").getValue(String.class));
                                item.setId(childSnapshot.getKey());

                                userModelListConfirmed.add(item);
                            }
                        }

                        usersAdapterConfirmed.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                regUsers.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    regUsers.add(item);
                    Log.i("asd", item);
                }

                DatabaseReference ref3 = database.getReference().child("Users");
                ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModelListReg.clear();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            UserModel item = childSnapshot.getValue(UserModel.class);
                            if (regUsers.contains(childSnapshot.getKey())) {
                                item.setEmail(childSnapshot.child("email").getValue(String.class));
                                item.setName(childSnapshot.child("name").getValue(String.class));
                                item.setSurName(childSnapshot.child("surName").getValue(String.class));
                                item.setPassword(childSnapshot.child("password").getValue(String.class));
                                item.setId(childSnapshot.getKey());

                                Log.i("asd", item.getName());
                                userModelListReg.add(item);
                            }
                        }

                        usersAdapterReg.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}