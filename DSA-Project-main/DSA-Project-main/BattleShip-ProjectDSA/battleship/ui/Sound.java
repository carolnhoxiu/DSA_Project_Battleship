package battleship.ui;

import java.net.URL;
import javax.sound.sampled.*;


public class Sound{
	Clip clip;
	URL soundURL[] = new URL[5];
	
	public Sound() {
		soundURL[0] = getClass().getResource("/res/sounds/sinking.wav");
		soundURL[1] = getClass().getResource("/res/sounds/strike.wav");
		soundURL[2] = getClass().getResource("/res/sounds/water_splash.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		
	}
	
	public void stop() {
		
	}
}






















///**
// * Sound Manager class
// * 
// * @date 3.12.2012
// * @author Christian Metz
// * @version 1.5
// */
//public class Sound extends Thread
//{
//
//  private BufferedInputStream file = null;
//  private Player player = null;
//
//  public Sound(String filepath)
//  {
//    try {
//      InputStream input = getClass().getResourceAsStream(filepath);
//      file = new BufferedInputStream(input);
//      player = new Player(file);
//    } catch (Exception e) {
//      System.out.println(e);
//    }
//  }
//
//  public void playSound()
//  {   
//    start();
//  }
//
//  @Override
//  public void run() 
//  {
//    try {
//      player.play();
//    } catch (JavaLayerException e) {
//      System.out.println("Soundmanager error: " + e.getMessage());
//    }
//  }

/*
 
SwingWorker sw=new SwingWorker()
{
@Override
protected Object doInBackground() throws Exception
{
InputStream in=null;
.....abspielen....
return null;
}
};
sw.execute();
}
 */

//}