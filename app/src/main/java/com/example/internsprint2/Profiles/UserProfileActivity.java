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

import com.example.internsprint2.EmployersActivity;
import com.example.internsprint2.InvitationsAdapter;
import com.example.internsprint2.MainActivity;
import com.example.internsprint2.MoreUserForLogged;
import com.example.internsprint2.R;
import com.example.internsprint2.UsersActivity;
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

public class UserProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerViewInvitations;
    InvitationsAdapter invitationsAdapter;
    List<EmployerModel> employerModelList;
    TextView name ;
    TextView email ;
    TextView age;
    TextView about ;
    TextView institution;
    TextView phone ;
    TextView emptyText ;
    ImageView image;
    private Uri imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        employerModelList = new ArrayList<>();
        setContentView(R.layout.activity_user_profile);
        recyclerViewInvitations = findViewById(R.id.recyclerViewInvitations);
        recyclerViewInvitations.setVisibility(View.VISIBLE);
        recyclerViewInvitations.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


        image = findViewById(R.id.profileImage);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        invitationsAdapter = new InvitationsAdapter(getApplicationContext(), employerModelList);

        recyclerViewInvitations.setAdapter(invitationsAdapter);

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        Button logout=findViewById(R.id.logout);
        Button more=findViewById(R.id.update);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        about = findViewById(R.id.about);
        institution = findViewById(R.id.institution);
        phone = findViewById(R.id.phone);
        emptyText = findViewById(R.id.emptyStateText);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);

        ImageView navigBar = findViewById(R.id.navigationBar);



        navigBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if(employerModelList.isEmpty()){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }


        retrieveInvitations();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.users:

                        Intent intent=new Intent(UserProfileActivity.this, UsersActivity.class);
                        startActivity(intent);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.employers:
                        Intent intent2=new Intent(UserProfileActivity.this, EmployersActivity.class);
                        startActivity(intent2);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName()+" "+userModel.getSurName());

                if (userModel.getPhone()!=null && !userModel.getPhone().isEmpty()){
                    phone.setText(userModel.getPhone());
                }
                if (userModel.getEmail()!=null && !userModel.getEmail().isEmpty()){
                    email.setText(userModel.getEmail());
                }
                if (userModel.getAbout()!=null && !userModel.getAbout().isEmpty()){
                    about.setText(userModel.getAbout());
                }
                if (userModel.getEducation()!=null && !userModel.getEducation().isEmpty()){

                    institution.setText(userModel.getEducation());
                }
                if (userModel.getAge()!=null && !userModel.getAge().isEmpty()){
                    age.setText(userModel.getAge());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the invitations list
                invitationsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                if(employerModelList.isEmpty()){
                    emptyText.setVisibility(View.VISIBLE);
                }else{
                    emptyText.setVisibility(View.GONE);
                }
                retrieveInvitations();
                swipeRefreshLayout.setRefreshing(false);
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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent=new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileActivity.this, MoreUserForLogged.class);
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
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePicture").setValue(task.getResult().toString());

                        }
                    });
                    Toast.makeText(UserProfileActivity.this,"successfully updated",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(UserProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






    private void retrieveInvitations() {
        DatabaseReference employerRef = FirebaseDatabase.getInstance().getReference("Users")
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
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName()+" "+userModel.getSurName());

                if (userModel.getPhone()!=null && !userModel.getPhone().isEmpty()){
                    phone.setText(userModel.getPhone());
                }
                if (userModel.getEmail()!=null && !userModel.getEmail().isEmpty()){
                    email.setText(userModel.getEmail());
                }
                if (userModel.getAbout()!=null && !userModel.getAbout().isEmpty()){
                    about.setText(userModel.getAbout());
                }
                if (userModel.getEducation()!=null && !userModel.getEducation().isEmpty()){

                    institution.setText(userModel.getEducation());
                }
                if (userModel.getAge()!=null && !userModel.getAge().isEmpty()){
                    age.setText(userModel.getAge());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitations");
        ArrayList<String> invitations = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invitations.clear(); // Clear the existing list

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    Log.i("asd", item);
                    invitations.add(item);
                }

                DatabaseReference employersRef = database.getReference().child("Employers");
                employersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        employerModelList.clear(); // Clear the existing list

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            EmployerModel item = childSnapshot.getValue(EmployerModel.class);

                            if (invitations.contains(childSnapshot.getKey())) {
                                item.setEmail(childSnapshot.child("email").getValue(String.class));
                                item.setName(childSnapshot.child("name").getValue(String.class));
                                item.setSurName(childSnapshot.child("surName").getValue(String.class));
                                item.setPassword(childSnapshot.child("password").getValue(String.class));
                                item.setId(childSnapshot.getKey());
                                employerModelList.add(item);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle cancelled event
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled event
            }
        });
    }

}