package com.battleship.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.battleship.Board.Board;
import com.battleship.Manager.Game.GameManager;
import com.battleship.MyBattleshipGame;

import java.awt.*;

public class InputHandler implements InputProcessor {
    private Vector2 mouseCursorTile;
    private Vector2 offset = MyBattleshipGame.playerBoardOffset;
    private GameManager game;

    public InputHandler() {
        this.game = GameManager.getManager();
        mouseCursorTile = new Vector2(1, 1);

        game.setMouseCursor(mouseCursorTile);
    }

    /**
     * Called by LibGDX when a key is pressed
     * @param keycode   key that was pressed
     * @return  whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.ESCAPE)    //Exit game on Escape
            Gdx.app.exit();

        else if(keycode == Input.Keys.F5)   //Start new game on F5
            game.reset();

        else if(keycode == Input.Keys.D)    //Change difficulty on D
            game.changeDifficulty();

        else return false;

        return true;
    }

    /**
     * Called by LibGDX when a mouse click even occurs
     * @param screenX   x position of mouse cursor on screen
     * @param screenY   y position of mouse cursor on screen
     * @param pointer   the pointer for the event
     * @param button    mouse button that was pressed
     * @return          true if input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT)    //Clicking left mouse button
            game.leftClick();
        else if (button == Input.Buttons.RIGHT)  //Clicking right mouse button
            game.rightClick();
        else return false;

        return true;
    }

    /**
     * Called by LibGDX when the mouse cursor moves
     * @param screenX   new mouse cursor x position
     * @param screenY   new mouse cursor y position
     * @return          true if input processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //Find the tile the player moved the mouse to
        int x = screenX < offset.x ? 0 : (int) ((screenX - offset.x) / Board.TILE_SIZE);
        int y = screenY < offset.y ? 0 : (int) ((screenY - offset.y) / Board.TILE_SIZE);
        mouseCursorTile.set(x, y);
        game.setMouseCursor(mouseCursorTile);

        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
