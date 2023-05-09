/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.battleship.Controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.battleship.Board.BoardPlayer;
import com.battleship.MyBattleshipGame;
import com.battleship.Board.Ship.ShipType;
import com.battleship.Board.Ship.ShotState;

import java.awt.*;

/**
 * Created by Mark on 1/18/2016.
 *
 * Child class of Board for facilitating the player placing ships on their board. Includes methods for moving &
 * rotating ships as you place them.
 */
public class Player extends BoardController {
    private int m_iPlacing;    //For handling ship placement - current ship we're placing
    BoardPlayer playerBoard;

    /**
     * Construct a new board for the player to use (also see Board.Board())
     * @param txBg      Background texture for board
     * @param txMiss    Texture for drawing where the player has missed guesses
     * @param txCenter  Texture for drawing the center of ships when they are hit
     * @param txEdge    Texture for drawing the edge of ships
     */
    public Player(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge) {
        super(txBg, txMiss, txCenter, txEdge, MyBattleshipGame.playerBoardOffset);//Construct default class
        board = new BoardPlayer(txBg, txMiss, txCenter, txEdge, MyBattleshipGame.playerBoardOffset);
        playerBoard = (BoardPlayer)board;

        m_iPlacing = -1;    //Not currently placing a ship
    }

    public void startPlacingShips() {
        m_iPlacing = 0;
    }   //Init ship placement by placing ship 0 (first ship)

    public boolean placeShip(Vector2 point){
        if (playerBoard.playerPlace(ShipType.values()[m_iPlacing], point))
            m_iPlacing++;

        return !(m_iPlacing >= 0 && m_iPlacing < ShipType.values().length);
    }

    public void previewShip(Vector2 point) {
            playerBoard.previewShip(ShipType.values()[m_iPlacing], point);
    }

    public void rotateShip() {
            playerBoard.rotateShip(ShipType.values()[m_iPlacing]);
    }

    public ShotState fireAtOpponent(BoardController opponent, Vector2 point) {
        return opponent.fireAtPos(point);
    }
}
