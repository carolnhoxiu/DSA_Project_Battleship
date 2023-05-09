package com.battleship.Controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.battleship.Board.Board;
import com.battleship.Board.Ship.ShotState;

import java.awt.*;

public abstract class BoardController {
    protected Board board;
    protected int boardSize = Board.BOARD_SIZE;

    public BoardController(Texture txBg, Texture txMiss, Texture txCenter, Texture txEdge, Vector2 boardOffset) {
        board = new Board(txBg, txMiss, txCenter, txEdge, boardOffset);
    }

    public Board getBoard() {
        return board;
    }

    protected ShotState fireAtPos(Vector2 point) {
        return board.fireAtPos(point);
    }

    public void reset() {
        board.reset();
    }
}
