package com.battleship.Board.Ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.battleship.Board.Board;
import com.battleship.Manager.AssetManager;
import com.battleship.Manager.Game.GameManager;
import com.battleship.Interface.Observer;


import java.util.HashMap;

/**
 *
 * Abstract class for ships that handles interactions between ships, facilitates launching missiles at ships, etc
 * See Ship_* classes for implementations of this class that contain ship-specific info (such as size, name, etc)
 */
public class Ship {
    private static final HashMap<String, Sprite> spriteAsset = setupAsset(AssetManager.getManager().requestAsset((Ship) null));
    private static Sprite hitSprite;    //Image for the ship being hit (inner image)
    private final ShipType type;
    private final Sprite shipSprite; //Image for the ship being ok (outer image)
    private Array<Vector2> hitPositions; //Array of positions that have been hit
    private Array<Vector2> shipPoints; //Array of the all the points of the ship
    private Vector2 orientation;
    private Observer observer;

    //How faded out a sunk ship looks
    public static final float SHIP_SUNK_ALPHA = 0.65f;

    /**
     * Constructor. Requires a sprite for the hit image and one for the non-hit image
     */
    public Ship(ShipType type) {
        this.type = type;
        shipSprite = type.getSprite();
        shipSprite.setOrigin((float) Board.TILE_SIZE / 2, (float) Board.TILE_SIZE / 2);

        reset();    //Set default values
    }

    /**
     * Resets this ship to default state (off board and not hit)
     */
    public void reset() {
        orientation = new Vector2(1, 0);
        hitPositions = new Array();
        shipPoints = new Array();
    }

    private static HashMap<String, Sprite> setupAsset(HashMap<String, Sprite> sprites) {
        hitSprite = sprites.get("hitSprite");

        return sprites;
    }

    //Getter/setter methods
    public Vector2 getOrientation(){
        return orientation;
    }

    public Vector2 getPosition() {
        if(isPlaced()) {
            return shipPoints.get(0).cpy();
        }
        else
            return null;
    }

    public ShipType getType() {
        return type;
    }

    public boolean isPlaced() {
        if (shipPoints.isEmpty()) {
            return false;
        } else
            return true;
    }

    public boolean isHorizontal(){
        return getOrientation().x == 1;
    }

    //Returns true if this ship has been sunk, false otherwise
    public boolean isSunk() {
        return hitPositions.size == type.getSize();
    }

    public boolean beenHit() {
        return hitPositions.size > 0;
    }

    public void updatePosition(Vector2 position, boolean horizontal) { //Sets the ship position
        if(!position.equals(getPosition()) || horizontal != isHorizontal()) {
            orientation = horizontal ? new Vector2(1, 0) : new Vector2(0, 1);

            shipPoints.clear();
            for (int i = 0; i < type.getSize(); i++)
                shipPoints.add(position.cpy().mulAdd(orientation, i));
        }
    }


    /**
     * Fires at this ship. Returns true and marks as hit if hit, returns false on miss
     * @return  true on hit, false on miss
     */
    public ShotState fireAtShip(Vector2 point) {
        attachObserver();

        hitPositions.add(point.cpy());

        if(isSunk()) {
            notifyObserver();
            return ShotState.SUNK;
        } else
            return ShotState.HIT;
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
    public void draw(boolean bHidden, Batch bBatch, Vector2 offset) {
        if(getPosition() == null || hitSprite == null || shipSprite == null)
            return;

        int tileSize = Board.TILE_SIZE;
        Vector2 drawPosition = getPosition().scl(tileSize).add(offset);

        shipSprite.setRotation(isHorizontal() ? 0 : 90);

        //Change ship's appearance slightly if it's been sunk
        if(isSunk()) {
            hitSprite.setColor(1, 1, 1, SHIP_SUNK_ALPHA); //Draw at half alpha
            shipSprite.setColor(1, 1, 1, SHIP_SUNK_ALPHA);

            shipSprite.setPosition(drawPosition.x, drawPosition.y);
            shipSprite.draw(bBatch);
        }

        //Only draw ship tiles that have been hit (enemy board generally)
        if(!bHidden) {
            //Draw all ship tiles first
                shipSprite.setPosition(drawPosition.x, drawPosition.y);
                shipSprite.draw(bBatch);
        }

        for(Vector2 point : hitPositions) {
            drawPosition = offset.cpy().mulAdd(point, tileSize);
            hitSprite.setPosition(drawPosition.x, drawPosition.y);
            hitSprite.draw(bBatch);
        }

        if(isSunk()) {
            //Reset to default color since other ships share this sprite
            shipSprite.setColor(Color.WHITE);
            hitSprite.setColor(Color.WHITE);
        }
    }
}


























