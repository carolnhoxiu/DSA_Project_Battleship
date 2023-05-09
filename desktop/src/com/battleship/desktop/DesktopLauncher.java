package com.battleship.desktop;
/* Tran Thanh Tung ITITIU20347

 * Tran Thi Ngoc Tu ITITIU20338

 * Vo Dang Trinh ITITIU20326

 * Lam Nguyen Phuong Uyen ITITIU20348

 */
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.battleship.MyBattleshipGame;

/**
 * Created by Mark on 1/13/2016.
 *
 * Program entry point. Launches LibGDX
 */

public class DesktopLauncher
{
    /**
     * Entry point for main program. Initializes LibGDX and starts main loop
     * @param arg   command-line arguments
     */
    public static void main(String[] arg)
    {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Battleship");
        //config.width = 640;
        config.setWindowedMode(1200,975);
        config.setResizable(false);
        config.setWindowIcon(Files.FileType.Internal, "Sprite/icon_128.png");
        config.setWindowIcon(Files.FileType.Internal, "Sprite/icon_64.png");
        config.setWindowIcon(Files.FileType.Internal, "Sprite/icon_32.png");
        config.setWindowIcon(Files.FileType.Internal, "Sprite/icon_16.png");
        new Lwjgl3Application(new MyBattleshipGame(), config);
    }
}
