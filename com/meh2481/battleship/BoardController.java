/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
package com.meh2481.battleship;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public abstract class BoardController {
    protected Array<Ship> m_lShips;   //Ships on this board
    protected Board board;
    protected int boardSize = Board.BOARD_SIZE;

    public BoardController(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge, Point boardOffset) {
        m_lShips = new Array();
        board = new Board(txBg, txMiss, boardOffset, m_lShips);

        //Create ships and add them to our list
        for(int i = 0; i < 5; i ++)
            m_lShips.add(new Ship(new Sprite(txCenter), new Sprite(txEdge), ShipType.values()[i]));
    }

    public Board getBoard() {
        return board;
    }

    public Ship fireAtPos(Point point) {
        return board.fireAtPos(point);
    }
}
