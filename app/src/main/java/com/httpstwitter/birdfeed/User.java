package com.httpstwitter.birdfeed;

import java.util.ArrayList;
/**
 * Created by Casey on 3/13/17.
 * User object keeps track of user credentials, maybe?
 */

public class User {

    private String uid;
    private String password;
    private ArrayList<Boolean> options;

    public User() {

    }

    public User(String u, String p, ArrayList<Boolean> o) {
        uid = u;
        password = p;
        options = o;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Boolean> getOptions() {
        return options;
    }

    public void setUid(String u) {
        uid = u;
    }

    public void setPassword(String p) {
        password = p;
    }

    public void setOptions(ArrayList<Boolean> o) {
        options = o;
    }
}
