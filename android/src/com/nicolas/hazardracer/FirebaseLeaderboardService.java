package com.nicolas.hazardracer;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shark on 11/29/2017.
 */

public class FirebaseLeaderboardService implements LeaderboardService {
    private DatabaseReference db;
    ArrayList<HighScore> highScores;
    boolean success;

    public FirebaseLeaderboardService(){
        db = FirebaseDatabase.getInstance().getReference().child("Scores");
        highScores = new ArrayList<>();
    }

    @Override
    public boolean submitScore(String user, int score) {
        HighScore values = new HighScore();
        values.setUserName(user);
        values.setScore(new Integer(score));
        db.push().setValue(values, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    success = true;
                } else {
                    success = false;
                }
            }
        });
        return success;
    }

    @Override
    public ArrayList<HighScore> resetHighScores(){
        highScores = new ArrayList<>();
        return getTopTen();
    }

    @Override
    public ArrayList<HighScore> getTopTen() {
        db.orderByChild("score").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                HighScore hs = dataSnapshot.getValue(HighScore.class);
                Log.i("HIGHSCORE", "onChildAdded: "+hs);
                highScores.add(hs);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return highScores;
    }


    @Override
    public ArrayList<HighScore> getList() {
        return highScores;
    }
}
