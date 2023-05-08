package battleship;

import battleship.ui.FrameManageship;
import battleship.ui.FrameSplashscreen;

public class BattleShip {

	public static void main(String[] args) {
		FrameSplashscreen intro = new FrameSplashscreen();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
		intro.setVisible(false);
		
		FrameManageship manage = new FrameManageship();
		
		manage.setVisible(true);
	
	}
}