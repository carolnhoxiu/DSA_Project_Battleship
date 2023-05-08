package battleship;

public class Report {
	private Position p;
	private boolean shoot;
	private boolean sunk;
	
	public Report(){
	}
	
	public Report(Position p, boolean shoot, boolean sunk) {
		this.p = p;
		this.shoot = shoot;
		this.sunk = sunk;
	}
	public Position getP() {
		return p;
	}
	public void setP(Position p) {
		this.p = p;
	}
	public boolean isShooted() {
		return shoot;
	}
	public void setShoot(boolean colpita) {
		this.shoot = colpita;
	}
	public boolean issunk() {
		return sunk;
	}
	public void setSunk(boolean sunk) {
		this.sunk = sunk;
	}	
	public String toString(){
		return "coordinate:"+p+" shoot:"+shoot+" sunk:"+sunk;
	}
}
