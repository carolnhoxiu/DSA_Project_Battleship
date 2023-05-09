package com.meh2481.battleship;

/* Tran Thanh Tung ITITIU20347
 * Tran Thi Ngoc Tu ITITIU20338
 * Vo Dang Trinh ITITIU20326
 * Lam Nguyen Phuong Uyen ITITIU20348
 */
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;

import java.awt.*;

public class MyBattleshipGame extends Game implements Screen, InputProcessor
{
    //The menu pane on the right side
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton sonarButton;
    private TextButton quitButton;
    private SpriteBatch batch;
    private Sprite sprite;
    private TextButton minesButton;

    //Screens
    private StartScreen startScreen;
    private  MainScreen mainScreen;

    //Game Application
    private MyBattleshipGame app;
    //Variables for image/sound resources
    //Images
	private Texture m_txShipCenterImage;
    private Texture m_txMissImage;
	private Texture m_txShipEdgeImage;
    private Texture m_txBoardBg;
    private Texture m_txFireCursorSm;
    private Texture m_txFireCursorLg;
    //Sounds
	private Sound m_sMissSound;
    private Sound m_sHitSound;
    private Sound m_sSunkSound;
    private Sound m_sWinSound;
    private Sound m_sLoseSound;


    //Music
	private Music m_mPlacingMusic;
    private Music m_mPlayingMusic;

    //Variables to handle rendering
	private SpriteBatch m_bBatch;
    private ShapeRenderer m_rShapeRenderer;


    private OrthographicCamera m_cCamera;
    private BitmapFont m_ftTextFont;
    private String m_sOverlayTxt;



    //Classes that hold game information
	private Player m_bPlayer;        //Board the player places ships on and the enemy guesses onto
    private Board playerBoard;
    public final static Point playerBoardOffset = new Point(75, 75);
    private Bot m_bBot;                //Board the enemy places ships on and the player guesses onto
    private Board botBoard;



    //private EnemyAI m_aiEnemy;                  //Enemy player AI
    private Point m_ptCurMouseTile;   //Current tile the mouse is hovering over

    //Variables & constants to handle current game state
    private int m_iGameMode;    //State machine value for current game mode
    private final int MODE_PLACESHIP = 0;
    private final int MODE_PLAYERTURN = 1;
    private final int MODE_ENEMYTURN = 2;
    private final int MODE_GAMEOVER = 3;
    private long m_iModeCountdown;  //Delay timer for counting down to next game state change
    private long m_iEnemyGuessTimer;    //Pause timer for before enemy guess so the player can tell where they're guessing
    private final double MODESWITCHTIME = 0.6;  //Time in seconds it takes the enemy AI to make an action and to switch the mode back to the player
    private final double PLAYERHITPAUSE = 1.25; //Pause in seconds after player launches a missile, to let them read the result

    //Constants to deal with flashing cursor
    private final float CURSOR_MIN_ALPHA = 0.4f;   //Minimum cursor alpha (on a scale 0..1) when at its most transparent
    private final float CURSOR_MAX_ALPHA = 0.8f;    //Maximum cursor alpha (on a scale 0..1) when at its most opaque
    private final double CURSOR_FLASH_FREQ = 1.65;  //How many times per second the cursor flashes
    private final double NANOSEC = 1000000000.0;    //Nanoseconds in a second (used for System.nanoTime() conversions)
    private final double MAX_CROSSHAIR_SCALE = 20.0;    //Multiplying factor for how large the enemy firing cursor starts out

