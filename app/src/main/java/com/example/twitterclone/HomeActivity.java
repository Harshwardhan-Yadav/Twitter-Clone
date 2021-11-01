package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

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

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public ListView listView;
    public ArrayList<String> users;
    public ArrayList<String> UIDS;
    public ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.tweet:
                startActivity(new Intent(this,TweetActivity.class));
                break;
            case R.id.feed:
                startActivity(new Intent(this,FeedActivity.class));
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView=findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-c17d3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new ArrayList<>();
                UIDS = new ArrayList<>();
                Map map = (HashMap)snapshot.child("Customer").child(mAuth.getUid()+"").getValue();
                setTitle(map.get("username")+"");
                Map map1 = (HashMap)snapshot.child("Customer").getValue();
                for(Object i:map1.keySet()){
                    System.out.println(i.toString());
                    if(i.toString().compareTo(mAuth.getUid()+"")!=0){
                        Map temp = (Map) map1.get(i);
                        users.add((String) temp.get("username"));
                        UIDS.add((String)i);
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked,users);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CheckedTextView checkedTextView = (CheckedTextView) view;
                        if(checkedTextView.isChecked()){
                            //User is checked.
                            Map temp = (Map) map1.get(mAuth.getUid()+"");
                            ArrayList<String> frnds = (ArrayList<String>) temp.get("friends");
                            if(!frnds.contains(UIDS.get(i)))
                                frnds.add(UIDS.get(i));
                            myRef.child("Customer").child(mAuth.getUid()+"").child("friends").setValue(frnds);
                            System.out.println("checked");

                        } else {
                            //user is not checked.
                            Map temp = (Map) map1.get(mAuth.getUid()+"");
                            ArrayList<String> frnds = (ArrayList<String>) temp.get("friends");
                            if(frnds.contains(UIDS.get(i)))
                                frnds.remove(UIDS.get(i));
                            myRef.child("Customer").child(mAuth.getUid()+"").child("friends").setValue(frnds);
                            System.out.println("Not Checked");
                            System.out.println(users.size());
                        }
                    }
                });
                int pos=0;
                for(String i:UIDS){
                    for(String j:(ArrayList<String>) map.get("friends")){
                        if(j.compareTo(i)==0 && !listView.isItemChecked(pos)){
                            listView.setItemChecked(pos,true);
                        }
                    }
                    pos++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Read Error: ","Error while reading from database!");
            }
        });

    }
}