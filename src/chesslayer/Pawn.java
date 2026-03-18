package chesslayer;

import java.util.Arrays;

import boardlayer.Board;
import boardlayer.Position;

public class Pawn extends ChessPiece {
	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color,ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch=chessMatch;
	}

	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
        boolean[][] pMov = new boolean[getBoard().getColumns()][getBoard().getRows()];
        
        switch(this.getColor()) {
        	
        case WHITE: 
        	
        	if(getBoard().piece(position.getCollumn(), position.getRow()+1)==null) {
        	pMov[position.getCollumn()][position.getRow()+1]=true;
        	if(this.getMoveCount()==0&&getBoard().piece(position.getCollumn(), position.getRow()+2)==null) {
        		pMov[position.getCollumn()][position.getRow()+2]=true;
        	}
        }
        	int[][]directionsWhite = {{-1,1},{1,1}};
        	for(int[] direction :directionsWhite) {
        		int dCollumn=direction[0];
        		int dRow=direction[1];
        		int collumn=position.getCollumn()+dCollumn;
        		int row=position.getRow()+dRow;
        		
        		 if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))) {
                     pMov[collumn][row] = true;
                 }
        	}
        	//Consider implementing equal() in class Position

        	if(this.chessMatch.getEnPassantVunerable()!=null) {
           	 
           	int toTheRight=position.getCollumn()+1;
           	int toTheLeft=position.getCollumn()-1;
           	int sameRow=position.getRow();
           	
           	
           	if(this.chessMatch.getEnPassantVunerable().getPosition().getRow()==sameRow&&
           			this.chessMatch.getEnPassantVunerable().getPosition().getCollumn()==toTheLeft){
           		pMov[toTheLeft][sameRow+1]=true;
           	}
           	else if(this.chessMatch.getEnPassantVunerable().getPosition().getRow()==sameRow&&
           			this.chessMatch.getEnPassantVunerable().getPosition().getCollumn()==toTheRight){
           		pMov[toTheRight][sameRow+1]=true;
           	}
           
           }
        	break;
        	
        case BLACK:	
        	
        	if(getBoard().piece(position.getCollumn(), position.getRow()-1)==null) {
            	pMov[position.getCollumn()][position.getRow()-1]=true;
            	if(this.getMoveCount()==0&&getBoard().piece(position.getCollumn(), position.getRow()-2)==null) {
            		pMov[position.getCollumn()][position.getRow()-2]=true;
            }
        	}
            	int[][]directionsBlack = {{-1,-1},{1,-1}};
            	for(int[] direction :directionsBlack) {
            		int dCollumn=direction[0];
            		int dRow=direction[1];
            		int collumn=position.getCollumn()+dCollumn;
            		int row=position.getRow()+dRow;
            		
            		 if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))) {
                         pMov[collumn][row] = true;
                     }
            	}
            	//Consider implementing equal() in class Position
                if(this.chessMatch.getEnPassantVunerable()!=null) {
                	
                	int toTheRight=position.getCollumn()+1;
                	int toTheLeft=position.getCollumn()-1;
                	int sameRow=position.getRow();
                	
                	
                	if(this.chessMatch.getEnPassantVunerable().getPosition().getRow()==sameRow&&
                			this.chessMatch.getEnPassantVunerable().getPosition().getCollumn()==toTheLeft){
                		pMov[toTheLeft][sameRow-1]=true;
                	}
                	else if(this.chessMatch.getEnPassantVunerable().getPosition().getRow()==sameRow&&
                			this.chessMatch.getEnPassantVunerable().getPosition().getCollumn()==toTheRight){
                		pMov[toTheRight][sameRow-1]=true;
                	}
                
                }
            	break;
        	
        	
        }
        return pMov;
	}
	// This function finds the intersection between two boolean[][],here its used to exclude the illegal moves from the possible moves set
	public boolean[][] filterLegalMoves(boolean[][] pMov){
		boolean[][] onlyLegalMoves = this.checkLegalMoves();

		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (pMov[i][j] && onlyLegalMoves[i][j]) {
					pMov[i][j] = true;
				} else {
					pMov[i][j] = false;
				}

			}
		}
		return pMov;
	}
}