    //Constants to deal with gameover screen
    private int m_iCharWon;
    private final int PLAYER_WON = 0;
    private final int ENEMY_WON = 1;
    private final String GAMEOVER_STR = "Game Over";
    private final String ENEMY_WON_STR = "You Lose";
    private final String PLAYER_WON_STR = "You Win";
    private final String MISS_STR = "Miss";
    private final String HIT_STR = "Hit ";
    private final String SUNK_STR = "Sunk ";
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
        app = this;
    }

    public void setM_mPlacingMusic(Music m_mPlacingMusic) {
        this.m_mPlacingMusic = m_mPlacingMusic;
    }

    public Music getM_mPlacingMusic() {
        return m_mPlacingMusic;
    }

    public OrthographicCamera getM_cCamera() {
        return m_cCamera;
    }

    public void setM_cCamera(OrthographicCamera m_cCamera) {
        this.m_cCamera = m_cCamera;
    }


    public Stage getStage() {
        return stage;
    }

    /**
     * Create all the resources for our game. Called by libGDX automagically
     */
    @Override
	public void create() {
        //Tell GDX this class will be handling input
        InputMultiplexer im = new InputMultiplexer(stage,this);
        Gdx.input.setInputProcessor(im);
        startScreen = new StartScreen(this,mainScreen);
        setScreen(startScreen);

		//Load the game resources
        m_ftTextFont = new BitmapFont(true);
		m_txShipCenterImage = new Texture("hit.png");
		m_txShipEdgeImage = new Texture("ship_edge.png");
        m_txMissImage = new Texture("miss.png");
        m_txBoardBg = new Texture("map.png");
        m_txFireCursorSm = new Texture("crosshair.png");
        m_txFireCursorLg = new Texture("crosshair_lg.png");
        m_sMissSound = Gdx.audio.newSound(Gdx.files.internal("miss.ogg"));
        m_sHitSound = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
        m_sLoseSound = Gdx.audio.newSound(Gdx.files.internal("youLose.mp3"));
        m_sWinSound = Gdx.audio.newSound(Gdx.files.internal("youWin.mp3"));
        m_sSunkSound = Gdx.audio.newSound(Gdx.files.internal("sunk.ogg"));
        m_mPlacingMusic = Gdx.audio.newMusic(Gdx.files.internal("beginningMusic.mp3"));
        m_mPlayingMusic = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"));

        //Set the camera origin 0,0 to be upper-left, not bottom-left like the gdx default (makes math easier)
        m_cCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        m_cCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = startScreen.getStage();
        table = new Table();
        sprite = new Sprite(new Texture(Gdx.files.internal("pkmblack.png")));

        table.setWidth(stage.getWidth());
        table.align(Align.right | Align.center);

        table.setPosition(0,Gdx.graphics.getHeight()/2);

        sonarButton = new TextButton("Sonar",skin,"default");
        quitButton = new TextButton("Quit",skin,"default");
        minesButton = new TextButton("Mines",skin,"default");
        sonarButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        minesButton.addListener(new ClickListener(){
        @Override
        public void clicked(InputEvent event, float x, float y) {

        }
    });

        table.padRight(120);
        table.add(sonarButton).padBottom(20);
        table.row();
        table.add(minesButton).padBottom(20);
        table.row();
        table.add(quitButton);
        stage.addActor(table);
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        table.setVisible(false);


        //Create game logic classes
        m_bPlayer = new Player(m_txBoardBg, m_txMissImage, m_txShipCenterImage, m_txShipEdgeImage);
        playerBoard = m_bPlayer.getBoard();

        m_bBot = new Bot(m_txBoardBg, m_txMissImage, m_txShipCenterImage, m_txShipEdgeImage);
        botBoard = m_bBot.getBoard();

        //m_aiEnemy = new EnemyAI();
        m_bBatch = new SpriteBatch();
        m_rShapeRenderer = new ShapeRenderer();

        //Initialize game state
        m_bPlayer.startPlacingShips();
        m_bBot.placeShipRandom();
        m_iGameMode = MODE_PLACESHIP;
        m_iModeCountdown = 0;
        m_iEnemyGuessTimer = 0;
        m_ptCurMouseTile = new Point(-1,-1);

        //Start music
        m_mPlayingMusic.setLooping(true);
		m_mPlacingMusic.setLooping(true);

        //Show game controls text
        m_iAIMsgCountdown = System.nanoTime() + (long)(CONTROLS_INTRO_LEN * NANOSEC);   //Show message longer than normal to give player time to read
        m_sMsgTxt = CONTROLS_INTRO_STR;

    }



    //Getter setter for application
    public MyBattleshipGame getApp() {
        return app;
    }

    public void setApp(MyBattleshipGame app) {
        this.app = app;
    }
    public StartScreen getStartScreen() {
        return startScreen;
    }

    /** Draw a large text message in upper center of screen
     *
     * @param       sMsg     Message to write to screen
     */
    public void drawLgText(String sMsg)
    {
        if(sMsg.isEmpty())
            return;

        final int iTextOffset = 15;  //Used to center textbox around gameover text

        //Draw black box behind text so it shows up better
        m_bBatch.end(); //In order to do this, we need to stop drawing the spritebatch so we can draw shapes
        Gdx.gl.glEnable(GL20.GL_BLEND); //Enable OpenGL blending so the box properly shows up low-alpha
        m_rShapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //Start drawing shapes with a filled background

        m_rShapeRenderer.setColor(0, 0, 0, 0.65f);  //Set the color to a lower-alpha black
        m_ftTextFont.getData().setScale(GAMEOVER_STR_PT);
        m_rShapeRenderer.rect(0, Gdx.graphics.getHeight() / 4 - iTextOffset, 900, m_ftTextFont.getLineHeight());    //Draw behind where the text will be

        m_rShapeRenderer.end(); //Done rendering shapes
        Gdx.gl.glDisable(GL20.GL_BLEND);    //Disable OpenGL blending to get back to previous OpenGL state
        m_bBatch.begin();   //Begin drawing the SpriteBatch again

        //Draw gameover text larger and higher up
        m_ftTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        m_ftTextFont.getData().setScale(GAMEOVER_STR_PT);
        m_ftTextFont.draw(m_bBatch, sMsg, 0, Gdx.graphics.getHeight() / 4, 900, Align.center, false);
    }

    /**
     * Handle drawing the game to the screen and updating game state (called by LibGDX every frame)
     */
	@Override
	public void render()
	{
        //Tell the camera to update its matrices.
        m_cCamera.update();

        //Set our batch drawing to use this updated camera matrix
        m_bBatch.setProjectionMatrix(m_cCamera.combined);
        m_rShapeRenderer.setProjectionMatrix(m_cCamera.combined);

        //Update our state machine
        if(m_iGameMode == MODE_PLAYERTURN && m_iModeCountdown > 0)  //Player turn waiting for enemy turn
        {
            if(System.nanoTime() >= m_iModeCountdown)   //Timer expired; set to enemy turn
            {
                m_iModeCountdown = 0;   //Reset timer
                if(botBoard.boardCleared())    //Game over; player won
                {
                    m_iGameMode = MODE_GAMEOVER;
                    m_iCharWon = PLAYER_WON;
                    //Play winning music
                    m_mPlayingMusic.stop();
                    m_sWinSound.play();
                }
                else
                {
                    m_iGameMode = MODE_ENEMYTURN;   //Change mode
                    m_iEnemyGuessTimer = (long) (System.nanoTime() + MODESWITCHTIME * NANOSEC);  //Set timer for the pause before an enemy guess
                }
            }
        }
        else if(m_iGameMode == MODE_ENEMYTURN)  //Enemy turn
        {
            if(m_iEnemyGuessTimer > 0)  //If we're waiting for the enemy firing animation
            {
                if(System.nanoTime() >= m_iEnemyGuessTimer) //If we've waited long enough
                {
                    //EnemyAI.Guess gHit = m_aiEnemy.guess(m_bPlayerBoard); //Make enemy AI fire at their guessed position

                    //Play the appropriate sound
                    //if(gHit == EnemyAI.Guess.HIT)
                    m_sHitSound.play();
                    //else if(gHit == EnemyAI.Guess.MISS)
                    m_sMissSound.play();
                    /*else*/ if(!playerBoard.boardCleared())
                    m_sSunkSound.play();

                    m_iModeCountdown = (long)(System.nanoTime() + MODESWITCHTIME * NANOSEC);    //Start countdown for switching to player's turn
                    m_iEnemyGuessTimer = 0; //Stop counting down
                }
            }
            else if(m_iModeCountdown > 0)   //If we're waiting until countdown is done for player's turn
            {
                if(System.nanoTime() >= m_iModeCountdown)  //Countdown is done
                {
                    m_iModeCountdown = 0;   //Reset timer
                    if(playerBoard.boardCleared())   //Game over; enemy won
                    {
                        m_iGameMode = MODE_GAMEOVER;
                        m_iCharWon = ENEMY_WON;
                        //Play losing sound
                        m_mPlayingMusic.stop();
                        m_sLoseSound.play();
                    }
                    else
                    {
                        m_iGameMode = MODE_PLAYERTURN;  //Switch to player's turn
                    }
                }
            }
        }
        ScissorStack.calculateScissors(m_cCamera, m_bBatch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
        //---------------------------------
        // Begin drawing loop
        //---------------------------------
        m_bBatch.begin();

        if(m_iGameMode == MODE_PLAYERTURN)  //On the player's turn, draw enemy board, guessed positions, and cursor
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
                    m_bBatch.draw(m_txFireCursorSm, playerBoardOffset.x + m_ptCurMouseTile.x * Board.TILE_SIZE, playerBoardOffset.y + m_ptCurMouseTile.y * Board.TILE_SIZE);
                    m_bBatch.setColor(Color.WHITE); //Reset color to default
            }
            else
                drawLgText(m_sOverlayTxt);  //Draw text overlay for hit/miss
        }
        else if(m_iGameMode == MODE_ENEMYTURN)  //On enemy's turn, draw player's board and crosshair animation if applicable
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
        }
        else if(m_iGameMode == MODE_PLACESHIP)  //If we're placing ships, just draw board as normal
        {
            playerBoard.draw(false, m_bBatch);
        }
        else if(m_iGameMode == MODE_GAMEOVER)
        {
            if(m_iCharWon == PLAYER_WON)
                botBoard.draw(false, m_bBatch);
            else
                playerBoard.draw(false, m_bBatch);

            drawLgText(GAMEOVER_STR);

            //Draw player won/lost text green, slightly smaller, and lower down
            if(m_iCharWon == PLAYER_WON)
                m_ftTextFont.setColor(0.25f, 1.0f, 0.25f, 1.0f);    //Green if won
            else
                m_ftTextFont.setColor(1.0f, 0.1f, 0.1f, 1.0f);      //Red if lost
            m_ftTextFont.getData().setScale(GAMEOVER_SUBSTR_PT);
            m_ftTextFont.draw(m_bBatch, (m_iCharWon == PLAYER_WON)?(PLAYER_WON_STR):(ENEMY_WON_STR), 0, Gdx.graphics.getHeight() / 2, 900, Align.center, false);
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
        m_bBatch.draw(new Texture(Gdx.files.internal("menu-pane.png")),Gdx.graphics.getWidth()-225,0,300,975);
        m_bBatch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        super.render();
    }


    /**
     * Called by LibGDX when a key is pressed
     * @param keycode   key that was pressed
     * @return  whether the input was processed
     */
	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode == Input.Keys.ESCAPE)    //Exit game on Escape
            Gdx.app.exit();
        else if(keycode == Input.Keys.F5)   //Start new game on F5
        {
            //Reset boards and game state
            m_iGameMode = MODE_PLACESHIP;
            m_iModeCountdown = 0;
            m_iEnemyGuessTimer = 0;
            playerBoard.reset();
            botBoard.reset();
            //m_aiEnemy.reset();
            m_bPlayer.startPlacingShips();
            m_bBot.placeShipRandom();

            //Start playing music
            m_mPlayingMusic.stop();
            m_mPlacingMusic.stop();
            m_mPlacingMusic.play();
        }
        else if(keycode == Input.Keys.D)    //Change difficulty on D
        {
            //Show message at top of screen alterting player of this change
            m_iAIMsgCountdown = System.nanoTime() + (long)(AI_MSG_LEN * NANOSEC);
            //m_aiEnemy.setHardMode(!m_aiEnemy.isHardMode()); //Switch difficulty of AI
            //Set to correct message
            //if(m_aiEnemy.isHardMode())
                m_sMsgTxt = AI_HARD_STR;
            //else
                m_sMsgTxt = AI_EASY_STR;
        }

		return false;
	}

    /**
     * Called by LibGDX when a mouse click even occurs
     * @param screenX   x position of mouse cursor on screen
     * @param screenY   y position of mouse cursor on screen
     * @param pointer   the pointer for the event
     * @param button    mouse button that was pressed
     * @return          true if input was processed
     */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT)    //Clicking left mouse button
        {
            if (!quitButton.isPressed()){
            if (m_iGameMode == MODE_PLACESHIP)   //Placing ships; lock this ship's position and go to next ship
            {
                if (m_bPlayer.placeShip(m_ptCurMouseTile)) {
                    m_iGameMode = MODE_PLAYERTURN;//Done placing ships; start playing now. Player always goes first
                    m_mPlacingMusic.stop();
                    m_mPlayingMusic.play();
                }
            } else if (m_iGameMode == MODE_PLAYERTURN && m_iModeCountdown == 0)   //Playing; fire at a ship
            {
                if (!botBoard.alreadyFired(m_ptCurMouseTile))    //If we haven't fired here already
                {
                        Ship sHit = m_bBot.fireAtPos(m_ptCurMouseTile);    //Fire!
                        if (sHit != null)    //If we hit a ship
                        {
                            if (sHit.isSunk())   //Sunk a ship
                            {
                                if (!botBoard.boardCleared())
                                    m_sSunkSound.play();
                                m_sOverlayTxt = SUNK_STR + sHit.getType().name;
                            } else    //Hit a ship
                            {
                                m_sHitSound.play();
                                m_sOverlayTxt = HIT_STR + sHit.getType().name;
                            }
                        } else    //Missed a ship
                        {
                            m_sMissSound.play();
                            m_sOverlayTxt = MISS_STR;
                        }
                        m_iModeCountdown = (long) (System.nanoTime() + PLAYERHITPAUSE * NANOSEC);    //Start countdown timer for the start of the enemy turn
                }
            } else if (m_iGameMode == MODE_GAMEOVER) //Game over; start a new game
            {
                //Reset boards and game state
                m_iGameMode = MODE_PLACESHIP;
                playerBoard.reset();
                botBoard.reset();
                //m_aiEnemy.reset();
                m_bPlayer.startPlacingShips();
                m_bBot.placeShipRandom();

                //Start playing music
                m_mPlayingMusic.stop();
                m_mPlacingMusic.play();
            }
        }
    }
            else if (button == Input.Buttons.RIGHT)  //Clicking right mouse button
            {
                if (m_iGameMode == MODE_PLACESHIP)   //Rotate ships on RMB if we're currently placing them
                    m_bPlayer.rotateShip();
            }
		return false;
	}

    /**
     * Called by LibGDX when the mouse cursor moves
     * @param screenX   new mouse cursor x position
     * @param screenY   new mouse cursor y position
     * @return          true if input processed
     */
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
        //Find the tile the player moved the mouse to
        m_ptCurMouseTile.x = screenX < playerBoardOffset.x ? 0 : (screenX - playerBoardOffset.x) / Board.TILE_SIZE;
        m_ptCurMouseTile.y = screenY < playerBoardOffset.y ? 0 : (screenY - playerBoardOffset.y) / Board.TILE_SIZE;

        if (m_iGameMode == MODE_PLACESHIP)   //If the player is currently placing ships, move ship preview to this location
            m_bPlayer.previewShip(m_ptCurMouseTile.x, m_ptCurMouseTile.y);
        return false;

	}

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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
	public void dispose()
	{
		//Clean up resources
		m_txShipCenterImage.dispose();
		m_txShipEdgeImage.dispose();
        m_txMissImage.dispose();
        m_txBoardBg.dispose();
        m_txFireCursorSm.dispose();
        m_txFireCursorLg.dispose();
		m_sMissSound.dispose();
        m_sHitSound.dispose();
        m_sSunkSound.dispose();
        m_sWinSound.dispose();
        m_sLoseSound.dispose();
		m_mPlacingMusic.dispose();
        m_mPlayingMusic.dispose();
        m_bBatch.dispose();
        m_rShapeRenderer.dispose();
        m_ftTextFont.dispose();
	}

    //Methods we have to override for LibGDX purposes that we don't care about
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }


    public boolean scrolled(int amount)	{ return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }
}
