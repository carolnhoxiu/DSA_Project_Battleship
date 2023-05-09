
/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */package com.battleship.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.battleship.Manager.Game.GameManager;
import com.battleship.MyBattleshipGame;

public class WinScreen extends MyBattleshipGame implements Screen {

    private Skin skin;
    private SpriteBatch batch;
    private Sprite sprite;
    private MyBattleshipGame app;
    private MainScreen mainScreen;

    public WinScreen(){
        skin = new Skin(Gdx.files.internal("Font/uiskin.json"));
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("Sprite/win.png")));
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
