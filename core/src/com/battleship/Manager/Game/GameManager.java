package com.battleship.Manager.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.battleship.*;
import com.battleship.Board.Board;
import com.battleship.Board.BoardPlayer;
import com.battleship.Board.Ship.ShipType;
import com.battleship.Board.Ship.ShotState;
import com.battleship.Controller.BoardController;
import com.battleship.Controller.Bot;
import com.battleship.Controller.Player;
import com.battleship.Interface.Observer;
import com.battleship.UI.TextPrompt;

import java.awt.*;

import static com.battleship.MyBattleshipGame.*;

public class GameManager implements Observer {
    private static GameManager manager;
    public static Vector2 mouseCursorTile;
    public static GameMode currentMode;
    private Player player;
    private Bot bot;
    private final int PLAYER_WON = 0;
    private final int ENEMY_WON = 1;
    public static BoardPlayer playerBoard;
    public static Board botBoard;

    private final double PLAYERHITPAUSE = 1.25; //Pause in seconds after player launches a missile, to let them read the result

    private Texture m_txShipCenterImage;
    private Texture m_txMissImage;
    private Texture m_txShipEdgeImage;
    private Texture m_txBoardBg;

    private final String MISS_STR = "Miss";
    private final String HIT_STR = "Hit";
    private final String SUNK_STR = "Sunk ";

    //Sounds
    private Sound m_sHitSound;
    private Sound m_sMissSound;
    private Sound m_sSunkSound;
    private Sound m_sWinSound;
    private Sound m_sLoseSound;

    //Music
    private Music m_mPlacingMusic;
    private Music m_mPlayingMusic;

    private Queue<ShipType> sunkShip;

    public static void createGameManager() {
        if(manager == null) {
            manager = new GameManager();
            System.out.println("Game manager created.");
        } else
            System.out.println("Game manager has been created.");
    }

    private GameManager(){
        m_txShipCenterImage = new Texture("Sprite/hit.png");
        m_txShipEdgeImage = new Texture("Sprite/ship_edge.png");
        m_txMissImage = new Texture("Sprite/miss.png");
        m_txBoardBg = new Texture("Sprite/map.png");
        //Create game logic classes
        player = new Player(m_txBoardBg, m_txMissImage, m_txShipCenterImage, m_txShipEdgeImage);
        playerBoard = (BoardPlayer) player.getBoard();

        bot = new Bot(m_txBoardBg, m_txMissImage, m_txShipCenterImage, m_txShipEdgeImage);
        botBoard = bot.getBoard();

        sunkShip = new Queue<>();

        m_sHitSound = Gdx.audio.newSound(Gdx.files.internal("Sound/hit.ogg"));
        m_sSunkSound = Gdx.audio.newSound(Gdx.files.internal("Sound/sunk.ogg"));
        m_sMissSound = Gdx.audio.newSound(Gdx.files.internal("Sound/miss.ogg"));
        m_sWinSound = Gdx.audio.newSound(Gdx.files.internal("Sound/youWin.mp3"));
        m_sLoseSound = Gdx.audio.newSound(Gdx.files.internal("Sound/youLose.mp3"));

        m_mPlacingMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/beginningMusic.mp3"));
        m_mPlayingMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/mainTheme.mp3"));

        //Start music
        m_mPlayingMusic.setLooping(true);
        m_mPlacingMusic.setLooping(true);
    }

    public static GameManager getManager() {
        return manager;
    }

    public Music getM_mPlacingMusic(){
        return m_mPlacingMusic;
    }

    public void setMouseCursor(Vector2 mouseCursorTile) {
        this.mouseCursorTile = mouseCursorTile;
    }

    public void update(){
        mouseHover();

        //Update our state machine
        if(currentMode == GameMode.PLAYERTURN && m_iModeCountdown > 0)  //Player turn waiting for enemy turn
        {
            if(System.nanoTime() >= m_iModeCountdown)   //Timer expired; set to enemy turn
            {
                m_iModeCountdown = 0;   //Reset timer
                if(botBoard.boardCleared())    //Game over; player won
                {
                    currentMode = GameMode.GAMEOVER;
                    m_iCharWon = PLAYER_WON;
                    //Play winning music
                    m_mPlayingMusic.stop();
                    m_sWinSound.play();
                }
                else
                {
                    currentMode = GameMode.BOTTURN;   //Change mode
                    m_iEnemyGuessTimer = (long) (System.nanoTime() + MODESWITCHTIME * NANOSEC);  //Set timer for the pause before an enemy guess
                }
            }
        }
        else if(currentMode == GameMode.BOTTURN)  //Enemy turn
            botTurn();
    }

