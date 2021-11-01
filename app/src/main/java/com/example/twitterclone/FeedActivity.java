package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.feedActivityListView);
        List<Map<String,String>> feeds = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-c17d3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                ArrayList<String> friends = (ArrayList<String>) snapshot.child("Customer").child(mAuth.getUid()+"").child("friends").getValue();
                for(String i:friends){
                    String username = (String) snapshot.child("Customer").child(i+"").child("username").getValue();
                    System.out.println(i);
                    ArrayList<String> tweets =  (ArrayList<String>) snapshot.child("Customer").child(i+"").child("tweet").getValue();
                    if(tweets == null){
                        System.out.println("Null for"+i);
                        continue;
                    }
                    for(String j: tweets){
                        if(j.compareTo("start")==0) continue;
                        Map<String,String> temp = new HashMap();
                        temp.put("username",username);
                        temp.put("tweet",j);
                        feeds.add(temp);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),feeds, android.R.layout.simple_list_item_2,new String[]{"username","tweet"},new int[]{android.R.id.text1,android.R.id.text2});
                    listView.setAdapter(simpleAdapter);
                    myRef.removeEventListener(this);
                }} catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Empty Feed",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error while loading feeds",Toast.LENGTH_SHORT).show();
            }
        });
    }
}