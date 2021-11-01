package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TweetActivity extends AppCompatActivity {

    EditText tweet;
    Button post;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public void Post(View view){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> tweetList = (ArrayList<String>)snapshot.child("Customer").child(mAuth.getUid()+"").child("tweet").getValue();
                tweetList.add(tweet.getText().toString());
                myRef.child("Customer").child(mAuth.getUid()+"").child("tweet").setValue(tweetList);
                myRef.removeEventListener(this);
                Toast.makeText(getApplicationContext(),"Tweet Posted!",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error while posting the tweet:(",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        tweet=findViewById(R.id.tweetEditText);
        post=findViewById(R.id.postButton);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://twitter-clone-c17d3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference();
    }
}