package com.meh2481.battleship;
/* Tran Thanh Tung ITITIU20347
 * Tran Thi Ngoc Tu ITITIU20338
 * Vo Dang Trinh ITITIU20326
 * Lam Nguyen Phuong Uyen ITITIU20348
 */
import com.badlogic.gdx.graphics.Texture;
import java.awt.*;

/**
 * Created by Mark on 1/18/2016.
 *
 * Child class of Board for facilitating the player placing ships on their board. Includes methods for moving &
 * rotating ships as you place them.
 */
public class Player extends BoardController
{
    private Point m_ptCurPos;   //Hold onto the current position of the ship we're placing
    private int m_iPlacing;    //For handling ship placement - current ship we're placing

    /**
     * Construct a new board for the player to use (also see Board.Board())
     * @param txBg      Background texture for board
     * @param txMiss    Texture for drawing where the player has missed guesses
     * @param txCenter  Texture for drawing the center of ships when they are hit
     * @param txEdge    Texture for drawing the edge of ships
     */
    public Player(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge) {
        super(txBg, txMiss, txCenter, txEdge, MyBattleshipGame.playerBoardOffset);//Construct default class
        m_ptCurPos = new Point(-1,-1);          //Reset current position we're placing a ship
        m_iPlacing = -1;    //Not currently placing a ship
    }

    public void startPlacingShips()
    {
        m_iPlacing = 0;
    }   //Init ship placement by placing ship 0 (first ship)

    /**
     * Place a ship on the board and prepare to place next ship
     * @return      true if this is the last ship and placement should stop, false otherwise
     */
    public boolean placeShip(Point point) {
        if(m_iPlacing >= 0 && m_iPlacing < m_lShips.size) {
            Ship sPlace = m_lShips.get(m_iPlacing);
            int size = sPlace.getType().size;

            if(point.x + sPlace.getHorizontal().x * size > boardSize)
                point.x = boardSize - sPlace.getHorizontal().x * size;

            if(point.y + sPlace.getHorizontal().y * size > boardSize)
                point.y = boardSize - sPlace.getHorizontal().y * size;

            if(board.placeShip(point, m_lShips.get(m_iPlacing), m_lShips.get(m_iPlacing).isHorizontal()))
                m_iPlacing++;   //Go to next ship
        }
        if(m_iPlacing >= m_lShips.size)
            return true;

        return false;
    }

    /**
     * Move the current ship we're going to place next
     * @param xPos  x board position to move the upper left of the ship to
     * @param yPos  y board position to move the upper left of the ship to
     */
    public void previewShip(int xPos, int yPos) {
        m_ptCurPos.x = xPos;
        m_ptCurPos.y = yPos;

        if(m_iPlacing >= 0 && m_iPlacing < m_lShips.size) {
            //Check and be sure we're not off the edge of the map.
            Ship sPlace = m_lShips.get(m_iPlacing);
            int size = sPlace.getType().size;

            if(xPos + sPlace.getHorizontal().x * size > boardSize)
                xPos = boardSize - sPlace.getHorizontal().x * size;

            if(yPos + sPlace.getHorizontal().y * size > boardSize)
                yPos = boardSize - sPlace.getHorizontal().y * size;


            sPlace.updatePosition(xPos, yPos, sPlace.isHorizontal());
        }
    }

    /**
     * Rotate the current ship we're placing (rotate horizontal if vertical & vice versa)
     */
    public void rotateShip() {
        if(m_iPlacing >= 0 && m_iPlacing < m_lShips.size) {
            Ship sPlace = m_lShips.get(m_iPlacing);
            sPlace.updatePosition(sPlace.getPosition(), !sPlace.isHorizontal());
            //Make sure we're not off the map after rotating by moving to the current position
            previewShip(m_ptCurPos.x, m_ptCurPos.y);
        }
    }
    public void teleportShip(){}
}
