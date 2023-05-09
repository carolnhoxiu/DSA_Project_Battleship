package com.battleship.Screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.battleship.MyBattleshipGame;

public class MainScreen extends MyBattleshipGame implements Screen {
    private MyBattleshipGame game;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton startButton;
    private TextButton quitButton;
    private SpriteBatch batch;
    private Sprite sprite;

    public MainScreen (MyBattleshipGame game){
        this.game=game;

    }

    @Override
    public void create() {


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void hide() {

    }
}
