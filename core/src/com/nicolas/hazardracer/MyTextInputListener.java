package com.nicolas.hazardracer;

import com.badlogic.gdx.Input;

/**
 * Created by shark on 11/29/2017.
 */

public class MyTextInputListener implements Input.TextInputListener {
    HazardRacer game;
    public MyTextInputListener(HazardRacer game) {
        this.game = game;
    }

    @Override
    public void input (String text) {
        game.leaderboardService.submitScore(text , game.getScore());
        game.getHighScores();
        game.goToMainMenu();
    }

    @Override
    public void canceled () {
        game.goToMainMenu();
    }
}
