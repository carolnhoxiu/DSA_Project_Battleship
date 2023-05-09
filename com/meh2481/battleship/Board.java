package com.meh2481.battleship;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import java.awt.*;

/**
 /* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 Purpose: place to controll and display all things related to ships
 */
public class Board
{
    public static final int BOARD_SIZE = 12;    //X and Y size of the board, in tiles 12
    public static final int TILE_SIZE = 75;     //Size of each tile, in pixels 64
    public final Point offset;

    private Texture m_txBoardBg;    //Texture for the board background
    private Texture m_txMissImage;  //Image to draw when we've guessed somewhere and missed
    protected Array<Ship> m_lShips;   //Ships on this board
    private Array<Point> guessPos;   //Places on the map that have been guessed already, and were misses
    public Array<Array<Ship>> shipPositions;

    /**
     * Constructor for creating a Board class object
     * @param offset
     * @param txBg     Background board texture to use as the backdrop when drawing the board
     * @param txMiss   Texture used for drawing guesses that missed
     */
    public Board(Texture txBg, Texture txMiss, Point offset, Array<Ship> ships) {
        this.offset = offset;
        //Hang onto the board background and miss tile textures
        m_txBoardBg = txBg;
        m_txMissImage = txMiss;

        //Create arrays
        guessPos = new Array<Point>();
        m_lShips = ships;

        shipPositions = new Array<>();
        for (int i = 0; i < BOARD_SIZE; i++){
            Array<Ship> temp = new Array();
            temp.setSize(BOARD_SIZE);
            shipPositions.add(temp);
        }
    }

    /**To place a ship at a certain position. It checks if the position is placeable then save the position of the ship
     * @param ship ship to place
     * @param horizontal ship orientation
     */
    public boolean placeShip(Point point, Ship ship, boolean horizontal){
        if (checkOK(ship, point)){
            ship.updatePosition(point, horizontal);

            for(int i = 0; i < ship.type.size; i++)
                shipPositions.get(point.x + ship.getHorizontal().x * i).set(point.y + ship.getHorizontal().y * i, ship);

            return true;
        }

        return false;
    }

    /** Checks if ship at certain position is outside the border or overlaps with other ships
     * @param ship ship to check
     */
    public boolean checkOK(Ship ship, Point point){
        if (point.x < 0 || point.x + ship.getHorizontal().x * ship.getType().size > BOARD_SIZE
                || point.x < 0 || point.y + ship.getHorizontal().y * ship.getType().size > BOARD_SIZE){
            System.out.println("Out of BOund " + point);
            return false;
        } else for (int i = 0; i < ship.type.size; i++)
            if (shipPositions.get(point.x + ship.getHorizontal().x * i).get(point.y + ship.getHorizontal().y * i) != null) {
                System.out.println("OVerlap");
                return false;
            }

        return true;
    }

    /** Fire at this position, returning ship that was hit or null on miss
     * @param       point     position to fire to
     * @return      Ship that was hit or null on miss
     */
    public Ship fireAtPos(Point point) {
        Ship ship = shipPositions.get(point.x).get(point.y);
        guessPos.add(new Point(point)); //Miss; add to our miss positions and return nothing

        if(ship != null) {
            ship.fireAtShip(point);
            return ship;
        }

        return null;
    }

    /** Test if we've already fired a missile at this position
     * @return  true if we have fired at this position already, false if not
     */
    public boolean alreadyFired(Point point) {
        return guessPos.contains(point, false);
    }

    /**
     * Reset the board to uninitialized state
     */
    public void reset() {
        for(Ship s : m_lShips)
            s.reset();

        guessPos.clear();
    }

    /**
     * Test to see if every ship is sunk
     * @return true if every ship on the board is sunk, false otherwise
     */
    public boolean boardCleared() {
        for(Ship s : m_lShips)
            if(!s.isSunk())
                return false;

        return true;
    }

    /**
     * Get the number of ships that haven't been sunk yet
     * @return  Number of ships that are still afloat
     */
    public int shipsLeft() {
        int numLeft = 0;
        for(Ship s : m_lShips) {
            if(!s.isSunk())
                numLeft++;
        }
        return numLeft;
    }

    public Ship fireHorizontalBomb(int xPos, int yPos){
        for (Ship ship : m_lShips){
            if(true)
                return ship;
        }
        return null;
    }

    public Ship fireVerticalBomb(int xPos, int yPos){
        for (Ship ship : m_lShips){
            if(true)
                return ship;
        }
        return null;
    }

    /**Special feature - ship is allowed to teleport to a different position on the board if it has not been hit and the new position has not been hit 
     * @param xPos new x position
     * @param yPos new y position
     * @param ship the ship to teleport
     * @param horizontal new orientation
    */
    public void teleport(Point point, Ship ship, boolean horizontal){
        if (ship.beenHit = false){
            if (!alreadyFired(point)){
                placeShip(point, ship, horizontal);
            }
        }
    }
    public void placeSonar(int xPos, int yPos){
        
    }

    public void placeShield(int xPos, int yPos){

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
        for(Point p : guessPos)
            bBatch.draw(m_txMissImage, p.x * TILE_SIZE + offset.x, p.y * TILE_SIZE + offset.y);
        //Draw ships
        for(Ship s : m_lShips)
            s.draw(bHidden, bBatch, offset);
    }
}













