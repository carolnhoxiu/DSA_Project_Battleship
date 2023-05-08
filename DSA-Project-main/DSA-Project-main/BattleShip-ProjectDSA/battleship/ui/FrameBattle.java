package battleship.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import battleship.Computer;
import battleship.Map;
import battleship.Ship;
import battleship.Position;
import battleship.Report;

public class FrameBattle implements ActionListener, KeyListener {
	UIMapPanel playerPanel = new UIMapPanel("player");
	UIMapPanel cpuPanel = new UIMapPanel("cpu");
	JFrame frame = new JFrame("Battleship");
	JPanel comandPanel = new JPanel();
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	UIJPanelBG panel = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/battleImg.jpg")));
	Report rep;
	Computer cpu;
	Map cpuMap;
	Map playerMap;
	int numShipPlayer = 10;
	int numShipCPU = 10;
	StringBuilder sb = new StringBuilder();
	boolean b = true;
	UIStatPanel statPlayer;
	UIStatPanel statCPU;
	JPanel targetPanel = new JPanel(null);
	UIJPanelBG target = new UIJPanelBG(
			Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/images/target.png")));
	ImageIcon wreck = new ImageIcon(getClass().getResource("/res/images/wreck.gif"));
	Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	Timer timer;
	boolean turnoDelCPU;

	public FrameBattle(LinkedList<int[]> playerShips, Map map) {
		playerMap = map;
		cpu = new Computer(map);
		cpuMap = new Map();
		cpuMap.fillMapRandom();
		frame.setSize(1080, 700);
		frame.setTitle("Battleship - Pirate Edition");
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.addKeyListener(this);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/images/icon.png")));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		statPlayer = new UIStatPanel();
		statCPU = new UIStatPanel();
		statPlayer.setBounds(30, 595, 500, 80);
		statCPU.setBounds(570, 595, 500, 80);
		frame.add(statPlayer);
		frame.add(statCPU);
		
		targetPanel.setBounds(0, 0, 500, 500);
		targetPanel.setOpaque(false);
		playerPanel.sea.add(targetPanel);

		panel.add(playerPanel);
		playerPanel.setBounds(0, 0, UIMapPanel.X, UIMapPanel.Y);
		playerPanel.setOpaque(false);
		panel.add(cpuPanel);
		cpuPanel.setBounds(540, 0, UIMapPanel.X, UIMapPanel.Y);
		panel.add(comandPanel);
		frame.add(panel);
		frame.setResizable(false);
		timer = new Timer(2000, new GestoreTimer());
		turnoDelCPU = false;

		for (int i = 0; i < cpuPanel.button.length; i++) {
			for (int j = 0; j < cpuPanel.button[i].length; j++) {
				cpuPanel.button[i][j].addActionListener(this);
				cpuPanel.button[i][j].setActionCommand("" + i + " " + j);
			}
		}
		for (int[] v : playerShips) {
			playerPanel.drawShip(v);
		}

	}

	void setBox(Report rep, boolean player) {
		int x = rep.getP().getCoordX();
		int y = rep.getP().getCoordY();
		ImageIcon fire = new ImageIcon(getClass().getResource("/res/images/fireButton.gif"));
		ImageIcon water = new ImageIcon(getClass().getResource("/res/images/grayButton.gif"));
		String cosa;
		if (rep.isShooted())
			cosa = "X";
		else
			cosa = "A";
		UIMapPanel mappanel;
		if (!player) {
			mappanel = playerPanel;
		} else {
			mappanel = cpuPanel;
		}
		if (cosa == "X") {
			mappanel.button[x][y].setIcon(fire);
			mappanel.button[x][y].setEnabled(false);
			mappanel.button[x][y].setDisabledIcon(fire);
			mappanel.button[x][y].setCursor(cursorDefault);
		} else {
			mappanel.button[x][y].setIcon(water);
			mappanel.button[x][y].setEnabled(false);
			mappanel.button[x][y].setDisabledIcon(water);
			mappanel.button[x][y].setCursor(cursorDefault);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (turnoDelCPU)
			return;
		JButton source = (JButton) e.getSource();
		StringTokenizer st = new StringTokenizer(source.getActionCommand(), " ");
		int x = Integer.parseInt(st.nextToken());
		int y = Integer.parseInt(st.nextToken());
		Position newP = new Position(x, y);
		boolean hit = cpuMap.shoot(newP);
		Report rep = new Report(newP, hit, false);
		this.setBox(rep, true);
		if (hit) { 
			Ship shipsunk = cpuMap.sunk(newP);
			if (shipsunk != null) {
				numShipCPU--;
				setsunk(shipsunk);
				if (numShipCPU == 0) {
					Object[] options = { "New match", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You won!", JLabel.CENTER)),
							"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						this.frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}
		} else { 

			if (b) {
				timer.start();
				turnoDelCPU = true;
			}
		}
		frame.requestFocusInWindow();
	}

	private void setsunk(Position p) {
		LinkedList<String> possibility = new LinkedList<String>();
		if (p.getCoordX() != 0) {
			possibility.add("N");
		}
		if (p.getCoordX() != Map.DIM_MAP - 1) {
			possibility.add("S");
		}
		if (p.getCoordY() != 0) {
			possibility.add("O");
		}
		if (p.getCoordY() != Map.DIM_MAP - 1) {
			possibility.add("E");
		}
		String direction;
		boolean found = false;
		Position posCurrent;
		do {
			posCurrent = new Position(p);
			if (possibility.isEmpty()) {
				deleteShip(1, statPlayer);
				playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setIcon(wreck);
				playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setEnabled(false);
				playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setDisabledIcon(wreck);
				playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setCursor(cursorDefault);
				return;
			}
			direction = possibility.removeFirst();
			posCurrent.move(direction.charAt(0));
			if (playerMap.hit(posCurrent)) {
				found = true;
			}
		} while (!found);
		int dim = 0;
		posCurrent = new Position(p);
		do {

			playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setIcon(wreck);
			playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setEnabled(false);
			playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setDisabledIcon(wreck);
			playerPanel.button[posCurrent.getCoordX()][posCurrent.getCoordY()].setCursor(cursorDefault);
			posCurrent.move(direction.charAt(0));

			dim++;
		} while (posCurrent.getCoordX() >= 0 && posCurrent.getCoordX() <= 9 && posCurrent.getCoordY() >= 0
				&& posCurrent.getCoordY() <= 9 && !playerMap.water(posCurrent));

		deleteShip(dim, statPlayer);
	}

	private void setsunk(Ship shipsunk) {
		int dim = 0;
		for (int i = shipsunk.getXin(); i <= shipsunk.getXfin(); i++) {
			for (int j = shipsunk.getYin(); j <= shipsunk.getYfin(); j++) {
				cpuPanel.button[i][j].setIcon(wreck);
				cpuPanel.button[i][j].setEnabled(false);
				cpuPanel.button[i][j].setDisabledIcon(wreck);
				cpuPanel.button[i][j].setCursor(cursorDefault);
				dim++;
			}
		}
		deleteShip(dim, statCPU);
	}

	private void deleteShip(int dim, UIStatPanel panel) {
		switch (dim) {
		case 4:
			panel.ships[0].setEnabled(false);
			break;
		case 3:
			if (!panel.ships[1].isEnabled())
				panel.ships[2].setEnabled(false);
			else
				panel.ships[1].setEnabled(false);
			break;
		case 2:
			if (!panel.ships[3].isEnabled())
				if (!panel.ships[4].isEnabled())
					panel.ships[5].setEnabled(false);
				else
					panel.ships[4].setEnabled(false);
			else
				panel.ships[3].setEnabled(false);
			break;
		case 1:
			if (!panel.ships[6].isEnabled())
				if (!panel.ships[7].isEnabled())
					if (!panel.ships[8].isEnabled())
						panel.ships[9].setEnabled(false);
					else
						panel.ships[8].setEnabled(false);
				else
					panel.ships[7].setEnabled(false);
			else
				panel.ships[6].setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int tasto = arg0.getKeyCode();
		if (tasto == KeyEvent.VK_ESCAPE) {
			FrameManageship manage = new FrameManageship();
			manage.setVisible(true);
			frame.setVisible(false);
		}

		sb.append(arg0.getKeyChar());
		if (sb.length() == 4) {
			int z = sb.toString().hashCode();
			if (z == 3194657) {
				sb = new StringBuilder();
				b = !b;
			} else {
				String s = sb.substring(1, 4);
				sb = new StringBuilder(s);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public class GestoreTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			timer.stop();
			boolean flag;

			Report report = cpu.myTurn();
			drawTarget(report.getP().getCoordX() * 50, report.getP().getCoordY() * 50);
			flag = report.isShooted();
			setBox(report, false);
			if (report.issunk()) {
				numShipPlayer--;
				setsunk(report.getP());
				if (numShipPlayer == 0) {
					Object[] options = { "New match", "Exit" };
					int n = JOptionPane.showOptionDialog(frame, (new JLabel("You lost!", JLabel.CENTER)),
							"Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
							options[1]);
					if (n == 0) {
						FrameManageship restart = new FrameManageship();
						restart.setVisible(true);
						frame.setVisible(false);
					} else {
						System.exit(0);
					}
				}
			}

			turnoDelCPU = false;
			if (flag) {
				timer.start();
				turnoDelCPU = true;
			}
			frame.requestFocusInWindow();
		}

	}

	public void drawTarget(int i, int j) {
		target.setBounds(j, i, 50, 50);
		target.setVisible(true);
		targetPanel.add(target);
		targetPanel.repaint();
	}
}
