package com.meh2481.battleship.specialAttack;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.meh2481.battleship.Board;
import com.meh2481.battleship.Ship;

import java.awt.*;

public class Sonar {
    private Sprite sonarSprite;
    private Array<Integer> foundShipPositions;
    private int SONAR_SIZE = 3;
    private int xPos, yPos;
    private Array<Ship> ships;

    public void setPosition(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public Sonar(Sprite sonar, Array<Ship> ships){
        this.sonarSprite = sonar;
        this.ships = ships;
    }

    private Array<Integer> findShip(int xPos, int yPos){
        for (Ship ship : ships){
            int radius = (SONAR_SIZE-1)/2;
            for (int i = -radius; i <= radius; i++){
                for (int j = -radius; j<= radius; j++){
//                    if (ship.isHit(xPos + i, yPos+j)){
//                        Array<Integer> result = new Array<>();
//                        result.add(xPos + i);
//                        result.add(yPos + j);
//                        return result;
//                    }
                }
            }
        }
        return null;
    }

    public void moveSonar(int xPos, int yPos){
       if (SONAR_SIZE + xPos > Board.BOARD_SIZE){
           xPos = Board.BOARD_SIZE - SONAR_SIZE;
       }
       if (SONAR_SIZE + yPos > Board.BOARD_SIZE){
           yPos = Board.BOARD_SIZE - SONAR_SIZE;
       }
       setPosition(xPos, yPos);
    }
    public void placeSonar(int xPos, int yPos){
        moveSonar(xPos, yPos);
        findShip(xPos, yPos);
    }
}
