package com.example.twitterclone;

import java.util.ArrayList;

public class User {
    public String fullname;
    public String username;
    public String email;
    public ArrayList<String> tweet;
    public ArrayList<String> friends;
    User(){

    }
    User(String fullname,String username,String email,ArrayList<String> tweet,ArrayList<String> friends){
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.tweet = tweet;
        this.tweet.add("start");
        this.friends = friends;
        this.friends.add("start");
    }

}
