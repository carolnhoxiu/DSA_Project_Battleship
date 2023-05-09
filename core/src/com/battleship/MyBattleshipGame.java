package com.battleship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.battleship.Board.Board;
import com.battleship.Controller.Bot;
import com.battleship.Manager.AssetManager;
import com.battleship.Manager.Game.GameManager;
import com.battleship.Manager.Game.GameMode;
import com.battleship.Manager.InputHandler;
import com.battleship.Screen.LoseScreen;
import com.battleship.Screen.MainScreen;
import com.battleship.Screen.StartScreen;
import com.battleship.Screen.WinScreen;
import com.battleship.UI.TextPrompt;

import java.awt.*;

import static com.battleship.Manager.Game.GameManager.*;

/**
 *
 *
 * Handles game information for managing the player/enemy boards, drawing through LibGDX, restarting games,
 * processing input, etc.
 */
public class MyBattleshipGame extends Game implements Screen {
    //The game
    private static MyBattleshipGame game;
    private InputHandler inputHandler;
    private TextPrompt textPrompt;

    //The menu pane on the right side
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton sonarButton;
    private TextButton quitButton;
    private SpriteBatch batch;
    private Sprite sprite;
    private TextButton bombButton;
    private TextButton shieldButton;

    //Screens
    private StartScreen startScreen;
    private MainScreen mainScreen;
    private WinScreen winScreen;
    private LoseScreen loseScreen;

    //Variables for image/sound resources
    //Images
    private Texture m_txFireCursorSm;
    private Texture m_txFireCursorLg;


    //Variables to handle rendering
	private SpriteBatch m_bBatch;
    private ShapeRenderer m_rShapeRenderer;


    private OrthographicCamera m_cCamera;
    private BitmapFont m_ftTextFont;



    //Classes that hold game information
    public final static Vector2 playerBoardOffset = new Vector2(76, 76);


    //private EnemyAI m_aiEnemy;                  //Enemy player AI
    public static long m_iModeCountdown;  //Delay timer for counting down to next game state change
    public static long m_iEnemyGuessTimer;    //Pause timer for before enemy guess so the player can tell where they're guessing
    public static final double MODESWITCHTIME = 0.6;  //Time in seconds it takes the enemy AI to make an action and to switch the mode back to the player

    //Constants to deal with flashing cursor
    private final float CURSOR_MIN_ALPHA = 0.4f;   //Minimum cursor alpha (on a scale 0..1) when at its most transparent
    private final float CURSOR_MAX_ALPHA = 0.8f;    //Maximum cursor alpha (on a scale 0..1) when at its most opaque
    private final double CURSOR_FLASH_FREQ = 1.65;  //How many times per second the cursor flashes
    public static final double NANOSEC = 1000000000.0;    //Nanoseconds in a second (used for System.nanoTime() conversions)
    private final double MAX_CROSSHAIR_SCALE = 20.0;    //Multiplying factor for how large the enemy firing cursor starts out

    //Constants to deal with gameover screen
    public static int m_iCharWon;
    private final String GAMEOVER_STR = "Game Over";
    private final String ENEMY_WON_STR = "You Lose";
    private final String PLAYER_WON_STR = "You Win";
    private final int GAMEOVER_STR_PT = 5;  //Scale fac for gameover and large info text
    private final int GAMEOVER_SUBSTR_PT = 4;   //Scale fac for smaller "player/enemy won" text

    //For drawing messages about AI difficulty
    private final String AI_EASY_STR    = "Easy AI";
    private final String AI_HARD_STR    = "Difficult AI";
    private final String CONTROLS_INTRO_STR = "Press RMB to rotate ship, LMB to place it, D to change enemy AI difficulty";
    private final double AI_MSG_LEN     = 3.0;  //Default time in seconds to display these messages
    private final double CONTROLS_INTRO_LEN = 15.0; //Default time in seconds to display intro controls message
    private final int AI_MSG_OFFSET = 20;   //Offset in pixels from top of screen to display this message
    private long m_iAIMsgCountdown;  //time in nanoseconds left to display message
    private String m_sMsgTxt;
    com.badlogic.gdx.math.Rectangle scissors = new com.badlogic.gdx.math.Rectangle();
    com.badlogic.gdx.math.Rectangle clipBounds = new Rectangle(0,0,975,975);


    public MyBattleshipGame() {
        if (game == null){
            game = this;
        }
    }

    //Getter setter for application
    public static MyBattleshipGame getGame() {
        return game;
    }

