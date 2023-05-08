package battleship;

import java.util.LinkedList;
import java.util.Random;


public class Computer {
	private LinkedList<Position> listShoot;
	private Random r;
	private int hit;
	private LinkedList<String> possibility;
	private Position lastShoot;
	private String direction;
	private Map plMap;
	private Position firstShoot;
	

	public Computer(Map mapOpponent) {
		listShoot = new LinkedList<Position>();
		this.plMap = mapOpponent;
		for (int i = 0; i < Map.DIM_MAP; i++) {
			for (int j = 0; j < Map.DIM_MAP; j++) {
				Position p = new Position(i, j);
				listShoot.add(p);
			}
		}
		r = new Random();
		hit = 0;
	}

	public Report myTurn() {

		Report rep = new Report();
		if (hit == 0) {
			boolean hit1 = stepMove();
			rep.setP(lastShoot);
			rep.setShoot(hit1);
			Ship sunk;
			if (hit1) {
				hit++;
				sunk = plMap.sunk(lastShoot);
				if (sunk != null) {
					rep.setSunk(true);
					removeOutlines(sunk);
					hit = 0;
					direction = null;
				} else {
					firstShoot = lastShoot;
					possibility = new LinkedList<String>();
					initializeList();
				}
			}
			return rep;
		} 
		if (hit == 1) {
			boolean hit1 = shootAimed1();
			Ship sunk;
			rep.setP(lastShoot);
			rep.setShoot(hit1);
			rep.setSunk(false);
			if (hit1) {
				hit++;
				possibility = null;
				sunk = plMap.sunk(lastShoot);
				if (sunk != null) {
					rep.setSunk(true);
					removeOutlines(sunk);
					hit = 0;
					direction = null;
				}
			}
			return rep;
		}
		if (hit >= 2) {
			boolean hit1 = shootAimed2();
			Ship sunk;
			rep.setP(lastShoot);
			rep.setShoot(hit1);
			rep.setSunk(false);
			if (hit1) {
				hit++;
				sunk = plMap.sunk(lastShoot);
				if (sunk != null) {
					rep.setSunk(true);
					removeOutlines(sunk);
					hit = 0;
					direction = null;
				}
			} else {
				invertDirection();
			}
			return rep;
		}
		return null;
	}

	private boolean stepMove() {
		int i = r.nextInt(listShoot.size());
		Position p = listShoot.remove(i);
		lastShoot = p;
		boolean hit1 = plMap.shoot(p);
		// if (!hit1) {
		// 	i = i+step;
		// }
		// if (i > listShoot.size()) {
		// 	i = i - listShoot.size();
		// }
		return hit1;
	}

	private boolean shootAimed1() {
		boolean error = true;
		Position p = null;
		do {
			int shot = r.nextInt(possibility.size());
			String dive = possibility.remove(shot);
			p = new Position(firstShoot);
			p.move(dive.charAt(0));
			direction = dive;
			if (!plMap.water(p)) {
				listShoot.remove(p);
				error = false;
			}
		} while (error);
							
		lastShoot = p;
		return plMap.shoot(p);
	}

	private boolean shootAimed2() {
		boolean hitable = false;
		Position p = new Position(lastShoot);
		do {
			p.move(direction.charAt(0));

			if (p.MapLimit() || plMap.water(p)) {
				invertDirection();
			} else {
				if (!plMap.hit(p)) {
					hitable = true;
				}

			}
		} while (!hitable);
		listShoot.remove(p);
		lastShoot = p;
		return plMap.shoot(p);
	}

	//

	private void removeOutlines(Ship sunk) {
		int Xin = sunk.getXin();
		int Xfin = sunk.getXfin();
		int Yin = sunk.getYin();
		int Yfin = sunk.getYfin();
		if (Xin == Xfin) {
			if (Yin != 0) {
				Position p = new Position(Xin, Yin - 1);
				if (!plMap.water(p)) {
					listShoot.remove(p);
					plMap.setWater(p);

				}
			}
			if (Yfin != Map.DIM_MAP - 1) {
				Position p = new Position(Xin, Yfin + 1);
				if (!plMap.water(p)) {
					listShoot.remove(p);
					plMap.setWater(p);
				}
			}
			if (Xin != 0) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin - 1, Yin + i);
					if (!plMap.water(p)) {
						listShoot.remove(p);
						plMap.setWater(p);
					}
				}

			}
			if (Xin != Map.DIM_MAP - 1) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin + 1, Yin + i);
					if (!plMap.water(p)) {
						listShoot.remove(p);
						plMap.setWater(p);
					}
				}
			}
		} else {
			if (Xin != 0) {
				Position p = new Position(Xin - 1, Yin);
				if (!plMap.water(p)) {
					listShoot.remove(p);
					plMap.setWater(p);
				}
			}
			if (Xfin != Map.DIM_MAP - 1) {
				Position p = new Position(Xfin + 1, Yin);
				if (!plMap.water(p)) {
					listShoot.remove(p);
					plMap.setWater(p);
				}
			}
			if (Yin != 0) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin - 1);
					if (!plMap.water(p)) {
						listShoot.remove(p);
						plMap.setWater(p);
					}
				}

			}
			if (Yfin != Map.DIM_MAP - 1) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin + 1);
					if (!plMap.water(p)) {
						listShoot.remove(p);
						plMap.setWater(p);
					}
				}
			}
		}
	}

	private void initializeList() {
		if (lastShoot.getCoordX() != 0) {
			possibility.add("N");
		}
		if (lastShoot.getCoordX() != Map.DIM_MAP - 1) {
			possibility.add("S");
		}
		if (lastShoot.getCoordY() != 0) {
			possibility.add("O");
		}
		if (lastShoot.getCoordY() != Map.DIM_MAP - 1) {
			possibility.add("E");
		}
	}

	private void invertDirection() {
		if (direction.equals("N")) {
			direction = "S";
		} else if (direction.equals("S")) {
			direction = "N";
		} else if (direction.equals("E")) {
			direction = "O";
		} else if (direction.equals("O")) {
			direction = "E";
		}
	}

}
