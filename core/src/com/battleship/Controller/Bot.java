/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.battleship.Controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.battleship.MyBattleshipGame;
import com.battleship.Board.Ship.ShipType;
import com.battleship.Board.Ship.ShotState;

import java.awt.*;

import static com.battleship.Board.Board.BOARD_SIZE;

public class Bot extends BoardController {
    enum ATTACK_STATE {
        SEEK,
        DESTROY;
    }
    Array<Array<Boolean>> map;
    ATTACK_STATE currentMode;
    int checkLength;
    Vector2 lastHit;
    Vector2 firstHit;
    int destroyDirection;
    Array<Point> direction;
    int shipLength;
    Array<Integer> sizesOfDestroyedShips;
    public static int attackMode;
    /**
     * Constructor for creating a Board class object
     *
     * @param txBg     Background board texture to use as the backdrop when drawing the board
     * @param txMiss   Texture used for drawing guesses that missed
     * @param txCenter Texture used for drawing the central portion of ships that have been hit
     * @param txEdge   Texture used for drawing the edge of ships
     */
    public Bot(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge) {
        super(txBg, txMiss, txCenter, txEdge, MyBattleshipGame.playerBoardOffset);

        currentMode = ATTACK_STATE.SEEK;
        sizesOfDestroyedShips = new Array<>();
        direction = new Array<>(new Point[]{new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)});
        checkLength = 1;

        map = new Array<>();
        for (int i = 0; i < boardSize; i++){
            Array<Boolean> temp = new Array();
            for(int j = 0; j < BOARD_SIZE; j++)
                temp.add(false);
            map.add(temp);
        }
    }

    public void placeShipRandom(){
        int xPos, yPos;
        boolean horizontal;
        for (ShipType type : ShipType.values()){
            do {
                horizontal = MathUtils.randomBoolean();

                xPos = MathUtils.random(0, boardSize - (horizontal ? 1 : 0) * type.getSize() - 1);
                yPos = MathUtils.random(0, boardSize - (horizontal ? 0 : 1) * type.getSize() - 1);

            }while(!board.placeShip(new Vector2(xPos, yPos), type, horizontal));
        }

        for(int i = 0; i < 12 ; i ++) {
            for (int j = 0; j < 12; j++) {
                System.out.print((board.shipPositions.get(j).get(i) == null ? "*" : board.shipPositions.get(j).get(i).getType().getId()) + " ");
            }
            System.out.println();
        }
    }

    public ShotState attack(BoardController opponent) {
        attackMode = 1;
        Vector2 firePos;
        if(currentMode == ATTACK_STATE.SEEK)
            firePos = seek();
        else
            firePos = destroy();

        for(int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++)
                System.out.print((map.get(j).get(i) == true ? 1 : "*") + "  ");

            System.out.println();
        }

        System.out.println();
        System.out.println();

        ShotState result = opponent.fireAtPos(firePos);
        updateAlgo(result, firePos);

        return result;
    }

    private Vector2 seek() {
        int x, y;
        do {
            x = MathUtils.random(0, BOARD_SIZE - 1);
            y = MathUtils.random(0, BOARD_SIZE - 1);

            while(y % 2 != x % 2)
                y = MathUtils.random(0, BOARD_SIZE - 1);
        } while (!checkValid(x, y));

        map.get(x).set(y, true);
        return new Vector2(x, y);
    }

    private Vector2 destroy() {
        System.out.println("Destroy");

        int x = (int) (lastHit.x + direction.get(destroyDirection).x);
        int y = (int) (lastHit.y + direction.get(destroyDirection).y);

        map.get(x).set(y, true);
        return new Vector2(x, y);
    }

    private boolean checkValid(int x, int y){
        if(x >= 0 && y >= 0 && x < boardSize && y < boardSize)
            return !map.get(x).get(y);

        return false;
    }

    private void updateAlgo(ShotState result, Vector2 firePos) {
        if(result == ShotState.HIT) {
            for(int i = -1 ; i <= 1; i++)
                for(int j = -1; j <= 1; j++)
                    if(checkValid((int) (firePos.x + j), (int) (firePos.y + i)))
                        map.get((int) (firePos.x + j)).set((int) (firePos.y + i), true);

        }

        if(currentMode == ATTACK_STATE.SEEK && result == ShotState.HIT){
            currentMode = ATTACK_STATE.DESTROY;

            lastHit = firePos;
            firstHit = firePos;
            shipLength = 1;

            destroyDirection = -1;
            do {
                destroyDirection = (destroyDirection + 1) % 4;
            }while (checkValid((int) (lastHit.x + direction.get(destroyDirection).x), (int) (lastHit.y + direction.get(destroyDirection).x)));
        } else if(currentMode == ATTACK_STATE.DESTROY) {
            switch (result) {
                case HIT:
                    lastHit = firePos;

                    int x = (int) (firePos.x + direction.get(destroyDirection).x);
                    int y = (int) (firePos.y + direction.get(destroyDirection).y);
                    if(x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
                        lastHit = firstHit;
                        destroyDirection = (destroyDirection + 2) % 4;
                    }
                    break;
                case MISS:
                    if(lastHit.equals(firstHit)) {
                        do {
                            destroyDirection = (destroyDirection + 1) % 4;
                        } while (checkValid((int) (firePos.x + direction.get(destroyDirection).x), (int) (firePos.y + direction.get(destroyDirection).y)));
                    } else {
                        lastHit = firstHit;
                        destroyDirection = (destroyDirection + 2) % 4;
                    }
                    break;
                case SUNK:
                    currentMode = ATTACK_STATE.SEEK;
                    break;
            }
        }
    }
}
