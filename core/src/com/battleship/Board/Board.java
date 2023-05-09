/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.battleship.Board;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.battleship.Board.Ship.Ship;
import com.battleship.Board.Ship.ShipType;
import com.battleship.Board.Ship.ShotState;
import com.battleship.specialAttack.Sonar;
import com.battleship.specialAttack.Bomb;
import com.battleship.specialAttack.Shield;

import java.awt.*;
import java.util.HashMap;

/**
 *
 *
 * Defines a class for handling how ships are placed on the Battleship board, and for causing interactions
 * (such as firing at a ship, placing ships randomly on the board, etc)
 */
public class Board
{
    public static final int BOARD_SIZE = 12;    //X and Y size of the board, in tiles 12
    public static final int TILE_SIZE = 75;     //Size of each tile, in pixels 64
    public final Vector2 offset;

    private Texture m_txBoardBg;    //Texture for the board background
    private Texture m_txMissImage;  //Image to draw when we've guessed somewhere and missed
    protected HashMap<ShipType, Ship> m_lShips;   //Ships on this board
    private Array<Vector2> guessPos;   //Places on the map that have been guessed already, and were misses
    public Array<Array<Ship>> shipPositions;
    public Shield shield;
    public Sonar sonar;
    public Bomb bomb;

    /**
     * Constructor for creating a Board class object
     * @param offset
     * @param txBg     Background board texture to use as the backdrop when drawing the board
     * @param txMiss   Texture used for drawing guesses that missed
     */
    public Board(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge, Vector2 offset) {
        this.offset = offset;
        //Hang onto the board background and miss tile textures
        m_txBoardBg = txBg;
        m_txMissImage = txMiss;

        //Create arrays
        guessPos = new Array<Vector2>();
        m_lShips = new HashMap<>();

        shipPositions = new Array<>();
        for (int i = 0; i < BOARD_SIZE; i++){
            Array<Ship> temp = new Array();
            temp.setSize(BOARD_SIZE);
            shipPositions.add(temp);
        }

        //Create ships and add them to our list
        for(int i = 0; i < 5; i ++)
            m_lShips.put(ShipType.values()[i], new Ship(ShipType.values()[i]));
    }

    /**To place a ship at a certain position. It checks if the position is placeable then save the position of the ship
     * @param ship ship to place
     * @param horizontal ship orientation
     */
    public boolean placeShip(Vector2 point, ShipType type, boolean horizontal){
        if (checkOK(type, point, horizontal)){
            Ship ship = m_lShips.get(type);
            ship.updatePosition(point, horizontal);

            for(int i = 0; i < ship.getType().getSize(); i++)
                shipPositions.get((int) (point.x + ship.getOrientation().x * i)).set((int) (point.y + ship.getOrientation().y * i), ship);

            return true;
        }

        return false;
    }

    /** Checks if ship at certain position is outside the border or overlaps with other ships
     * @param ship ship to check
     */
    protected boolean checkOK(ShipType type, Vector2 point, boolean horizontal){
        Point orientation = horizontal ? new Point(1, 0) : new Point(0, 1);

        if (point.x < 0 || point.x + orientation.x * type.getSize() > BOARD_SIZE
                || point.x < 0 || point.y + orientation.y * type.getSize() > BOARD_SIZE){
            //System.out.println("Out of Bound " + point);
            return false;
        } else for (int i = 0; i < type.getSize(); i++)
            for(int j = -1; j <= 1; j++)
                for(int k = -1; k <= 1; k++) {
                    int x = (int) (point.x + orientation.x * i + k);
                    int y = (int) (point.y + orientation.y * i + j);

                    if (x < 0) x = 0;
                    if (x >= BOARD_SIZE) x = BOARD_SIZE - 1;

                    if (y < 0) y = 0;
                    if (y >= BOARD_SIZE) y = BOARD_SIZE - 1;

                    if (!(shipPositions.get(x).get(y) == null)) {
                        //System.out.println("OVerlap");
                        return false;
                    }
            }

        return true;
    }

    /** Fire at this position, returning ship that was hit or null on miss
     * @param       point     position to fire to
     * @return      Ship that was hit or null on miss
     */
    public ShotState fireAtPos(Vector2 point) {
        Ship ship = shipPositions.get((int) point.x).get((int) point.y);
        guessPos.add(new Vector2(point)); //Miss; add to our miss positions and return nothing
        if(ship != null) {
            if (ship.fireAtShip(point) == ShotState.SUNK){
                return ShotState.SUNK;
            }
            else return ShotState.HIT;
        }
        else
            return ShotState.MISS;
    }

    public Array<ShotState> fireBomb(){
        Array<ShotState> fireResult = new Array<ShotState>();
        for (Vector2 bombPoint : bomb.bomb){
            fireResult.add(fireAtPos(bombPoint));
        }
        return fireResult;
    }

    /** Test if we've already fired a missile at this position
     * @return  true if we have fired at this position already, false if not
     */
    public boolean alreadyFired(Vector2 point) {
        return guessPos.contains(point, false);
    }

    /**
     * Reset the board to uninitialized state
     */
    public void reset() {
        for(Ship s : m_lShips.values())
            s.reset();

        guessPos.clear();
    }

    /**
     * Test to see if every ship is sunk
     * @return true if every ship on the board is sunk, false otherwise
     */
    /*public boolean boardCleared() {
        for(Ship s : m_lShips.values())
            if(s.isSunk() == null)
                return false;

        return true;
    }*/
    public boolean boardCleared() {
        for(Ship ship : m_lShips.values())
            if (ship.isSunk() == false)
                    return false;

        return true;
    }

    /**Special feature - ship is allowed to teleport to a different position on the board if it has not been hit and the new position has not been hit 
     * @param xPos new x position
     * @param yPos new y position
     * @param ship the ship to teleport
     * @param horizontal new orientation
    */
    public void teleport(Vector2 point, Ship ship, boolean horizontal){
        if (ship.beenHit() == false){
            if (!alreadyFired(point)){
                placeShip(point, ship.getType(), horizontal);
            }
        }
    }
    public Array<Point> placeSonar(Point point){
        sonar.moveSonar(point);
        return sonar.findShip(point);
    }

    public void placeShield(Point point){
        if (shipPositions.get(point.x).get(point.y)!=null){
            shield.setPosition(point);
        }
    }

    /** Draw the board and all ships on it onto the specified Batch.
     *
     * @param    bHidden     true means hide ships that haven't been hit, false means draw all ships
     * @param    bBatch      The batch to draw the board onto
     */
    public void draw(boolean bHidden, Batch bBatch) {
        //Draw board background image
        Sprite bg = new Sprite(m_txBoardBg);
        bg.flip(false, true);
        bg.setPosition(0, 0);
        bg.draw(bBatch);
        //Draw misses
        for(Vector2 p : guessPos)
            bBatch.draw(m_txMissImage, p.x * TILE_SIZE + offset.x, p.y * TILE_SIZE + offset.y);
        //Draw ships
        for(Ship s : m_lShips.values())
            s.draw(bHidden, bBatch, offset);
    }
}













