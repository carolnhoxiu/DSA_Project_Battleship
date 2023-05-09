/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.battleship.Board.Ship;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;

public enum ShipType {
    CARRIER     (6, "Carrier", 5),
    BATTLESHIP  (5, "Battleship", 4),
    CRUISER     (4, "Cruiser", 3),
    SUBMARINE   (3, "Submarine", 3),
    DESTROYER   (2, "Destroyer", 2);

    private int id;
    private int size;
    private String name;
    private Sprite sprite;
    ShipType(int id, String name, int size) {
        this.id = id;
        this.size = size;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public static void setAsset(HashMap<String, Sprite> requestedSprites) {
        for(ShipType type : ShipType.values())
            type.sprite = requestedSprites.get(type.name);
    }
}
