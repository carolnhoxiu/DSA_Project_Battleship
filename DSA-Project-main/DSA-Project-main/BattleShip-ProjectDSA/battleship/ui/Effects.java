package battleship.ui;


public class Effects
{

  // dir path to the sound files
  Sound sound = new Sound();

  public void splash()
  {
    play(2);
  }

  public void strike()
  {
    play(1);
  }

  public void sinking()
  {
    play(0);
  }

  private void play(int filename)
  {
	  sound.setFile(filename);
	  sound.play();
  }

}