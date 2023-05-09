/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.battleship.Screen;

import com.badlogic.gdx.*;
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

public class StartScreen extends MyBattleshipGame implements Screen, InputProcessor {

    private Stage stage;
    private Skin skin;
    private Table table;
    private ImageButton startButton;
    private ImageButton menuButton;
    private SpriteBatch batch;
    private Sprite sprite;
    private MyBattleshipGame app;
    private MainScreen mainScreen;
    private Music startMusic;
    private Viewport vpSt;


    @Override
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public StartScreen(final MyBattleshipGame app, final MainScreen mainScreen){
        this.mainScreen=mainScreen;
        this.app=app;
        skin = new Skin(Gdx.files.internal("Font/uiskin.json"));
        stage = new Stage();
        table = new Table();
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("Sprite/mainscreen - no button.png")));
        startMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/titlemusic.mp3"));
        startMusic.setLooping(true);
        startMusic.play();
        table.setWidth(stage.getWidth());
        table.align(Align.right | Align.center);

        table.setPosition(0,Gdx.graphics.getHeight()-150);

        Drawable playDraw = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Sprite/play button.png"))));
        startButton = new ImageButton(playDraw);

        Drawable menuDraw = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Sprite/menu button.png"))));
        menuButton = new ImageButton(menuDraw);

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(mainScreen);
                app.getStartScreen().dispose();
                GameManager.getManager().getM_mPlacingMusic().setLooping(true);
                GameManager.getManager().getM_mPlacingMusic().play();
            }
        });
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.padRight(50);
        table.add(startButton).padTop(200);
        table.row();
        table.add(menuButton);
        stage.addActor(table);

        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {
        skin.dispose();
        GameManager.getManager().getM_mPlacingMusic().dispose();
        startButton.remove();
        menuButton.remove();
        batch.dispose();
        startMusic.dispose();
        System.out.println("Heya");
    }
    @Override
    public void create() {

    }


    @Override
    public void show() {
        InputMultiplexer im = new InputMultiplexer(stage,this);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

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


    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


}
