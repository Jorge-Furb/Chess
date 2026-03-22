package application;

import chesslayer.ChessMatch;
import chesslayer.ChessMove;
import chesslayer.ChessPosition;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		ChessBoardWindow cb = new ChessBoardWindow();
		boolean greenTiles[][] = new boolean[chessMatch.getBoard().getColumns()][chessMatch.getBoard().getRows()];
		UI.printBoard(chessMatch.getPieces(), greenTiles);
		boolean[][] emptyTiles = new boolean [chessMatch.getBoard().getColumns()][chessMatch.getBoard().getRows()];
		System.out.println("Do you want to have the possible movable pieces highlighted?y/n");
		char optionHighlitedPieces = sc.next().charAt(0);
		System.out.println("Do you want to play with the GUI mode? y for Yes or else for terminal UI version");
		char guiOption = sc.next().charAt(0);
		char optionInputType;
		if(guiOption!='y'){
			System.out.println("Do you want the short or long notation?(s/any other key) short : pe4, long: e2<Enter>e4");
			optionInputType = sc.next().charAt(0);
		if(optionInputType=='s') {
				System.out.println("You've choose the short notation");
			}
			else {
				System.out.println("Default long notation");
			}
		}
		else{
			optionInputType = 'w';
			cb.buildWindow();
			cb.scanBoardPieces(chessMatch.getPieces());
		}
			
		sc.nextLine();
		while (!chessMatch.isCheckMate()) {
			UI.cleanScreen();
			chessMatch.isCheck();
			greenTiles = chessMatch.movablePieces();
			if(chessMatch.isCheckMate()){
				continue;
			}
			if (optionHighlitedPieces == 'y') {
				UI.printBoard(chessMatch.getPieces(), greenTiles);
			}
			else {
				UI.printBoard(chessMatch.getPieces(), emptyTiles);
			}
			switch(optionInputType) {
			//This is the list of missing features:
			//Clock
			//List containing the log of the chess match ( i'm thinking of position and clock time at the move)
			//UI display the captured pieces
			//Improve UI in general, clear the screen every time i have to print the board or a new message
			// Try to print Unicode chess Piece, but i can't get them to line up
			
			
			
			// Codigo para notação algebrica curta ( ex: pe4 ) Working
			//-----------------------------------------------------
			case 's':
			System.out.println(chessMatch.getCurrentPlayer() + " to move:");
			String chessMoveScan = sc.nextLine();
			while(!checkShortInput(chessMoveScan)) {
				System.out.println("Invalid input");
				chessMoveScan = sc.nextLine();
			}
			ChessMove chessMove = new ChessMove (chessMoveScan,chessMatch);
			ChessPosition[] move = chessMove.extractChessPosition();
			
			if (move!=null) {
				ChessPosition sourcePosition = move[0];		
				ChessPosition targetPosition = move[1];
				chessMatch.performChessMove(sourcePosition, targetPosition);
			}
			//Arrumar mensagem de invalid chess move, nao esta 
			else {
				System.out.println("Invaid chess move");
			}
			break;
			//----------------------------------------------------------
			//  Codigo do GUI
			case 'w':
					
					try {
						cb.setGreenTiles(greenTiles);
						String chessSourcePositionMouseClick = cb.waitForClick();
						char collum = chessSourcePositionMouseClick.charAt(0);
						int row = Integer.parseInt(chessSourcePositionMouseClick.substring(1));
						ChessPosition sourcePosition = new ChessPosition(collum,row);
						greenTiles = chessMatch.matchPossibleMoves(sourcePosition);
						cb.setGreenTiles(greenTiles);
						String chessDestinyPositionMouseClick = cb.waitForClick();
						collum = chessDestinyPositionMouseClick.charAt(0);
						row = Integer.parseInt(chessDestinyPositionMouseClick.substring(1));
						ChessPosition targetPosition = new ChessPosition(collum,row);
						chessMatch.performChessMove(sourcePosition,targetPosition);

						
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			break;
			
			// Codigo notaçao algebrica longa (ex: enter e2 then enter e4) Working
			//------------------------------------------------
			default:
			System.out.println(chessMatch.getCurrentPlayer() + " to move:");
			ChessPosition sourcePosition = checkInput(sc);

			while (sourcePosition==null||positionCondition(chessMatch,sourcePosition,greenTiles)){
				System.out.println("Enter a valid position");
				sourcePosition = checkInput(sc);
			}
			greenTiles = chessMatch.matchPossibleMoves(sourcePosition);
			UI.printBoard(chessMatch.getPieces(), greenTiles);
			System.out.println("to:");
			ChessPosition targetPosition = checkInput(sc);
			while (targetPosition==null||positionCondition(chessMatch,targetPosition,greenTiles)) {
				System.out.println("Enter a valid position");
				targetPosition =checkInput(sc);
			}
			chessMatch.performChessMove(sourcePosition, targetPosition);
			
			break;
			//-----------------------------------------------------------
			

		}
		}
		System.out.println("Check Mate!!, the "+chessMatch.getCurrentPlayer()+" pieces lost the game");
		
		sc.close();
	}
	private static boolean positionCondition(ChessMatch chessMatch, ChessPosition position, boolean[][]greenTiles) {
		return !chessMatch.getBoard().positionExists(position.toPosition())
				||!greenTiles[position.toPosition().getCollumn()][position.toPosition().getRow()];
	
	}
	private static boolean checkShortInput(String input) {
		
		if(input.length()<3) {
			return false;
		}
		Set<String> checkPieceName = Set.of("q","Q","K","k","P","p","B","b","H","h","R","r");
		Set<String> checkColumnName = Set.of("a","b","c","d","e","f","g","h");
		Set<String> checkRowName = Set.of("1","2","3","4","5","6","7","8");
		boolean checkedPieceName=false;
		boolean checkedColumnName=false;
		boolean checkedRowName=false;
		
		
		for(String p : checkPieceName) {
			String inputAt0=input.substring(0, 1);
			if( inputAt0.equals(p))
				checkedPieceName=true;
		}
		for(String p : checkColumnName) {
			String inputAt1=input.substring(1, 2);
			if( inputAt1.equals(p)) {
				checkedColumnName=true;
			}
		}
		for(String p : checkRowName) {
			String inputAt2=input.substring(2, 3);
			if(inputAt2.equals(p)) {
				checkedRowName=true;
			}
		}
		return checkedPieceName&&checkedColumnName&&checkedRowName&&input.length()==3;
		
		
	}
	private static ChessPosition checkInput(Scanner sc) {

		try {
			Set<Character> checkColumn = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
			Set<Integer> checkRow = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
			String position = sc.nextLine();
			while(position.length()!=2) {
				System.out.println("Enter a valid position format (A letter and a number no space)");
				position = sc.nextLine();
			}
			
			char column = position.charAt(0);
			int row = Integer.parseInt(position.substring(1));
			
			
			checkColumn.contains(column);
			checkRow.contains(row);
			if(checkColumn.contains(column)||checkRow.contains(row)) {
				return new ChessPosition(column,row);
			}
			else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}

