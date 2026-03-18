package application;

import boardlayer.Board;
import chesslayer.ChessPiece;
import chesslayer.Color;



public class UI {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	
	// Peças vão de 0 a 7 para Colunas I e Linhas J, ao printar A 8, vamos ter linha j=7 e coluna i=0 com chessBoardPosition[0][7] = torre preta
	
	public static void cleanScrean() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
	
	public static void printBoard(ChessPiece[][] chessBoardPosition, boolean[][] greenTiles) {
		
		for(int j=Board.getRows() -1; j>=0;j--) {
			System.out.println();
			System.out.print(j+1+"|");
			for(int i=0;i<Board.getColumns();i++) {
				if(chessBoardPosition[i][j]!=null) {
					printPiece(chessBoardPosition[i][j],greenTiles[i][j]);
				}
				else {
					if(greenTiles[i][j]) {
						System.out.print(ANSI_GREEN+" □"+ANSI_RESET);
					}
					else {
						System.out.print(" □");
					}
				}
			}
		}
		System.out.print("\n |_________________");
		System.out.println("\n   A B C D E F G H");
	}
	
	private static void printPiece(ChessPiece piece, boolean greenTile) {
		
		if(greenTile) {
			System.out.print(ANSI_GREEN+" "+piece.toString()+ANSI_RESET);
		}
		
		else if ( piece.getColor() ==Color.WHITE) {
			System.out.print(" "+piece.toString());
		}
		
		else if( piece.getColor() == Color.BLACK) {
			System.out.print(ANSI_YELLOW+" "+piece.toString()+ANSI_RESET);
		}
		
	}
	private static String convertUnicode(String s) {
		
		switch(s) {
		case"P":
			
			s="♟︎";
			break;
			
		case "R":
			
			s="♜";
			break;
			
		case "H":
			
			s="♞";
			break;
			
		case"Q":
			s="♛";
			break;
		case"B":
			s= "♝";
			break;
		case"K":
			s = "♚";
			break;
			
		default:
			break;
		}
		return s;
	}
	
}
