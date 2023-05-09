package com.battleship.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.battleship.MyBattleshipGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyBattleshipGame();
        }

        @Override
        public ApplicationListener createApplicationListener() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'createApplicationListener'");
        }
}