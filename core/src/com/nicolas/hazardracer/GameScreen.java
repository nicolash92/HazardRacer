package com.nicolas.hazardracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shark on 11/20/2017.
 */

class GameScreen implements Screen {
    private Sound explosion;
    private float cX = 0, turnSpeed = 10;
    final HazardRacer game;
    int score;

    Texture playerTexture;
    Texture smallAstroidTexture;
    Texture largeAstroidTexture;
    Texture explosionTexture;
    Texture grassTexture;
    Texture roadTexture;


    Music enginLoop;
    Music music;


    Rectangle playerRectangle;
    ArrayList<Rectangle> smallHazards, largeHazards, grass, road;


    float gameSpeed;
    int turn;
    private long lastGrassTime;
    private long lastSmallHazardTime;
    private long lastLargeHazardTime;
    private boolean exploded = false;
    private long explosionTimer;

    public GameScreen(HazardRacer game) {
        this.game = game;
        Gdx.input.setInputProcessor(game.inpoutPorcessor);
        score = 0;
        if (game.gameScreen != null) {
            game.gameScreen.dispose();
        }
        game.gameScreen = this;

        playerTexture = new Texture(Gdx.files.internal("red_racer.png"));
        smallAstroidTexture = new Texture(Gdx.files.internal("astroid_s.png"));
        largeAstroidTexture = new Texture(Gdx.files.internal("astroid_l.png"));
        explosionTexture = new Texture(Gdx.files.internal("explosion.png"));
        grassTexture = new Texture(Gdx.files.internal("grass.png"));
        roadTexture = new Texture(Gdx.files.internal("road.png"));
        if (game.sfx) {
            enginLoop = Gdx.audio.newMusic(Gdx.files.internal("engine_loop.wav"));
            enginLoop.setLooping(true);
            enginLoop.setVolume(0.2f);
            explosion =Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        }
        if (game.music) {
            music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
            music.setLooping(true);
            music.setVolume(0.2f);
        }

        playerRectangle = new Rectangle();
        playerRectangle.width = playerTexture.getWidth();
        playerRectangle.height = playerTexture.getHeight();
        smallHazards = new ArrayList<Rectangle>();
        largeHazards = new ArrayList<Rectangle>();
        grass = new ArrayList<Rectangle>();
        road = new ArrayList<Rectangle>();

        if(game.hardMode)
            gameSpeed = 12;
        else
            gameSpeed = 15;
        createBackground();
    }

    @Override
    public void show() {
        calibrate();
        playerRectangle.setX(Gdx.graphics.getWidth() / 2 - playerRectangle.getWidth() / 2);
        if (game.sfx) enginLoop.play();
        if (game.music) music.play();
    }

    public void calibrate() {
        cX = Gdx.input.getAccelerometerX();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.hardMode)
            setGameSpeed();

