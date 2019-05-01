/*  First draft of the game.*/



package com.nicolas.hazardracer;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;

	OrthographicCamera camera;

	Texture playerTexture;

	Texture astroidTexture;


	Music enginLoop;
    Music music;


    Rectangle playerRectangle;
    ArrayList<Rectangle> hazards;

    long lastHazardTime;

    float gameSpeed;
    int turn;

    @Override
	public void create () {
		batch = new SpriteBatch();

        playerTexture = new Texture(Gdx.files.internal("red_racer.png"));
        astroidTexture = new Texture(Gdx.files.internal("badlogic.jpg"));

        enginLoop = Gdx.audio.newMusic(Gdx.files.internal("engine_loop.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        playerRectangle = new Rectangle();
        playerRectangle.width = playerTexture.getWidth();
        playerRectangle.height = playerTexture.getHeight();
        hazards = new ArrayList<Rectangle>();


        enginLoop.setLooping(true);
        enginLoop.setVolume(0.0f);
        enginLoop.play();


        music.setLooping(true);
        music.setVolume(0.0f);
        music.play();

        gameSpeed = 10;

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isTouched()) {
            /*Vector2 touchPos = new Vector2();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            //camera.unproject(touchPos);*/

            if(Gdx.input.getX() < playerRectangle.getX() ){
                turn = -1;
            }else if(Gdx.input.getX() > (playerRectangle.getX()+playerRectangle.getWidth())){
                turn = 1;
            }else{
                turn = 0;
            }
        }else {
            turn= 0;
        }

        playerRectangle.setX(playerRectangle.getX()+ (turn* gameSpeed));

        if(playerRectangle.getX()<0)
            playerRectangle.setX(0f);

        if(playerRectangle.getX()>Gdx.graphics.getWidth()- playerRectangle.getWidth())
            playerRectangle.setX(Gdx.graphics.getWidth()- playerRectangle.getWidth());

		batch.begin();
		batch.draw(playerTexture, playerRectangle.x, playerRectangle.y);

        for(Rectangle hazard: hazards) {
            batch.draw(astroidTexture, hazard.getX(), hazard.getY());
        }
		batch.end();



        if(TimeUtils.nanoTime() - lastHazardTime > 2000000000) spawHazard();
        Iterator<Rectangle> iter = hazards.iterator();
        while(iter.hasNext()) {
            Rectangle hazard = iter.next();
            hazard.setY(hazard.getY() - gameSpeed *2);// Gdx.graphics.getDeltaTime());
            if(hazard.getY() + hazard.getHeight() < 0) iter.remove();
            if(hazard.overlaps(playerRectangle)) {
                iter.remove();
            }
        }
	}

    private void spawHazard() {
        Rectangle hazard = new Rectangle();
        hazard.setWidth(astroidTexture.getWidth());
        hazard.setHeight(astroidTexture.getHeight());
        hazard.setX(MathUtils.random(0, Gdx.graphics.getWidth()-hazard.getWidth()));
        hazard.setY(Gdx.graphics.getHeight());
        hazards.add(hazard);
        lastHazardTime = TimeUtils.nanoTime();
    }
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		astroidTexture.dispose();
		music.dispose();
		enginLoop.dispose();
	}
}