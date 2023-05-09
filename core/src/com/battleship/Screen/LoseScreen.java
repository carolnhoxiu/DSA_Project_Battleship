package com.battleship.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.battleship.Manager.Game.GameManager;
import com.battleship.MyBattleshipGame;

public class LoseScreen extends MyBattleshipGame implements Screen {

    private Skin skin;
    private SpriteBatch batch;
    private Sprite sprite;
    private MyBattleshipGame app;
    private MainScreen mainScreen;

    public LoseScreen(){
        skin = new Skin(Gdx.files.internal("Font/uiskin.json"));
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("Sprite/game-over.png")));
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {
        skin.dispose();
        GameManager.getManager().getM_mPlacingMusic().dispose();
        batch.dispose();
        System.out.println("Heya");
    }
    @Override
    public void create() {

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
    @Override
    public void render() {

    }

    @Override
    public void resize(int i, int i1) {

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
}
