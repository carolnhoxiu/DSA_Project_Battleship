package com.battleship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;


import java.awt.*;

/**
 * Created by Mark on 1/15/2016.
 *
 * Abstract class for ships that handles interactions between ships, facilitates launching missiles at ships, etc
 * See Ship_* classes for implementations of this class that contain ship-specific info (such as size, name, etc)
 */
public class Ship
{
    protected ShipType type;
    private Sprite m_sShipHitSprite;    //Image for the ship being hit (inner image)
    private Sprite m_sShipOKSprite; //Image for the ship being ok (outer image)
    private Array<Point> m_iHitPositions; //Array of positions that have been hit
    private Array<Point> pointsOfShip; //Array of the all the points of the ship
    private Point position; // the start position of the ship
    private Point orientation; // orientation vector
    public boolean beenHit; //if ship has been hit by a bomb
    public Observer observer;

    //How faded out a sunk ship looks
    public static final float SHIP_SUNK_ALPHA = 0.65f;

    /**
     * Constructor. Requires a sprite for the hit image and one for the non-hit image
     * @param sShipHit  LibGDX sprite to use when drawing the center part of the ship (hit image)
     * @param sShipOK   LibGDX sprite to use when drawing the outside edge of the ship
     */
    public Ship(Sprite sShipHit, Sprite sShipOK, ShipType type) {
        m_sShipHitSprite = sShipHit;
        m_sShipOKSprite = sShipOK;
        this.type = type;
        reset();    //Set default values
    }

    //Getter/setter methods
    public Point getOrientation(){
        return orientation;
    }

    public boolean isHorizontal(){
        return orientation.x == 1;
    }

    //Returns true if this ship has been sunk, false otherwise
    public ShotState isSunk() {
        if (m_iHitPositions.size == type.size)
            return ShotState.SUNK;

        else return null;  
    }

    public Point getPosition() {
        return position;
    }

    public ShipType getType() {
        return type;
    }

    public void updatePosition(Point position, boolean horizontal) { //Sets the ship position
        attachObserver();
        this.position = position;

        this.orientation = horizontal ? new Point(1, 0) : new Point(0, 1);

        pointsOfShip= new Array<>();
        for (int i = 0; i < type.size; i++)
            pointsOfShip.add(new Point(this.position.x + this.orientation.x * i, this.position.y + this.orientation.y * i));
    }


    /**
     * Resets this ship to default state (off board and not hit)
     */
    public void reset() {
        beenHit = false;
        m_iHitPositions = new Array<>();
        position = new Point();
        pointsOfShip = new Array<>();
        orientation =  new Point(1, 0);
    }

    /**
     * Fires at this ship. Returns true and marks as hit if hit, returns false on miss
     * @return  true on hit, false on miss
     */
    public ShotState fireAtShip(Point point) {
        beenHit = true;
        m_iHitPositions.add(new Point(point));

        if(isSunk() == ShotState.SUNK)
            notifyObserver();

        return isSunk();
}

    public void attachObserver(){
        this.observer = GameManager.getManager();
    }

    public void notifyObserver(){
        observer.updateSunkShip(this.type);
    }
    /**
     * Draw the ship to the specified SpriteBatch
     * @param bHidden   if ship should be considered "hidden," that is only tiles that have previously been hit should be drawn
     * @param bBatch    LibGDX batch to draw the ship to
     */
    public void draw(boolean bHidden, Batch bBatch, Point offset) {
        if(position == null || m_sShipHitSprite == null || m_sShipOKSprite == null) return; //Don't draw if no sprite textures

        //Change ship's appearance slightly if it's been sunk
        if(isSunk() == ShotState.SUNK) {
            m_sShipHitSprite.setColor(1, 1, 1, SHIP_SUNK_ALPHA); //Draw at half alpha
            m_sShipOKSprite.setColor(1, 1, 1, SHIP_SUNK_ALPHA);
        }

        //Only draw ship tiles that have been hit (enemy board generally)
        if(bHidden) {
            for(Point point : m_iHitPositions) {
                //Draw both center and edge for hit tiles
                float x = point.x * m_sShipHitSprite.getWidth() + offset.x;
                float y = point.y * m_sShipHitSprite.getHeight() + offset.y;
                m_sShipOKSprite.setPosition(x, y);
                m_sShipOKSprite.draw(bBatch);
                m_sShipHitSprite.setPosition(x, y);
                m_sShipHitSprite.draw(bBatch);
            }
        }
        //Draw all tiles, including ones that haven't been hit (generally player board)
        else {
            //Draw all ship tiles first
            for (Point point : pointsOfShip) {
                //Draw horizontally or vertically depending on our rotation

                m_sShipOKSprite.setPosition(point.x * m_sShipOKSprite.getWidth() + offset.x, point.y * m_sShipOKSprite.getHeight() + offset.y);
                m_sShipOKSprite.draw(bBatch);
            }
            //Draw image for tiles on the ship that have been hit
            for(Point point : m_iHitPositions) {
                m_sShipHitSprite.setPosition(point.x * m_sShipHitSprite.getWidth() + offset.x, point.y * m_sShipHitSprite.getHeight() + offset.y);
                m_sShipHitSprite.draw(bBatch);
            }
        }

        if(isSunk() == ShotState.SUNK) {
            m_sShipOKSprite.setColor(Color.WHITE);  //Reset to default color since other ships share this sprite
            m_sShipHitSprite.setColor(Color.WHITE);
        }
    }
}


























