package chesslayer;

import java.util.Arrays;

import boardlayer.Board;
import boardlayer.Position;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] pMov = new boolean[getBoard().getColumns()][getBoard().getRows()];

		// Directions: up, down, left, right and Diagonals: up-right, up-left,
		// down-right, down-left
		int[][] directions = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 }, { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

		for (int[] direction : directions) {

			int dCollumn = direction[0];
			int dRow = direction[1];
			int collumn = getPosition().getCollumn() + dCollumn;
			int row = getPosition().getRow() + dRow;

			// walks to a giving direction as long the position exists and there is no piece

			while (getBoard().positionExists(collumn, row) && !getBoard().isTherePiece(new Position(collumn, row))) {
				pMov[collumn][row] = true;
				collumn += dCollumn;
				row += dRow;
			}

			// when stops walking, check if the next positions doesn't exists or if its a
			// piece, checks if its a opponent piece if the later is the case

			if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))) {
				pMov[collumn][row] = true;
			}
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