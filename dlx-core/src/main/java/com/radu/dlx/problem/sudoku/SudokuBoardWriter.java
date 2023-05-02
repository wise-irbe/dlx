package com.radu.dlx.problem.sudoku;

import com.radu.dlx.problem.ExactCoveringProblem;
import com.radu.dlx.struct.DancingStructure;

import static com.radu.dlx.problem.sudoku.SudokuBoardBuilder.BOARD_SIDE;

/**
 * Class to print out the sudoku solution.
 * <p>
 * If for a exact cover value, more there are mora than one constraints, it is printed as 0.
 * <p>
 * When we start using the solver for other exact cover problems, the board size has to be parameterized
 */
public final class SudokuBoardWriter {
    private static final int UNDEFINABLE_CELL_VALUE = -1;
    private static final int CARRIAGE_RETURN_COUNT = 9;

    private SudokuBoardWriter() {
    }

    public static SudokuBoardWriter create() {
        return new SudokuBoardWriter();
    }


    public <V extends DancingStructure> String print(ExactCoveringProblem problem, int[] solution, V structure) {
        int[][] board = new int[BOARD_SIDE][BOARD_SIDE];

        setProblem(board, problem);
        setSolution(board, solution, structure);

        return printBoard(board);
    }

    public String print(ExactCoveringProblem problem) {
        int[][] board = new int[BOARD_SIDE][BOARD_SIDE];

        setProblem(board, problem);

        return printBoard(board);
    }

    private void setProblem(int[][] board, ExactCoveringProblem problem) {
        for (String[] item : problem.knownOptions) {
            int row = idx(item[1], 1);
            int col = idx(item[2], 1);
            int val = idx(item[1], 2);
            int rowIdx = row - 1;
            int colIdx = col - 1;
            if (board[rowIdx][colIdx] == 0) {
                board[rowIdx][colIdx] = val;
            } else {
                board[rowIdx][colIdx] = UNDEFINABLE_CELL_VALUE;
            }
        }
        cleanBoard(board);
    }

    private void cleanBoard(int[][] board) {
        for (int i = 0; i < BOARD_SIDE; i++) {
            for (int j = 0; j < BOARD_SIDE; j++) {
                int val = board[i][j];
                if (val == UNDEFINABLE_CELL_VALUE) {
                    board[i][j] = 0;
                }
            }
        }
    }


    private <V extends DancingStructure>
    void setSolution(int[][] board, int[] solution, V structure) {

        for (Integer firstOption : solution) {
            int row = UNDEFINABLE_CELL_VALUE;
            int col = UNDEFINABLE_CELL_VALUE;
            int val = UNDEFINABLE_CELL_VALUE;

            for (String option : structure.getItemList(firstOption)) {
                char prefix = option.charAt(0);
                int val1 = idx(option, 1) - 1;
                int val2 = idx(option, 2) - 1;

                if (prefix == 'p') {
                    row = val1;
                    col = val2;
                }
                if (prefix == 'r') {
                    val = val2 + 1;
                }
            }
            if (board[row][col] == 0) {
                board[row][col] = val;
            } else {
                board[row][col] = UNDEFINABLE_CELL_VALUE;
            }
        }
        cleanBoard(board);
    }

    private String printBoard(int[][] board) {
        StringBuilder result = new StringBuilder(BOARD_SIDE * BOARD_SIDE + CARRIAGE_RETURN_COUNT);

        for (int row = 0; row < BOARD_SIDE; row++) {
            for (int col = 0; col < BOARD_SIDE; col++) {
                result.append(board[row][col]);
            }

            if (row < BOARD_SIDE - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }


    private int idx(String item, int pos) {
        return item.charAt(pos) - SudokuBoardReader.NUM_ZERO_CHAR_CODE;
    }

}
