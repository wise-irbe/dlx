package com.radu.dlx;

import com.radu.dlx.algo.DancingLinks;
import com.radu.dlx.io.tree.OfIntSolutionTree;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.problem.ExactCoveringProblem;
import com.radu.dlx.problem.sudoku.SudokuBoardReader;
import com.radu.dlx.problem.sudoku.SudokuBoardWriter;
import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrays;

//TODO: Build a more sophisticated command line app
//TODO: Build a command line app for generating exact cover problem descriptions
public class SudokuCommand {
    private static final String HELP_MESSAGE = "Sudoku solver:\n" +
            "Use standard input to send the contents of the interesting file into the jar file.\n" +
            "File should contain a sequence of symbols where 0 marks an empty cell and a number from 1-9 a filled cell.\n" +
            "Spacing and all other symbols will be ignored.\n" +
            "java -jar sudoku.jar < <sudoku-file>\n" +
            "or " +
            "cat <sudoku-file> | java -jar sudoku.jar\n" +
            " where <sudoku-file> is the file containing the Sudoku problem";

    public static void main(String... args) {
        if (args != null && args.length > 0) {
            throw new IllegalArgumentException(HELP_MESSAGE);
        }
        try {
            System.out.println(new SudokuCommand().solveStackless());
        } catch (Exception e) {
            System.out.println(HELP_MESSAGE);
            throw e;
        }
    }

    public String solveStackless() {
        ExactCoveringProblem problem = SudokuBoardReader.create().read(System.in);
        DancingArray array = DancingArrays.create(problem);
        SolutionTree solutions = DancingLinks.create(OfIntSolutionTree.createStoring(array, 1), array).solve();
        return SudokuBoardWriter.create().print(problem, solutions.getFirstSolution(), array);
    }
}
