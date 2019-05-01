package com.nicolas.hazardracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;


/**
 * Created by shark on 11/20/2017.
 */

class MainMenuScreen implements Screen {
    private BitmapFont font;
    private BitmapFont font2;

    final  HazardRacer game;
    private final ImageButton emptyBtn;

    //private OrthographicCamera camera;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;

    private TextButton startBtn;
    private TextButton optionsBtn;
    private TextButton scoreBtn;
    private TextButton exitBtn;
    private Table table;


    public MainMenuScreen(final HazardRacer game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        font.setColor(Color.FIREBRICK);
        font.getData().scale(5);
        font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().scale(2);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont() ;
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        startBtn = new TextButton("Start Game", textButtonStyle);
        emptyBtn = new ImageButton(new BaseDrawable());
        optionsBtn = new TextButton("Options", textButtonStyle);
        exitBtn = new TextButton("Exit", textButtonStyle);
        scoreBtn = new TextButton("High Scores", textButtonStyle);
        startBtn.getLabel().setFontScale(4f);
        optionsBtn.getLabel().setFontScale(4f);
        exitBtn.getLabel().setFontScale(4f);
        scoreBtn.getLabel().setFontScale(4f);
        startBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.goToGame();
                return true;
            }
        });

        optionsBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });

        scoreBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.setScreen(new HighscoreScreen(game));
                return true;
            }
        });


        exitBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.exit();
                return false;
            }
        });
        table.add(startBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(scoreBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(optionsBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();
        table.add(exitBtn).size(Value.percentWidth(50f),Value.percentHeight(10f));
        table.row();

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
        font2.draw(game.batch,"Name: Nicolas Hanout   |   email: nhanout", 50, Gdx.graphics.getHeight()-font2.getLineHeight());


        font.draw(game.batch,"Hazard Racer", Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/10);

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
