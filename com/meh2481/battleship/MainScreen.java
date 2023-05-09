/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.meh2481.battleship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_SCISSOR_TEST;

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
