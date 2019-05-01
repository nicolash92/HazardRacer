package com.nicolas.hazardracer;

/**
 * Created by shark on 11/29/2017.
 */

public class HighScore {

    private String userName;
    private Integer score;

    public HighScore(){
        userName = "test";
        score = 0;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return this.userName+"  "+this.score;
    }
}
