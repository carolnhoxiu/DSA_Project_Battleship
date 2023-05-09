package com.meh2481.battleship.specialAttack;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import java.awt.*;

public class Bomb {
    private Sprite bombSprite;
    public Array<Point> bomb;
    private Point orientation;
    private static final int BOMB_SIZE = 4;

    public Bomb(Sprite bombSprite){
        this.bombSprite = bombSprite;
        this.bomb = new Array<Point>();
    }

    public void updateBombPosition(int x, int y, boolean horizontal){
        this.orientation = horizontal ? new Point(1, 0) : new Point(0, 1);
        for (int i = 0; i < BOMB_SIZE; i++) {
            bomb.clear();
            bomb.add(horizontal ? new Point(x + i, y) : new Point(x, y + i));
        }
    }

}
