package com.nicolas.hazardracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shark on 11/29/2017.
 */

class HighscoreScreen implements Screen {

    private final ImageButton emptyBtn;
    private boolean empty=false;
    private BitmapFont font;

    final  HazardRacer game;

    //private OrthographicCamera camera;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    private TextButton line;
    private TextButton backBtn;
    private Table table;

    public HighscoreScreen(final HazardRacer game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().scale(5);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont() ;
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        line = new TextButton("No High Scores Yet, Try Later", textButtonStyle);
        backBtn = new TextButton("Back", textButtonStyle);
        emptyBtn = new ImageButton(new BaseDrawable());
        line.getLabel().setFontScale(4f);
        backBtn.getLabel().setFontScale(2f);
        ArrayList<HighScore> hs = this.game.highScores;
        if(hs == null || hs.size() == 0){
            this.empty = true;
            table.add(line).size(Value.percentWidth(50f), Value.percentHeight(10f));
            table.row();
        }else {
            Collections.reverse(hs);
            for (int i=1; i<hs.size()&& i<=10; i++) {
                line = new TextButton(i+"- "+hs.get(i-1).toString(), textButtonStyle);
                line.getLabel().setFontScale(4f);
                table.add(line).size(Value.percentWidth(50f), Value.percentHeight(8f));
                table.row();
                table.add(emptyBtn).size(Value.percentWidth(50f), Value.percentHeight(8f));
                table.row();
            }
        }
        table.add(backBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        this.game.getHighScores();
        backBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.goToMainMenu();
                return false;
            }
        });

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        game.batch.begin();

        font.draw(game.batch,"High Scores", Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/20);

        game.batch.end();

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
        stage.dispose();
    }
}
