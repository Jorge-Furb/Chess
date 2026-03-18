package chesslayer;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ChessMove {
	
	private ChessPosition[] chessPosition = new ChessPosition[2];
	private String chessMove;
	private ChessMatch chessMatch;
	
	
	public ChessMove(String chessMove,ChessMatch chessMatch) {
		this.chessMove=chessMove;
		this.chessMatch=chessMatch;
	}
	
	private boolean pieceCanMoveThere(ChessPosition matchPosition, ChessPosition chessPosition) {
		
		boolean[][] possibleMoves = chessMatch.matchPossibleMoves(matchPosition);
	
		for(int i=0;i<chessMatch.getBoard().getColumns();i++) {
			for(int j =0 ; j<chessMatch.getBoard().getRows();j++) {
				
				if(possibleMoves[i][j]&&chessPosition.toPosition().getCollumn()==i&&chessPosition.toPosition().getRow()==j) {
				return	true;
				}
			}
		}
		return false;
		
	}
	
	
	public ChessPosition[] extractChessPosition() {
		
		ChessPiece[][]matchPosition = chessMatch.getPieces();
		
		//Convert string to a String with the piece name and a Chess Position i'm targeting
		//----------------------------------------
		String chessPieceName = this.chessMove.substring(0,1);
		chessPieceName = chessPieceName.toUpperCase();
		String targetPosition = this.chessMove.substring(1,3);
		char chessPositionColumn = targetPosition.charAt(0);
		int chessPositionRow = Integer.parseInt(targetPosition.substring(1));
		ChessPosition targetChessPosition=new ChessPosition(chessPositionColumn,chessPositionRow);

		//------------------------------------------------------------------
		ChessPosition sourceChessPosition = new ChessPosition();
		Set<ChessPosition> piecesThatCanMoveToTheTargetPosition = new HashSet<>();
		//Scans the board for occupied positions, looks for the piece to move, checks if the found piece can move...
		//... to the targeted position
		for(int i=0;i<chessMatch.getBoard().getColumns();i++) {
			for(int j=0 ;j<chessMatch.getBoard().getRows();j++) {
				
				
				if(matchPosition[i][j]!=null&&chessPieceName.equals(matchPosition[i][j].toString())&&matchPosition[i][j].getColor().equals(chessMatch.getCurrentPlayer())
						&& pieceCanMoveThere(this.convertToChessPosition(matchPosition[i][j]), targetChessPosition)) {
					sourceChessPosition = sourceChessPosition.fromPosition(i,j);			
					piecesThatCanMoveToTheTargetPosition.add(sourceChessPosition);
				}
				
			}
		}
		//calls special treatment if 2 pieces of the same type can move to target chess position
		if(piecesThatCanMoveToTheTargetPosition.size()==1) {
			chessPosition[0] = sourceChessPosition;
			chessPosition[1] = targetChessPosition;
			System.out.println("Moving :"+ chessPieceName+ chessPosition[0]+" to " + chessPosition[1]);
		}
		else if(piecesThatCanMoveToTheTargetPosition.size()>1) {
			chessPosition[0] = thereIsAnotherPossibleMove(piecesThatCanMoveToTheTargetPosition);
			chessPosition[1] = targetChessPosition;
			System.out.println("Moving :"+ chessPieceName+ chessPosition[0]+" to " + chessPosition[1]);
		}
		
		if(chessPosition[0]==null||chessPosition[1]==null) {
			return null;
		}
		
		else {
			return chessPosition;
		}
		
	}
	private ChessPosition convertToChessPosition(ChessPiece p) {
		ChessPosition chessPieceMatchPosition = new ChessPosition();
		chessPieceMatchPosition = chessPieceMatchPosition.fromPosition(p.getPosition());
		return chessPieceMatchPosition;
	}
	//Special treatment in case 2 pieces of the same type can o to the target square
	private ChessPosition thereIsAnotherPossibleMove(Set<ChessPosition> m) {
		Scanner sc2 = new Scanner(System.in);
		Set<String> pChoices = new HashSet<>();
		System.out.println("Whic one of those Pieces you wanna move:");
		for(ChessPosition s: m){
			pChoices.add(s.toString());
			System.out.print(s.toString()+" ");
		}
		String choice = sc2.nextLine();
		while(!validateChoice(choice,pChoices)) {
			System.out.println("Choose a valid position");
			choice = sc2.nextLine();
		}
		char column = choice.charAt(0);
		int row = Integer.parseInt(choice.substring(1));
		
		ChessPosition sourcePosition = new ChessPosition(column,row);
		
		return sourcePosition;
	}
	
	private boolean validateChoice(String choice, Set<String> pChoices) {
		int aux=0;
		//returning inside the for only works properly on the last item of the Set
		for(String s : pChoices) {
			
			if (s.equals(choice)) {
				aux++;
			}
		}
		return aux>0;
	}
	
}