    public void reset() {
        //Reset boards and game state
        currentMode = GameMode.PLACESHIP;
        m_iModeCountdown = 0;
        m_iEnemyGuessTimer = 0;
        player.reset();
        bot.reset();
        //m_aiEnemy.reset();
        player.startPlacingShips();
        bot.placeShipRandom();

        //Start playing music
//        m_mPlayingMusic.stop();
//        m_mPlacingMusic.stop();
//        m_mPlacingMusic.play();
    }

    public void changeDifficulty() {
//        //Show message at top of screen alterting player of this change
//        m_iAIMsgCountdown = System.nanoTime() + (long)(AI_MSG_LEN * NANOSEC);
//        //m_aiEnemy.setHardMode(!m_aiEnemy.isHardMode()); //Switch difficulty of AI
//        //Set to correct message
//        //if(m_aiEnemy.isHardMode())
//        m_sMsgTxt = AI_HARD_STR;
//        //else
//        m_sMsgTxt = AI_EASY_STR;
    }

    public void leftClick(){
        if (currentMode == GameMode.PLACESHIP)   //Placing ships; lock this ship's position and go to next ship
            playerPlacing();
        else if (currentMode == GameMode.PLAYERTURN && m_iModeCountdown == 0){   //Playing; fire at a ship
            playerTurn();
        } else if (currentMode == GameMode.GAMEOVER) //Game over; start a new game
        {
            //Reset boards and game state
            currentMode = GameMode.PLACESHIP;
            player.reset();
            bot.reset();
            //m_aiEnemy.reset();
            player.startPlacingShips();
            bot.placeShipRandom();

            //Start playing music
//            m_mPlayingMusic.stop();
//            m_mPlacingMusic.play();
        }
    }

    public void rightClick() {
        if (currentMode == GameMode.PLACESHIP)   //Rotate ships on RMB if we're currently placing them
            player.rotateShip();
    }

    private void mouseHover(){
        if (currentMode == GameMode.PLACESHIP)   //If the player is currently placing ships, move ship preview to this location
            player.previewShip(mouseCursorTile);
    }

    private void playerPlacing() {
        if (player.placeShip(mouseCursorTile)) {
            currentMode = GameMode.PLAYERTURN;//Done placing ships; start playing now. Player always goes first
                m_mPlacingMusic.stop();
                m_mPlayingMusic.play();
        }
    }

    private void playerTurn() {
        Bot.attackMode = 0;
        if (!botBoard.alreadyFired(mouseCursorTile)) {   //If we haven't fired here already
            ShotState shipState = player.fireAtOpponent(bot, mouseCursorTile);    //Fire!

            updatePrompt(shipState, bot);

            m_iModeCountdown = (long) (System.nanoTime() + PLAYERHITPAUSE * MyBattleshipGame.NANOSEC);    //Start countdown timer for the start of the enemy turn
        }
    }

    private void botTurn() {
        if(m_iEnemyGuessTimer > 0)  //If we're waiting for the enemy firing animation
        {
            if(System.nanoTime() >= m_iEnemyGuessTimer) //If we've waited long enough
            {
                updatePrompt(bot.attack(player), player);

                m_iModeCountdown = (long)(System.nanoTime() + MODESWITCHTIME * MyBattleshipGame.NANOSEC);    //Start countdown for switching to player's turn
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
                    currentMode = GameMode.GAMEOVER;
                    m_iCharWon = ENEMY_WON;
                    //Play losing sound
                    m_mPlayingMusic.stop();
                    m_sLoseSound.play();
                }
                else
                {
                    currentMode = GameMode.PLAYERTURN;  //Switch to player's turn
                }
            }
        }
    }

    public void updatePrompt(ShotState result, BoardController opponent) {
        if (result != ShotState.MISS) {   //If we hit a ship
            if (sunkShip.size != 0) {  //Sunk a ship
                System.out.println("Sunk a ship");
                if (!opponent.getBoard().boardCleared())
                    m_sSunkSound.play();

                for (ShipType type : sunkShip) {
                    TextPrompt.updateMessage(SUNK_STR + type.getName());
                    sunkShip.removeFirst();
                }
            } else {    //Hit a ship
                m_sHitSound.play();
                TextPrompt.updateMessage(HIT_STR);
            }
        } else {   //Missed a ship
            m_sMissSound.play();
            TextPrompt.updateMessage(MISS_STR);
        }
    }

    @Override
    public void updateSunkShip(ShipType shipType){
        sunkShip.addLast(shipType);
    }

    public void placeSonar(){

    }

    public void dispose(){
        m_sMissSound.dispose();
        m_sHitSound.dispose();
        m_sSunkSound.dispose();
        m_sWinSound.dispose();
        m_sLoseSound.dispose();
        m_mPlacingMusic.dispose();
        m_mPlayingMusic.dispose();
        m_txShipCenterImage.dispose();
        m_txShipEdgeImage.dispose();
        m_txMissImage.dispose();
        m_txBoardBg.dispose();
    }
}
