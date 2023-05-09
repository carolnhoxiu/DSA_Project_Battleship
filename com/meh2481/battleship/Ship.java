package com.meh2481.battleship;
/* Tran Thanh Tung ITITIU20347
 * Tran Thi Ngoc Tu ITITIU20338
 * Vo Dang Trinh ITITIU20326
 * Lam Nguyen Phuong Uyen ITITIU20348
 */
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
    private Point horizontal; // orientation vector
    public boolean beenHit; //if ship has been hit by a bomb

    //How faded out a sunk ship looks
    public static final float SHIP_SUNK_ALPHA = 0.65f;

    /**
     * Constructor. Requires a sprite for the hit image and one for the non-hit image
     * @param sShipHit  LibGDX sprite to use when drawing the center part of the ship (hit image)
     * @param sShipOK   LibGDX sprite to use when drawing the outside edge of the ship
     */
    public Ship(Sprite sShipHit, Sprite sShipOK, ShipType type)
    {
        m_sShipHitSprite = sShipHit;
        m_sShipOKSprite = sShipOK;
        this.type = type;
        reset();    //Set default values
    }

    //Getter/setter methods
    public Point getHorizontal(){
        return horizontal;
    }

    public boolean isHorizontal(){
        return horizontal.x == 1;
    }

    //Returns true if this ship has been sunk, false otherwise
    public boolean isSunk() {
        return m_iHitPositions.size == type.size;
    }

    public Point getPosition() {
        return position;
    }

    public ShipType getType() {
        return type;
    }

    public void updatePosition(int x, int y, boolean horizontal) { //Sets the ship position
        position = new Point(x, y);
        setPointsAndOrientation(horizontal);
    }

    public void updatePosition(Point position, boolean horizontal) { //Sets the ship position
        this.position = position;
        setPointsAndOrientation(horizontal);
    }

    private void setPointsAndOrientation(boolean horizontal) { //Set the orientation and translate from start position to the points of the ship
        this.horizontal = horizontal ? new Point(1, 0) : new Point(0, 1);

        pointsOfShip= new Array<>();
        for (int i = 0; i < type.size; i++)
            pointsOfShip.add(new Point(this.position.x + this.horizontal.x * i, this.position.y + this.horizontal.y * i));
    }

    /**
     * Resets this ship to default state (off board and not hit)
     */
    public void reset() {
        beenHit = false;
        m_iHitPositions = new Array<>();
        position = new Point();
        pointsOfShip = new Array<>();
        horizontal =  new Point(1, 0);
    }

    /**
     * Fires at this ship. Returns true and marks as hit if hit, returns false on miss
     * @return  true on hit, false on miss
     */
    public boolean fireAtShip(Point point) {
        beenHit = true;
        if(isHit(point)) {
            m_iHitPositions.add(new Point(point));
            return true;
        }

        return false;
    }

    private boolean isHit(Point point) {
        for(int i = 0; i < type.size; i++)
            if(pointsOfShip.contains(point, false))
                return true;

        return false;
    }

    /**
     * Draw the ship to the specified SpriteBatch
     * @param bHidden   if ship should be considered "hidden," that is only tiles that have previously been hit should be drawn
     * @param bBatch    LibGDX batch to draw the ship to
     */
    public void draw(boolean bHidden, Batch bBatch, Point offset) {
        if(position == null || m_sShipHitSprite == null || m_sShipOKSprite == null) return; //Don't draw if no sprite textures

        //Change ship's appearance slightly if it's been sunk
        if(isSunk()) {
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

        if(isSunk()) {
            m_sShipOKSprite.setColor(Color.WHITE);  //Reset to default color since other ships share this sprite
            m_sShipHitSprite.setColor(Color.WHITE);
        }
    }
}


























