package battleship.ui;

import java.awt.Cursor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UIMapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static int X = 570;
	static int Y = 630;
	int numC = 10;
	int dimC = 48;
	int oroff = 1;
	int veroff = 1;
	int c1Off = 0;
	int c2Off = 0;
	JButton[][] button;
	JLabel[] COr;
	JLabel[] CVer;
	protected JLabel label;
	UIJPanelBG sea;
	Cursor cursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	Cursor cursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public UIMapPanel(String tab) {

		this.setSize(X, Y);
		this.setLayout(null);
		this.setOpaque(false);
		
		label = new JLabel();
		label.setIcon(new ImageIcon(getClass().getResource(("/res/images/" + tab + ".png"))));
		this.add(label);
		label.setBounds(50, 0, 550, 60);
	
		sea = new UIJPanelBG(
				Toolkit.getDefaultToolkit().createImage(FrameManageship.class.getResource("/res/images/sea.png")));
		sea.setBounds(34, 45, 550, 550);
		button = new JButton[numC][numC];
		ImageIcon gray = new ImageIcon(getClass().getResource("/res/images/grayButtonOpaque.png"));
		for (int i = 0; i < numC; i++) {
			for (int j = 0; j < numC; j++) {
				button[i][j] = new JButton(gray);
				button[i][j].setSize(dimC, dimC);
				sea.add(button[i][j]);
				button[i][j].setCursor(cursorHand);
				button[i][j].setBorder(null);
				button[i][j].setOpaque(false);
				button[i][j].setBorderPainted(false);
				button[i][j].setContentAreaFilled(false);
				button[i][j].setBounds(oroff, veroff, dimC, dimC);
				if (tab.equals("player")) {
					button[i][j].setCursor(cursorDefault);
					button[i][j].setDisabledIcon(gray);
					button[i][j].setEnabled(false);
				} else {
					button[i][j].setCursor(cursorHand);
				}
				oroff += dimC + 2;
			}
			veroff += dimC + 2;
			oroff = 1;
		}
		oroff = 40;
		veroff = 0;
		JPanel grid = new JPanel(null);
		grid.setOpaque(false);
		grid.add(sea);
		COr = new JLabel[10];
		CVer = new JLabel[10];
		
		for (int i = 0; i < 10; i++) {
			COr[i] = new JLabel();
			CVer[i] = new JLabel();
			grid.add(COr[i]);
			grid.add(CVer[i]);
			CVer[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 1) + ".png")))));
			CVer[i].setBounds(veroff, oroff, dimC, dimC);
			COr[i].setIcon(new ImageIcon(getClass().getResource((("/res/images/coord/" + (i + 11) + ".png")))));
			COr[i].setBounds(oroff, veroff, dimC, dimC);
			oroff += 50;
		}

		this.add(grid);
		grid.setBounds(0, 45, 550, 660);

	}

	void drawShip(int[] dati) {
		int x = dati[0];
		int y = dati[1];
		int dim = dati[2];
		int dir = dati[3];
		ImageIcon shipDim1orizz = new ImageIcon(
				getClass().getResource("/res/images/shipDim1orizz.png"));
		ImageIcon shipDim1vert = new ImageIcon(getClass().getResource("/res/images/shipDim1vert.png"));
		if (dim == 1) {
			button[x][y].setEnabled(false);
			if (dir == 0)
				button[x][y].setDisabledIcon(shipDim1orizz);
			else if (dir == 1)
				button[x][y].setDisabledIcon(shipDim1vert);
		} else {
			ImageIcon shipHeadLeft = new ImageIcon(
					getClass().getResource("/res/images/shipHeadLeft.png"));
			ImageIcon shipHeadTop = new ImageIcon(
					getClass().getResource("/res/images/shipHeadTop.png"));
			ImageIcon shipBodyLeft = new ImageIcon(
					getClass().getResource("/res/images/shipBodyLeft.png"));
			ImageIcon shipBodyTop = new ImageIcon(
					getClass().getResource("/res/images/shipBodyTop.png"));
			ImageIcon shipFootLeft = new ImageIcon(
					getClass().getResource("/res/images/shipFootLeft.png"));
			ImageIcon shipFootTop = new ImageIcon(
					getClass().getResource("/res/images/shipFootTop.png"));
			if (dir == 0) {
				
				button[x][y].setDisabledIcon(shipHeadLeft);
				button[x][y].setEnabled(false);
				
				for (int i = 1; i < dim - 1; i++) {
					button[x][y + i].setDisabledIcon(shipBodyLeft);
					button[x][y + i].setEnabled(false);
				}
				
				button[x][y + dim - 1].setDisabledIcon(shipFootLeft);
				button[x][y + dim - 1].setEnabled(false);
			} else { 
				
				button[x][y].setDisabledIcon(shipHeadTop);
				button[x][y].setEnabled(false);
			
				for (int i = 1; i < dim - 1; i++) {
					button[x + i][y].setDisabledIcon(shipBodyTop);
					button[x + i][y].setEnabled(false);
				}
				
				button[x + dim - 1][y].setDisabledIcon(shipFootTop);
				button[x + dim - 1][y].setEnabled(false);
			}
		}
	}

}
