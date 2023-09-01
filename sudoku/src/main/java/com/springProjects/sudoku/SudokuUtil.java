package com.springProjects.sudoku;


import jakarta.servlet.http.HttpServletRequest;


public class SudokuUtil {
	public Integer[][] grid = new Integer[9][9];

	public SudokuUtil(HttpServletRequest req) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				String cellId = String.valueOf(i * 9 + j);
				if (req.getParameter(cellId).equals("")) {
					grid[i][j] = 0;
				} else {
					grid[i][j] = Integer.valueOf(req.getParameter(cellId));
				}
			}
		}
	}
	
	public boolean isFilled() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	public boolean isValid() {
		for (int i = 0; i < 9; i++) {
			if (!isValidRow(i) || !isValidCol(i) || !isValidSquare(i))
				return false;
		}
		return true;
	}

	public boolean isValidRow(int row) {
		int[] seen = new int[10];
		for (int i = 0; i < 9; i++) {
			if (grid[row][i] != 0) {
				seen[grid[row][i]]++;
			}
			if (seen[grid[row][i]] > 1) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidCol(int col) {
		int[] seen = new int[10];
		for (int i = 0; i < 9; i++) {
			if (grid[i][col] != 0) {
				seen[grid[i][col]]++;
			}
			if (seen[grid[i][col]] > 1) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidSquare(int box) {
		int startRow = 3 * (box / 3);
		int startCol = 3 * 	(box % 3);

		int[] seen = new int[10];
		for (int i = startRow; i < startRow + 3; i++) {
			for (int j = startCol; j < startCol + 3; j++) {
				if (grid[i][j] != 0) {
					seen[grid[i][j]]++;
				}
				if (seen[grid[i][j]] > 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String res = "";
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				res += grid[i][j];
			}
		}
		return res;
	}
}
