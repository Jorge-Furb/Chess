package boardlayer;

import chesslayer.ChessPosition;
import chesslayer.Color;

public abstract class Piece {
	
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		
	}
	
	
	protected Board getBoard() {
		return board;
	}
	
	
	
	public Position getPosition() {
		return position;
	}
	
	public abstract boolean[][] possibleMoves();
	
	// I don't think i ever used this, i rather load all the possible moves of a 
	//piece once on a boolean[][] if i need to use it on a function, but hey... its there i guess
	
	public boolean possibleMove(Position position) {
		
		return possibleMoves()[position.getCollumn()][position.getRow()];
		
	}
	public boolean isThereAnyPossibleMoves() {
		
		boolean[][]pMoves = possibleMoves();
		pMoves = filterLegalMoves(pMoves);
		
		for (int i=0;i<board.getColumns();i++) {
			for(int j=0;j<board.getRows();j++) {
				if(pMoves[i][j]==true) {
					
					return true;
				}
				
			}
		}
		return false;
		
	}

	public abstract Color getColor();


	public abstract boolean[][] filterLegalMoves(boolean[][] pMov);
	
}
