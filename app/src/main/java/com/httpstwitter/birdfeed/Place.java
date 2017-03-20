package com.httpstwitter.birdfeed;

/**
 * Created by Casey on 3/8/17.
 * Class used to store information about restaurants queried from the database
 * Mostly used for testing purposes
 * Will most likely delete upon app completion
 */

public class Place {

    String name;
    Long score;
    String address;
    String tags;
    Long money;
    String website;
    String phone;

    public Place() {
        //Needed for Firebase
    }

    public Place(String n, Long s, String a, String w, String p, String t, Long m) {
        name = n;
        score = s;
        address = a;
        website = w;
        phone = p;
        tags = t;
        money = m;
    }

    public String getName() {
        return name;
    }

    public String getAdd() {
        return address;
    }

    public Long getScore() {
        return score;
    }

    public Long getMoney() { return money; }

    public String getTags() { return tags; }

    public void setName(String n) {
        name = n;
    }

    public void setTags(String t) { tags = t; }

    public void setMoney(Long m) { money = m; }

    public void setAdd(String a) {
        address = a;
    }

    public void setScore(Long s) {
        score = s;
    }
}
