package com.nicolas.hazardracer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

import sun.rmi.runtime.Log;

/**
 * Created by shark on 11/20/2017.
 */

public class HazardRacer extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public GameScreen gameScreen;
    public InputProcessor inpoutPorcessor;
    public LeaderboardService leaderboardService;
    public ArrayList<HighScore> highScores;
    private Sound click;


    private int score;

    //options
    public boolean music=true, sfx=true, tiltControl=true;
    public boolean hardMode = false;

    @Override
    public void create() {

        batch = new SpriteBatch();
        inpoutPorcessor = Gdx.input.getInputProcessor();
        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        //Use LibGDX's default font.
        font = new BitmapFont();
        font.setColor(Color.ROYAL);
        font.getData().setScale(4.0f);
        goToMainMenu();
        getHighScores();
    }

    @Override
    public void render() {
        super.render();
    }

    public void playClick(){
        click.play(0.2f);
    }

    @Override
    public void dispose() {
        if (batch != null)
            batch.dispose();
        if (font != null)
            font.dispose();
        if (gameScreen != null)
            gameScreen.dispose();
        super.dispose();
    }

    public void goToMainMenu(){
        this.setScreen(new MainMenuScreen(this));
        this.setScore(0);
    }
    public void goToHighScore(){
        this.setScreen(new HighscoreScreen(this));
        this.setScore(0);
    }

    public void goToGame(){
        this.setScreen(new GameScreen(this));
    }

    public void exit() {
        Gdx.app.exit();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public HazardRacer setLeaderboardService(com.nicolas.hazardracer.LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
        return this;
    }

    public void getHighScores() {
        highScores = leaderboardService.resetHighScores();
    }
}
