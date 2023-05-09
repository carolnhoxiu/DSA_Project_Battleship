package com.battleship.Manager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.battleship.Board.Board;
import com.battleship.Board.Ship.Ship;
import com.battleship.Board.Ship.ShipType;
import java.util.HashMap;

public class AssetManager {
    private static AssetManager manager;

    public static void createManager() {
        if(manager == null)
            manager = new AssetManager();
        else
            System.out.println("Asset manager has been created.");
    }

    private AssetManager() {}

    public static AssetManager getManager() {
        return manager;
    }

    /**
     * Ship sprites
     */

    private final Sprite shipHit = new Sprite(new Texture("Sprite/hit.png"));    //Image for the ship being hit (inner image)
    private final Sprite shipTypeCarrier = new Sprite(new Texture("Sprite/carrier-horizontal.png"));
    private final Sprite shipTypeBattleship = new Sprite(new Texture("Sprite/battleship-horizontal.png"));
    private final Sprite shipTypeCruiser = new Sprite(new Texture("Sprite/cruiser-horizontal.png"));
    private final Sprite shipTypeSubmarine = new Sprite(new Texture("Sprite/submarine-horizontal.png"));
    private final Sprite shipTypeDestroyer = new Sprite(new Texture("Sprite/destroyer-horizontal.png"));

    public HashMap<String, Sprite> requestAsset(Ship ship) {
        setShipTypeSprite();

        HashMap<String, Sprite> requestedSprites = new HashMap<>();
        requestedSprites.put("hitSprite", shipHit);

        return requestedSprites;
    }

    private void setShipTypeSprite() {
        HashMap<String, Sprite> sprites = new HashMap<>();

        sprites.put("Carrier", shipTypeCarrier);
        sprites.put("Battleship", shipTypeBattleship);
        sprites.put("Cruiser", shipTypeCruiser);
        sprites.put("Submarine", shipTypeSubmarine);
        sprites.put("Destroyer", shipTypeDestroyer);

        ShipType.setAsset(sprites);
    }


    /**
     * Board sprites
     */

    private final Sprite boardMiss = new Sprite(new Texture("Sprite/miss.png"));
    private final Sprite boardBackground = new Sprite(new Texture("Sprite/map.png"));

    public HashMap<String, Sprite> requestAsset(Board board) {
        HashMap<String, Sprite> requestedSprites = new HashMap<>();
        requestedSprites.put("missSprite", boardMiss);
        requestedSprites.put("backgroundSprite", boardBackground);

        return requestedSprites;
    }
}
