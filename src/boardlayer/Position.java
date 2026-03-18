package boardlayer;

import java.util.Objects;

public class Position {
	
	private int collumn;
	private int row;
	
	public Position(int collumn, int row) {
		this.collumn = collumn;
		this.row = row;
	}
	public int getCollumn() {
		return collumn;
	}
	
	public int getRow() {
		return row;
	}
	public void setValues(int collumn,int row) {
		this.row = row;
		this.collumn=collumn;
	}
	@Override
	public String toString() {
		return "Position: " + collumn + ", " + row;
	}
	@Override
	public int hashCode() {
		return Objects.hash(collumn, row);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return collumn == other.collumn && row == other.row;
	}
	
	
	
	
}
