package com.nicolas.hazardracer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shark on 11/29/2017.
 */

public interface LeaderboardService {

    public boolean submitScore(String user, int score);

    public ArrayList<HighScore> getTopTen();

    public ArrayList<HighScore> getList();

    public  ArrayList<HighScore> resetHighScores();
}
