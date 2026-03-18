package chesslayer;

import boardlayer.Board;
import boardlayer.Position;

public class King extends ChessPiece {
	private ChessMatch chessMatch;
	private boolean[][] aSquares = new boolean[getBoard().getColumns()][getBoard().getRows()];

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "K";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] pMov = new boolean[getBoard().getColumns()][getBoard().getRows()];
		// Loads all the squares being protected by the enemy piece, except for the
		// pawns(they have special treatment)
		// This if is to prevent a Stack overflow bug, figure out why, don't feel like
		// explaining it
		if (chessMatch.getCurrentPlayer() == this.getColor()) {
			scanForAttackedSquares();
		}
		// Directions: up, down, left, right and Diagonals: up-right, up-left,
		// down-right, down-left
		int[][] directions = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 }, { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

		for (int[] direction : directions) {

			int dCollumn = direction[0];
			int dRow = direction[1];
			int collumn = getPosition().getCollumn() + dCollumn;
			int row = getPosition().getRow() + dRow;

			// Go a single time towards the direction and sees if it finds a piece

			if (getBoard().positionExists(collumn, row) && !getBoard().isTherePiece(new Position(collumn, row))) {
				pMov[collumn][row] = true;

			}

			// Checks if it is an opponent, and if that piece is being protected by another
			// piece
			if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))) {
				if (!isThatPieceBeingProtected(collumn, row)) {
					pMov[collumn][row] = true;
				}
			}
			// This is because pawns are not included on the aSquares[][] because their
			// capture logic is weird, hence the special treatment
			if (this.isAPawnGuardingThisSquare(collumn, row)) {
				pMov[collumn][row] = false;
			}
		}
		// castle logic

		if (this.getMoveCount() == 0 && this.conditionLongCastle()) {
			pMov[2][this.position.getRow()] = true;
			chessMatch.setCastleAvaliable(true);

		}

		if (this.getMoveCount() == 0 && this.conditionShortCastle()) {
			pMov[6][this.position.getRow()] = true;
			chessMatch.setCastleAvaliable(true);

		}

		pMov = filterLegalMoves(pMov);
		return pMov;
	}

	private boolean isAPawnGuardingThisSquare(int column, int row) {
		if (this.getColor() == Color.WHITE) {

			if ((getBoard().positionExists(column + 1, row + 1) && getBoard().piece(column + 1, row + 1) != null
					&& getBoard().piece(column + 1, row + 1).toString().equals("P")
					&& getBoard().piece(column + 1, row + 1).getColor() == Color.BLACK)
					|| (getBoard().positionExists(column - 1, row + 1)
							&& (getBoard().piece(column - 1, row + 1) != null
									&& getBoard().piece(column - 1, row + 1).toString().equals("P"))
							&& getBoard().piece(column - 1, row + 1).getColor() == Color.BLACK)) {
				return true;
			}
		} else if (this.getColor() == Color.BLACK) {
			if ((getBoard().positionExists(column + 1, row - 1) && getBoard().piece(column + 1, row - 1) != null
					&& getBoard().piece(column + 1, row - 1).toString().equals("P")
					&& getBoard().piece(column + 1, row - 1).getColor() == Color.WHITE)
					|| (getBoard().positionExists(column - 1, row - 1) && getBoard().piece(column - 1, row - 1) != null
							&& getBoard().piece(column - 1, row - 1).toString().equals("P"))
							&& getBoard().piece(column - 1, row - 1).getColor() == Color.WHITE) {
				return true;
			}
		}
		return false;

	}

	private boolean isThatPieceBeingProtected(int collumn, int row) {
		
		ChessPiece p = (ChessPiece) getBoard().piece(collumn, row);
		return p.amIBeingProtected(p);
		
	}

	private boolean conditionLongCastle() {
		ChessPiece longRook = (ChessPiece) getBoard().piece(0, this.position.getRow());
		return longRook != null && longRook.getMoveCount() == 0 && getBoard().piece(1, this.position.getRow()) == null
				&& getBoard().piece(2, this.position.getRow()) == null
				&& getBoard().piece(3, this.position.getRow()) == null;
	}

	private boolean conditionShortCastle() {
		ChessPiece shortRook = (ChessPiece) getBoard().piece(7, this.position.getRow());
		return shortRook != null && shortRook.getMoveCount() == 0 && getBoard().piece(5, this.position.getRow()) == null
				&& getBoard().piece(6, this.position.getRow()) == null;

	}

	// Add all the attacked squares of the scanned piece to aSquares
	private void mergeAttackedSquares(boolean[][] opponentPMov) {

		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (opponentPMov[i][j]) {
					this.aSquares[i][j] = true;

				}

			}
		}
	}

	private void scanForAttackedSquares() {
		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (getBoard().piece(i, j) != null && getBoard().piece(i, j).getColor() != this.getColor()
						&& getBoard().piece(i, j).toString() != "K" && getBoard().piece(i, j).toString() != "P") {
					mergeAttackedSquares(getBoard().piece(i, j).possibleMoves());

				}
			}
		}
	}

	// Return only the moves that are both possible and legal
	public boolean[][] filterLegalMoves(boolean[][] pMov) {
		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (this.aSquares[i][j]) {
					pMov[i][j] = false;
				}

			}
		}
		return pMov;
	}

	// Returns the squares that when blocked will protect the king from check,
	// ̶b̶u̶t̶ ̶i̶t̶s̶ ̶n̶o̶t̶ ̶w̶o̶r̶k̶i̶n̶g̶ ̶b̶e̶c̶a̶u̶s̶e̶ ̶p̶a̶w̶n̶s̶ ̶a̶r̶e̶ w̶e̶i̶r̶d̶  now its working
	
	private boolean[][] whereIsTheAttackCommingFrom() {

		boolean[][] attackRoute = new boolean[getBoard().getColumns()][getBoard().getRows()];

		// Directions: up, down, left, right and Diagonals: up-right, up-left,
		// down-right, down-left
		int[][] directions = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 }, { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

		for (int[] direction : directions) {

			int dCollumn = direction[0];
			int dRow = direction[1];
			int collumn = getPosition().getCollumn() + dCollumn;
			int row = getPosition().getRow() + dRow;

			// Searching for a piece on a direction
			while (getBoard().positionExists(collumn, row) && !getBoard().isTherePiece(new Position(collumn, row))) {
				collumn += dCollumn;
				row += dRow;
			}
			// Sees if the piece is from the opponent

			if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))) {
				if (canMoveInThisDirection(dCollumn * dRow, getBoard().piece(new Position(collumn, row)).toString())) {
					attackRoute[collumn][row] = true;
					dCollumn *= -1;
					dRow *= -1;
					collumn += dCollumn;
					row += dRow;
					while (getBoard().positionExists(collumn, row)
							&& !getBoard().isTherePiece(new Position(collumn, row))) {
						attackRoute[collumn][row] = true;
						collumn += dCollumn;
						row += dRow;
					}

				}
			}
		}
		//Those damned pawns 
		if (this.getColor().equals(Color.WHITE)) {
			int[][] bPawnAttackDirections = { { -1, 1 }, { 1, 1 } };
			for (int[] direction : directions) {

				int dCollumn = direction[0];
				int dRow = direction[1];
				int collumn = getPosition().getCollumn() + dCollumn;
				int row = getPosition().getRow() + dRow;

				if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))
						&& getBoard().piece(collumn, row).toString().equals("P")) {
					attackRoute[collumn][row] = true;
				}

			}
		}
		if (this.getColor().equals(Color.BLACK)) {
			int[][] wPawnAttackDirections = { { -1, -1 }, { 1, -1 } };
			for (int[] direction : directions) {

				int dCollumn = direction[0];
				int dRow = direction[1];
				int collumn = getPosition().getCollumn() + dCollumn;
				int row = getPosition().getRow() + dRow;

				if (getBoard().positionExists(collumn, row) && isThereOpponentPiece(new Position(collumn, row))
						&& getBoard().piece(collumn, row).toString().equals("P")) {
					attackRoute[collumn][row] = true;
				}

			}
		}
		
		
		int[][] horseDirections = { { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { 2, 1 }, { 2, -1 }, { -2, -1 },
				{ -2, 1 } };

		for (int[] direction : horseDirections) {

			int dCollumn = direction[0];
			int dRow = direction[1];
			int collumn = getPosition().getCollumn() + dCollumn;
			int row = getPosition().getRow() + dRow;

			if (getBoard().positionExists(collumn, row)
					&& (isThereOpponentPiece(new Position(collumn, row)) || !getBoard().isTherePiece(collumn, row))
					&& getBoard().piece(collumn, row) != null
					&& getBoard().piece(collumn, row).toString().equals("H")) {
				attackRoute[collumn][row] = true;
			}
		}

		return attackRoute;

	}

	// I think the next 2 function are self explanatory, so i won't even comment on
	// what they do
	public boolean canIBeHelped(ChessPiece p) {
		boolean[][] canBeDefended = p.possibleMoves();
		boolean[][] attackRoute = this.whereIsTheAttackCommingFrom();
		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (attackRoute[i][j] && canBeDefended[i][j]) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean[][] helpMe(boolean[][] pMov) {

		boolean[][] attackRoute = this.whereIsTheAttackCommingFrom();
		for (int i = 0; i < getBoard().getColumns(); i++) {
			for (int j = 0; j < getBoard().getRows(); j++) {
				if (pMov[i][j] && attackRoute[i][j]) {
					pMov[i][j] = true;
				} else {
					pMov[i][j] = false;
				}
			}
		}

		return pMov;

	}

}
