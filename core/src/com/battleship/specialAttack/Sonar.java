package com.battleship.specialAttack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.battleship.Board.Board;
import com.battleship.Board.Ship.Ship;

import java.awt.*;

public class Sonar {
    private Sprite sonarSprite;
    private Sprite foundShipSprite;
    private Array<Point> foundShipPositions;
    private int SONAR_RADIUS = 1;
    private Point point;
    private Array<Array<Ship>> shipPositions;
    
    public Sonar(Sprite sonar, Array<Array<Ship>> ships){
        this.sonarSprite = sonar;
        this.shipPositions = ships;
    }

    public void setPosition(Point point) {
        this.point = point;
    }

    public Array<Point> findShip(Point point){
        for (int i = -SONAR_RADIUS; i <= SONAR_RADIUS; i++){
            for (int j = -SONAR_RADIUS; j<= SONAR_RADIUS; j++){
                if (shipPositions.get(point.x).get(point.y)!= null)
                    foundShipPositions.add(point);
            }
        }
        return foundShipPositions;
    }

    public void moveSonar(Point point){
       if (SONAR_RADIUS + point.x > Board.BOARD_SIZE){
           point.x = Board.BOARD_SIZE - SONAR_RADIUS;
       }
       if (SONAR_RADIUS + point.y > Board.BOARD_SIZE){
           point.x = Board.BOARD_SIZE - SONAR_RADIUS;
       }
       if (point.x - SONAR_RADIUS < 0){
           point.x = SONAR_RADIUS;
       }
       if (point.y - SONAR_RADIUS < 0){
           point.y = SONAR_RADIUS;
       }
       setPosition(point);
    }
    public void draw(boolean bHidden, Batch bBatch, Point offset){
        if(point == null || sonarSprite == null) return;
        if(bHidden){
            sonarSprite.setTexture(new Texture("sonar.png"));
            sonarSprite.setPosition(point.x * sonarSprite.getWidth() + offset.x,  (point.y) * sonarSprite.getHeight() + offset.y);
            sonarSprite.setScale(3,3);
            sonarSprite.draw(bBatch);
        }
        else{
            sonarSprite.setTexture(new Texture("sonar.png"));
            sonarSprite.setPosition(point.x * sonarSprite.getWidth() + offset.x,  (point.y) * sonarSprite.getHeight() + offset.y);
            sonarSprite.setScale(3,3);
            sonarSprite.draw(bBatch);
        }
        for (Point point : foundShipPositions){
            foundShipSprite.setTexture(new Texture(Gdx.files.internal("sonar-found-ship.png")));
            foundShipSprite.setPosition(point.x * foundShipSprite.getWidth() + offset.x, point.y * foundShipSprite.getHeight() + offset.y);
            foundShipSprite.draw(bBatch);
        }
    }
}
