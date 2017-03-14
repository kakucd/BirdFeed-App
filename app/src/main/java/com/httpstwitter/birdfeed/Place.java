package com.httpstwitter.birdfeed;

/**
 * Created by Casey on 3/8/17.
 */

public class Place {

    String name;
    Long score;
    String address;
    String hours = "";
    String tags = "";
    Long money = null;

    public Place() {
        //Needed for Firebase
    }

    public Place(String n, Long s, String a) {
        name = n;
        score = s;
        address = a;
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

    public String getHours() { return hours; }

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

    public void setHours(String h) { hours = h; }
}
