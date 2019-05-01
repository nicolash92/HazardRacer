package com.nicolas.hazardracer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

/**
 * Created by shark on 11/29/2017.
 */

class OptionsScreen implements Screen {
    private final ImageButton emptyBtn;
    private BitmapFont font;
    private BitmapFont font2;

    final  HazardRacer game;

    private Stage stage;
    private Table table;
    private TextButton.TextButtonStyle textButtonStyle;

    private TextButton musicOpBtn;
    private TextButton sfxOpBtn;
    private TextButton tiltBtn;
    private TextButton hardbtn;
    private TextButton backBtn;
    public OptionsScreen(final HazardRacer game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont() ;
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        emptyBtn = new ImageButton(new BaseDrawable());
        musicOpBtn = new TextButton("MUSIC   "+getTextVal(game.music), textButtonStyle);
        sfxOpBtn = new TextButton("SFX     "+getTextVal(game.sfx), textButtonStyle);
        backBtn = new TextButton("Back", textButtonStyle);
        tiltBtn = new TextButton("TILT CONTROLS    "+getTextVal(game.tiltControl), textButtonStyle);;
        hardbtn = new TextButton("HARD MODE    "+getTextVal(game.hardMode), textButtonStyle);
        musicOpBtn.getLabel().setFontScale(4f);
        sfxOpBtn.getLabel().setFontScale(4f);
        backBtn.getLabel().setFontScale(4f);
        tiltBtn.getLabel().setFontScale(4f);
        hardbtn.getLabel().setFontScale(4f);

        musicOpBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.music = !game.music;
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });

        sfxOpBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.sfx = !game.sfx;
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });

        tiltBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.tiltControl = !game.tiltControl;
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });

        hardbtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.hardMode = !game.hardMode;
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });


        backBtn.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(game.sfx)game.playClick();
                game.goToMainMenu();
                return false;
            }
        });

        table.add(musicOpBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(tiltBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(sfxOpBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(hardbtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(emptyBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
        table.add(backBtn).size(Value.percentWidth(75f),Value.percentHeight(10f));
        table.row();
    }

    private String getTextVal(boolean b) {
        return b?"ON":"OFF";
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