    public StartScreen getStartScreen() {
        return startScreen;
    }


    public Stage getStage() {
        return stage;
    }

    /**
     * Create all the resources for our game. Called by libGDX automagically
     */
    @Override
	public void create() {
        AssetManager.createManager();
        //Tell GDX inputHandler will be handling input
        GameManager.createGameManager();
        inputHandler = new InputHandler();
        startScreen = new StartScreen(this, mainScreen);
        winScreen = new WinScreen();
        loseScreen = new LoseScreen();
        setScreen(startScreen);
        stage = startScreen.getStage();
        InputMultiplexer im = new InputMultiplexer(stage, inputHandler);
        Gdx.input.setInputProcessor(im);

		//Load the game resources
        m_ftTextFont = new BitmapFont(true);
        m_txFireCursorSm = new Texture("Sprite/crosshair.png");
        m_txFireCursorLg = new Texture("Sprite/crosshair_lg.png");

        //Set the camera origin 0,0 to be upper-left, not bottom-left like the gdx default (makes math easier)
        m_cCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        m_cCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        skin = new Skin(Gdx.files.internal("Font/uiskin.json"));
        table = new Table();
        sprite = new Sprite(new Texture(Gdx.files.internal("Sprite/pkmblack.png")));

        table.setWidth(stage.getWidth());
        table.align(Align.right | Align.center);

        table.setPosition(0,Gdx.graphics.getHeight()/2);

        sonarButton = new TextButton("Sonar",skin,"default");
        quitButton = new TextButton("Quit",skin,"default");
        bombButton = new TextButton("Bomb",skin,"default");
        shieldButton = new TextButton("Shield",skin,"default");
        shieldButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        sonarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getManager().placeSonar();
            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        bombButton.addListener(new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y) {

        }
    });

        table.padRight(120);
        table.add(sonarButton).padBottom(20);
        table.row();
        table.add(bombButton).padBottom(20);
        table.row();
        table.add(shieldButton).padBottom(20);
        table.row();
        table.add(quitButton);
        stage.addActor(table);
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        table.setVisible(false);

        //m_aiEnemy = new EnemyAI();
        m_bBatch = new SpriteBatch();
        m_rShapeRenderer = new ShapeRenderer();

        //Initialize game state
        GameManager.getManager().reset();
        currentMode = GameMode.PLACESHIP;
        m_iModeCountdown = 0;
        m_iEnemyGuessTimer = 0;

        textPrompt = new TextPrompt(m_bBatch, m_rShapeRenderer, m_ftTextFont, GAMEOVER_STR_PT);



        //Show game controls text
        m_iAIMsgCountdown = System.nanoTime() + (long)(CONTROLS_INTRO_LEN * NANOSEC);   //Show message longer than normal to give player time to read
        m_sMsgTxt = CONTROLS_INTRO_STR;

    }

    public void update() {
        GameManager.getManager().update();
    }

    /**
     * Handle drawing the game to the screen and updating game state (called by LibGDX every frame)
     */
	@Override
	public void render()
	{
        update();
        //Tell the camera to update its matrices.
        m_cCamera.update();

        //Set our batch drawing to use this updated camera matrix
        m_bBatch.setProjectionMatrix(m_cCamera.combined);
        m_rShapeRenderer.setProjectionMatrix(m_cCamera.combined);

        ScissorStack.calculateScissors(m_cCamera, m_bBatch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
        //---------------------------------
        // Begin drawing loop
        //---------------------------------
        m_bBatch.begin();

        if(currentMode == GameMode.PLAYERTURN)  //On the player's turn, draw enemy board, guessed positions, and cursor
        {
            table.setVisible(true);
           // m_bBatch.draw(sprite,Gdx.graphics.getWidth()/3, Gdx.graphics.getWidth()/2);
            //Draw enemy's board and player's guessed positions
            botBoard.draw(!Gdx.input.isKeyPressed(Input.Keys.S), m_bBatch);

            //Cheat code: holding S shows placement of enemy ships
            if(m_iModeCountdown == 0)   //Draw a crosshair on the tile where the mouse cursor currently is hovering
            {
                    //Set the cursor's alpha to sinusoidally increase/decrease for a nice pulsating effect
                    Color cCursorCol = new Color(1, 1, 1, CURSOR_MAX_ALPHA);    //Start at high alpha
                    double fSecondsElapsed = (double) System.nanoTime() / NANOSEC;   //Use current time for sinusoidal alpha multiply
                    cCursorCol.lerp(1, 1, 1, CURSOR_MIN_ALPHA, (float) Math.sin(fSecondsElapsed * Math.PI * CURSOR_FLASH_FREQ));   //Linearly interpolate this color to final value
                    m_bBatch.setColor(cCursorCol);
                    m_bBatch.draw(m_txFireCursorSm, playerBoardOffset.x + mouseCursorTile.x * Board.TILE_SIZE, playerBoardOffset.y + mouseCursorTile.y * Board.TILE_SIZE);
                    m_bBatch.setColor(Color.WHITE); //Reset color to default
            }
            else
                textPrompt.drawLgText();  //Draw text overlay for hit/miss
        }
        else if(currentMode == GameMode.BOTTURN)  //On enemy's turn, draw player's board and crosshair animation if applicable
        {
            //Draw player's board, showing player where they had placed their ships
            playerBoard.draw(false, m_bBatch);
            if(m_iEnemyGuessTimer > 0)  //Draw enemy homing in on their shot
            {
                //Scale this crosshair inwards as time elapses
                double fCrosshairScale = ((double)(m_iEnemyGuessTimer - System.nanoTime()) / NANOSEC) * MODESWITCHTIME * MAX_CROSSHAIR_SCALE + ((double)Board.TILE_SIZE / (double)m_txFireCursorLg.getHeight());
                double fDrawSize = fCrosshairScale * m_txFireCursorLg.getHeight();

                //Draw the crosshair centered on the position where the enemy will be guessing
                //m_bBatch.draw(m_txFireCursorLg, (float)(xCrosshairCenter - fDrawSize / 2.0), (float)(yCrosshairCenter - fDrawSize / 2.0), (float)fDrawSize, (float)fDrawSize);
            }
            if(Bot.attackMode == 1)
                textPrompt.drawLgText();
        }
        else if(currentMode == GameMode.PLACESHIP)  //If we're placing ships, just draw board as normal
        {
            playerBoard.draw(false, m_bBatch);
        }
        else if(currentMode == GameMode.GAMEOVER)
        {
            if(m_iCharWon == 0)
                botBoard.draw(false, m_bBatch);
            else
                playerBoard.draw(false, m_bBatch);

            TextPrompt.updateMessage(GAMEOVER_STR);
            textPrompt.drawLgText();

            //Draw player won/lost text green, slightly smaller, and lower down
            if(m_iCharWon == 0){
                m_ftTextFont.setColor(0.25f, 1.0f, 0.25f, 1.0f);    //Green if won
            //    m_bBatch.draw(new Texture(Gdx.files.internal("win.png")),0,0);
                setScreen(winScreen);
            }
            else {
                m_ftTextFont.setColor(1.0f, 0.1f, 0.1f, 1.0f);      //Red if lost
            //    m_bBatch.draw(new Texture(Gdx.files.internal("game-over.png")),0,0);
                setScreen(loseScreen);
        }
                m_ftTextFont.getData().setScale(GAMEOVER_SUBSTR_PT);
                m_ftTextFont.draw(m_bBatch, (m_iCharWon == 0) ? (PLAYER_WON_STR) : (ENEMY_WON_STR), 0, Gdx.graphics.getHeight() / 2, 900, Align.center, false);
        }


        //See if we should draw small message at the top of the screen
        if(System.nanoTime() < m_iAIMsgCountdown)
        {
            m_ftTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);  //set back to white
            m_ftTextFont.getData().setScale(1);
            m_ftTextFont.draw(m_bBatch, m_sMsgTxt, 0, AI_MSG_OFFSET, 900, Align.center, true);
        }


        //---------------------------------
        // End drawing loop
        //---------------------------------
        m_bBatch.end();
        m_bBatch.flush();
        ScissorStack.popScissors();
        m_bBatch.begin();
        m_bBatch.draw(new Texture("Sprite/menu-pane.png"),Gdx.graphics.getWidth()-225,0,300,975);
        if (currentMode==GameMode.GAMEOVER){
            if (m_iCharWon==0){
                setScreen(winScreen);
            }
            else setScreen(loseScreen);
        }
        m_bBatch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        super.render();
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

    /**
     * Called by LibGDX on app exit when it's a good time to clean up game resources
     */
	@Override
	public void dispose() {
		//Clean up resources
        GameManager.getManager().dispose();
        m_txFireCursorSm.dispose();
        m_txFireCursorLg.dispose();
        m_bBatch.dispose();
        m_rShapeRenderer.dispose();
        m_ftTextFont.dispose();
	}
}
