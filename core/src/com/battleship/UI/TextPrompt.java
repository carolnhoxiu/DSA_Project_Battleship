package com.battleship.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

public class TextPrompt {
    private Batch m_bBatch;
    private ShapeRenderer m_rShapeRenderer;
    private BitmapFont m_ftTextFont;
    private float GAMEOVER_STR_PT;

    private static String sMsg;

    public TextPrompt(Batch batch, ShapeRenderer shapeRenderer, BitmapFont textFont, float gameover) {
        m_bBatch = batch;
        this.m_rShapeRenderer = shapeRenderer;
        this.m_ftTextFont = textFont;
        GAMEOVER_STR_PT = gameover;

        sMsg = "";
    }

    /** Draw a large text message in upper center of screen
     *
     * @param       sMsg     Message to write to screen
     */
    public void drawLgText() {
        final int iTextOffset = 15;  //Used to center textbox around gameover text

        //Draw black box behind text so it shows up better
        m_bBatch.end(); //In order to do this, we need to stop drawing the spritebatch so we can draw shapes
        Gdx.gl.glEnable(GL20.GL_BLEND); //Enable OpenGL blending so the box properly shows up low-alpha
        m_rShapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //Start drawing shapes with a filled background

        m_rShapeRenderer.setColor(0, 0, 0, 0.65f);  //Set the color to a lower-alpha black
        m_ftTextFont.getData().setScale(GAMEOVER_STR_PT);
        m_rShapeRenderer.rect(0, Gdx.graphics.getHeight() / 4 - iTextOffset, 975, m_ftTextFont.getLineHeight());    //Draw behind where the text will be

        m_rShapeRenderer.end(); //Done rendering shapes
        Gdx.gl.glDisable(GL20.GL_BLEND);    //Disable OpenGL blending to get back to previous OpenGL state
        m_bBatch.begin();   //Begin drawing the SpriteBatch again

        //Draw gameover text larger and higher up
        m_ftTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        m_ftTextFont.getData().setScale(GAMEOVER_STR_PT);
        m_ftTextFont.draw(m_bBatch, sMsg, 0, Gdx.graphics.getHeight() / 4, 975, Align.center, false);
    }

    public static void updateMessage(String message) {
        System.out.println("message updated: " + message);
        sMsg = message;


    }
}
