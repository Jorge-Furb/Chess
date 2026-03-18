package chesslayer;

import java.util.Arrays;

import boardlayer.Board;
import boardlayer.Position;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "H";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] pMov = new boolean[getBoard().getColumns()][getBoard().getRows()];

		// Directions: 2up1right,
		// 2down1right,2up1left,2down1left,1up2right,1down2right,1down2left,1up2left
		int[][] directions = { { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { 2, 1 }, { 2, -1 }, { -2, -1 }, { -2, 1 } };

		for (int[] direction : directions) {

			int dCollumn = direction[0];
			int dRow = direction[1];
			int collumn = getPosition().getCollumn() + dCollumn;
			int row = getPosition().getRow() + dRow;

			if (getBoard().positionExists(collumn, row)
					&& (isThereOpponentPiece(new Position(collumn, row)) || !getBoard().isTherePiece(collumn, row))) {
				pMov[collumn][row] = true;
			}
		}
		return pMov;
	}
	// This function finds the intersection between two boolean[][],here its used to exclude the illegal moves from the possible moves set
	public boolean[][] filterLegalMoves(boolean[][] pMov) {
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
