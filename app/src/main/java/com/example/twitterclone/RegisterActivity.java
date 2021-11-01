package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText registerFullName;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerUsername;
    Button registerButton;
    private FirebaseAuth mAuth;


    public void signUp(String email,String password,String fullame, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Sign Up", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-c17d3-default-rtdb.asia-southeast1.firebasedatabase.app/");
                            DatabaseReference myRef = database.getReference();
                            User user1 = new User(fullame,username,email,new ArrayList<String>(),new ArrayList<>());
                            myRef.child("Customer").child(mAuth.getUid()+"").setValue(user1);
                            Toast.makeText(RegisterActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Sign Up", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed. "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(View view){
        boolean fn = false,un=false,em=false,pa=false;
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        String fullname = registerFullName.getText().toString().trim();
        String username = registerUsername.getText().toString().trim();
        if(fullname.isEmpty()){
            registerFullName.setError("Name cannot be empty!");
            registerFullName.requestFocus();
        } else fn = true;
        if(username.isEmpty()){
            registerUsername.setError("Username cannot be empty!");
            registerUsername.requestFocus();
        } else un = true;
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Enter a valid E-mail!");
            registerEmail.requestFocus();
        } else{
            em = true;
        }
        if(password.length()>=8){
            pa = true;
        } else{
            registerPassword.setError("Password must be atleast 8 characters long!");
            registerPassword.requestFocus();
        }
        if(fn && un && em && pa){
            signUp(email,password,fullname,username);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        registerButton = findViewById(R.id.registerButton);
        registerFullName = findViewById(R.id.registerFullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        mAuth = FirebaseAuth.getInstance();
    }
}