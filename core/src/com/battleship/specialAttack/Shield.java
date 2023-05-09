package com.battleship.specialAttack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import java.awt.*;

public class Shield {
    public Sprite shieldSprite;
    private Array<Point> shieldPosition;
    private boolean shieldActivate;

    public Shield(Sprite shieldSprite){
        this.shieldSprite = shieldSprite;
    }

    public void setPosition(Point point){
        shieldPosition.add(point);
    }

    public boolean activateShield(){
        shieldActivate = true;
        return true;
    }
    public boolean deactivateShield(Point point){
        for (Point shieldPoint : shieldPosition){
            if (shieldPoint.equals(point)){
                shieldPosition.removeValue(point, false);
            }
        }
        shieldActivate = false;
        return true;
    }
    public void draw(boolean bHidden, Batch bBatch, Point offset){
        if(shieldPosition == null || shieldSprite == null) return;
        if(bHidden){
            for (Point point : shieldPosition){
                    shieldSprite.setTexture(new Texture("shield.png"));
                    shieldSprite.setPosition(point.x * shieldSprite.getWidth() + offset.x, (point.y) * shieldSprite.getHeight() + offset.y);
                    shieldSprite.draw(bBatch);
                break;
            }
        }
        else{
            for (Point point : shieldPosition){
                shieldSprite.setTexture(new Texture("shield.png"));
                shieldSprite.setPosition(point.x * shieldSprite.getWidth() + offset.x, (point.y) * shieldSprite.getHeight() + offset.y);
                shieldSprite.draw(bBatch);
                break;
            }
        }
    }
}