        if (exploded) {
            game.batch.begin();
            for (Rectangle r : road) {
                game.batch.draw(roadTexture, r.getX(), r.getY(), r.getWidth(), r.getHeight());
            }
            for (Rectangle g : grass) {
                game.batch.draw(grassTexture, g.getX(), g.getY(), g.getWidth(), g.getHeight());
            }
            if (TimeUtils.millis() - explosionTimer < 1000) {
                game.batch.draw(explosionTexture, playerRectangle.x, playerRectangle.y);
            }

            for (Rectangle hazard : smallHazards) {
                game.batch.draw(smallAstroidTexture, hazard.getX(), hazard.getY());
            }
            for (Rectangle hazard : largeHazards) {
                game.batch.draw(largeAstroidTexture, hazard.getX(), hazard.getY());
            }

            game.font.draw(game.batch, "Score: " + game.getScore(), Gdx.graphics.getWidth() / 50, Gdx.graphics.getHeight() - game.font.getLineHeight());

            game.batch.end();
        } else {
            if (Gdx.input.isTouched()) {

                if (Gdx.input.getX() < playerRectangle.getX()) {
                    turn = -1;
                } else if (Gdx.input.getX() > (playerRectangle.getX() + playerRectangle.getWidth())) {
                    turn = 1;
                } else {
                    turn = 0;
                }
            } else {
                turn = 0;
            }

            if (game.tiltControl) {
                if ((cX - Gdx.input.getAccelerometerX()) > 2f)
                    turn = 1;
                if ((cX - Gdx.input.getAccelerometerX()) < -2f)
                    turn = -1;
            }

            playerRectangle.setX(playerRectangle.getX() + (turn * turnSpeed));

            if (playerRectangle.getX() < 0)
                playerRectangle.setX(0f);

            if (playerRectangle.getX() > Gdx.graphics.getWidth() - playerRectangle.getWidth())
                playerRectangle.setX(Gdx.graphics.getWidth() - playerRectangle.getWidth());

            game.batch.begin();
            for (Rectangle r : road) {
                game.batch.draw(roadTexture, r.getX(), r.getY(), r.getWidth(), r.getHeight());
            }
            for (Rectangle g : grass) {
                game.batch.draw(grassTexture, g.getX(), g.getY(), g.getWidth(), g.getHeight());
            }
            game.batch.draw(playerTexture, playerRectangle.x, playerRectangle.y);

            for (Rectangle hazard : smallHazards) {
                game.batch.draw(smallAstroidTexture, hazard.getX(), hazard.getY());
            }
            for (Rectangle hazard : largeHazards) {
                game.batch.draw(largeAstroidTexture, hazard.getX(), hazard.getY());
            }

            game.font.draw(game.batch, "Score: " + game.getScore(), Gdx.graphics.getWidth() / 50, Gdx.graphics.getHeight() - game.font.getLineHeight());

            game.batch.end();

            if (TimeUtils.millis() - lastSmallHazardTime > 700) spawSmallHazard();
            if (TimeUtils.millis() - lastLargeHazardTime > 1500) spawLargeHazard();
            Iterator<Rectangle> smallHazardIterator = smallHazards.iterator();
            Iterator<Rectangle> largeHazardIterator = largeHazards.iterator();
            Iterator<Rectangle> grassIterator = grass.iterator();
            Iterator<Rectangle> roadIterator = road.iterator();

            while (smallHazardIterator.hasNext()) {
                Rectangle hazard = smallHazardIterator.next();
                hazard.setY(hazard.getY() - gameSpeed);// Gdx.graphics.getDeltaTime());
                if (hazard.getY() + hazard.getHeight() < 0) {
                    smallHazardIterator.remove();
                    game.setScore(game.getScore() + 1);
                }
                if (hazard.overlaps(playerRectangle)) {
                    Gdx.input.vibrate(500);
                    smallHazardIterator.remove();
                    game.setScore(game.getScore() - 1);

                }
            }

            while (largeHazardIterator.hasNext()) {
                Rectangle hazard = largeHazardIterator.next();
                hazard.setY(hazard.getY() - gameSpeed);// Gdx.graphics.getDeltaTime());
                if (hazard.getY() + hazard.getHeight() < 0) {
                    largeHazardIterator.remove();
                    game.setScore(game.getScore() + 5);
                }
                if (hazard.overlaps(playerRectangle)) {
                    Gdx.input.vibrate(500);
                    explode();

                }
            }

            while (roadIterator.hasNext()) {
                Rectangle road = roadIterator.next();
                road.setY(road.getY() - gameSpeed);
                if (road.getY() + road.getHeight() < 0)
                    road.setY(Gdx.graphics.getHeight());
            }

            while (grassIterator.hasNext()) {
                Rectangle grass = grassIterator.next();
                grass.setY(grass.getY() - gameSpeed);
                if (grass.getY() + grass.getHeight() < 0)
                    grass.setY(Gdx.graphics.getHeight());
                if (grass.overlaps(playerRectangle)) {
                    Gdx.input.vibrate(20);
                    if (TimeUtils.millis() - lastGrassTime > 100) {
                        game.setScore(game.getScore() - 1);
                        lastGrassTime = TimeUtils.millis();
                    }
                }
            }
        }

    }

    private void setGameSpeed() {
        gameSpeed = 12f+ (float) game.getScore()/50.0f;
        if(gameSpeed<12f)
            gameSpeed = 12f;
        else if(gameSpeed>50f)
            gameSpeed = 50f;
    }

    private void explode() {
        exploded = true;
        if (game.sfx) {
            music.stop();
            enginLoop.stop();
            explosion.play();
        }
        explosionTimer = TimeUtils.millis();
        MyTextInputListener listener = new MyTextInputListener(game);

        Gdx.input.getTextInput(listener, "Do you Want to Save your Score?", "", "Enter Your Name");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerTexture.dispose();
        smallAstroidTexture.dispose();
        largeAstroidTexture.dispose();
        roadTexture.dispose();
        grassTexture.dispose();
        if (music != null)
            music.dispose();
        if (enginLoop != null)
            enginLoop.dispose();
    }

    private void createBackground() {
        float bot = 0f;
        float xPos = Gdx.graphics.getWidth() / 5;
        float height = roadTexture.getHeight();
        float rWidth = Gdx.graphics.getWidth() * (3f / 5f);
        do {
            road.add(new Rectangle(xPos, bot, rWidth, height));
            bot += height/2;
        } while (bot < Gdx.graphics.getHeight() + height);
        bot = 0f;
        do {
            grass.add(new Rectangle(0f, bot, xPos, height));
            grass.add(new Rectangle(Gdx.graphics.getWidth() - xPos, bot, xPos, height));
            bot += height/2;
        } while (bot < Gdx.graphics.getHeight() + height);

    }

    private void spawSmallHazard() {
        Rectangle hazard = new Rectangle();
        hazard.setWidth(smallAstroidTexture.getWidth());
        hazard.setHeight(smallAstroidTexture.getHeight());
        hazard.setX(MathUtils.random(0, Gdx.graphics.getWidth() - hazard.getWidth()));
        hazard.setY(Gdx.graphics.getHeight());
        smallHazards.add(hazard);
        lastSmallHazardTime = TimeUtils.millis();
    }

    private void spawLargeHazard() {
        Rectangle hazard = new Rectangle();
        hazard.setWidth(largeAstroidTexture.getWidth());
        hazard.setHeight(largeAstroidTexture.getHeight());
        hazard.setX(MathUtils.random(0, Gdx.graphics.getWidth() - hazard.getWidth()));
        hazard.setY(Gdx.graphics.getHeight());
        largeHazards.add(hazard);
        lastLargeHazardTime = TimeUtils.millis();
    }
}
