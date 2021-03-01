package com.example.mybicyclerental.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mybicyclerental.R;
import com.example.mybicyclerental.activity.admin.DashboardActivity;
import com.example.mybicyclerental.databinding.ActivityLoginBinding;
import com.example.mybicyclerental.databinding.ActivityRegisterBinding;
import com.example.mybicyclerental.fragment.HomeFragment;
import com.example.mybicyclerental.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding viewBinding;
    String email, pass;
    EditText edEmail,edPass;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        edEmail=(EditText)findViewById(R.id.email);
        edPass=(EditText)findViewById(R.id.pass);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        email=edEmail.getText().toString().trim();
        pass=edPass.getText().toString().trim();
        preferences=getSharedPreferences("user",MODE_PRIVATE);
        editor=preferences.edit();



        viewBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = viewBinding.email.getText().toString();
                pass = viewBinding.pass.getText().toString();

                boolean invalid=false;
                if (email.equals("")){
                    invalid=true;
                    edEmail.setError("Enter Valid Email");
                }
                if (pass.equals("")){
                    invalid=true;
                    edPass.setError("Enter Valid Password");
                }

                 if (invalid==false)

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (email.equals("admin12@gmail.com")){
                                        Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(intent);

                                    }
                                    else{
                                        FirebaseFirestore.getInstance().collection("USERS").whereEqualTo("email", email)
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        UserModel model = value.getDocuments().get(0).toObject(UserModel.class);
                                                        editor.putString("name", model.getName());
                                                        editor.putString("contactNumber", model.getContactNumber());
                                                        editor.putString("email", model.getEmail());
                                                        editor.putString("password", model.getPassword());
                                                        editor.apply();
                                                    }
                                                });
                                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, BottomNavActivity.class);
                                        startActivity(intent);
                                    }
                                } else
                                    Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        viewBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        viewBinding.btnForgat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgatActivity.class);
                startActivity(intent);
            }
        });
    }
}