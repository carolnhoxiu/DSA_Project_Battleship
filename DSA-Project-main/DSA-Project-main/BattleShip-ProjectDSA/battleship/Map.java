package battleship;

import java.util.LinkedList;
import java.util.Random;

import battleship.ui.Effects;

public class Map {
	public static final int DIM_MAP = 10;
	private final char Null = '0', SHIP = 'X', WATER = 'A', SHOOT = 'C';
	private char[][] map;
	private LinkedList<Ship> listShip;
	
	Effects effects = new Effects();

	public Map() {
		listShip = new LinkedList<Ship>();
		map = new char[DIM_MAP][DIM_MAP];
		for (int i = 0; i < DIM_MAP; i++)
			for (int j = 0; j < DIM_MAP; j++)
				map[i][j] = Null;
	}

	public void fillMapRandom() {
		clear();
		Random r = new Random();
		insertShipRandom(r, 4);
		insertShipRandom(r, 3);
		insertShipRandom(r, 3);
		insertShipRandom(r, 2);
		insertShipRandom(r, 2);
		insertShipRandom(r, 2);
		insertShipRandom(r, 1);
		insertShipRandom(r, 1);
		insertShipRandom(r, 1);
		insertShipRandom(r, 1);
	}

	private void clear() {
		for (int i = 0; i < DIM_MAP; i++)
			for (int j = 0; j < DIM_MAP; j++)
				map[i][j] = Null;

	}

	public boolean insertShip(int x, int y, int dim, int dir) {
		if (dir == 1 && x + dim > DIM_MAP) {
			return false;
		} 
		if (dir == 0 && y + dim > DIM_MAP) {
			return false;
		} 
		boolean insert;

		if (dir == 0)
			insert = verificationHorizontal(x, y, dim);
		else
			insert = verificationVertical(x, y, dim);

		if (!insert)
			return false;
		if (dir == 0) {
			Ship n = new Ship(x, y, x, y + dim - 1);
			listShip.add(n);
		} else {
			Ship n = new Ship(x, y, x + dim - 1, y);
			listShip.add(n);
		}
		for (int i = 0; i < dim; i++) {
			if (dir == 0) {
				map[x][y + i] = SHIP;
			} else
				map[x + i][y] = SHIP;
		}
		return true;
	}

	public int[] insertShipRandom(Random random, int dimensione) {
		boolean insert;
		int[] data = new int[4];
		int direction, line, column;
		do {
			insert = true;
			direction = random.nextInt(2);
			if (direction == 0) {
				column = random.nextInt(DIM_MAP - dimensione + 1);
				line = random.nextInt(DIM_MAP);
			} else {
				column = random.nextInt(DIM_MAP);
				line = random.nextInt(DIM_MAP - dimensione + 1);
			}
			if (direction == 0)
				insert = verificationHorizontal(line, column, dimensione);
			else
				insert = verificationVertical(line, column, dimensione);
		} while (!insert);
		if (direction == 0) {
			Ship n = new Ship(line, column, line, column + dimensione - 1);
			listShip.add(n);
		} else {
			Ship n = new Ship(line, column, line + dimensione - 1, column);
			listShip.add(n);
		}
		for (int i = 0; i < dimensione; i++) {
			if (direction == 0) {
				map[line][column + i] = SHIP;
			} else
				map[line + i][column] = SHIP;
		}
		data[0] = line;
		data[1] = column;
		data[2] = dimensione;
		data[3] = direction;
		return data;
	}

	public boolean verificationVertical(int line, int column, int dimensione) {
		if (line != 0) 
			if (map[line - 1][column] == SHIP) 
				return false;
		if (line != DIM_MAP - dimensione)
			if (map[line + dimensione][column] == SHIP)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (column != 0)
				if (map[line + i][column - 1] == SHIP)
					return false;
			if (column != DIM_MAP - 1)
				if (map[line + i][column + 1] == SHIP)
					return false;
			if (map[line + i][column] == SHIP)
				return false;
		}
		return true;
	}

	public boolean verificationHorizontal(int line, int column, int dimensione) {
		if (column != 0)
			if (map[line][column - 1] == SHIP)
				return false;
		if (column != DIM_MAP - dimensione)
			if (map[line][column + dimensione] == SHIP)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (line != 0)
				if (map[line - 1][column + i] == SHIP)
					return false;
			if (line != DIM_MAP - 1)
				if (map[line + 1][column + i] == SHIP)
					return false;
			if (map[line][column + i] == SHIP)
				return false;
		}
		return true;
	}

	public boolean shoot(Position p) {
		int line = p.getCoordX();
		int column = p.getCoordY();
		if (map[line][column] == SHIP) {
			map[line][column] = SHOOT;
			effects.strike();
			return true;
		}
		else effects.splash();
		map[line][column] = WATER;
		return false;
	}

	public Ship sunk(Position p) {
		int line = p.getCoordX();
		int col = p.getCoordY();
		Ship ship = null;
		for (int i = 0; i < listShip.size(); i++) {
			if (listShip.get(i).uguale(line, col)) {
				ship = listShip.get(i);
				break;
			}
		}
		for (int i = ship.getXin(); i <= ship.getXfin(); i++) {
			for (int j = ship.getYin(); j <= ship.getYfin(); j++) {
				if (map[i][j] != SHOOT) {
					return null;
				}
			}
		}
		listShip.remove(ship);
		effects.sinking();
		return ship;
	}

	public void setWater(Position p) {
		map[p.getCoordX()][p.getCoordY()] = WATER;
	}

	public boolean water(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == WATER;
	}

	public boolean hit(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == SHOOT;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DIM_MAP; i++) {
			for (int j = 0; j < DIM_MAP; j++) {
				sb.append(map[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	
	public void setAdvShips(LinkedList<int[]> advShips) {
		listShip.clear();
		for (int[] a : advShips) {
			insertShip(a[0], a[1], a[2], a[3]);
			System.out.println("sto inserendo" + a[0] + a[1] + a[2] + a[3]);
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(map[i][j]);
			System.out.println("");
		}
	}
}
