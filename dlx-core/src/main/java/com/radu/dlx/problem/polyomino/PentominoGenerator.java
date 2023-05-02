package com.radu.dlx.problem.polyomino;

import com.radu.dlx.Solver;
import com.radu.dlx.problem.Board;

//TODO: Add problem descriptions to the log
public final class PentominoGenerator {

    public static GenericProblemBuilder.SolverBuilder build(Solver.ProblemAlgoBuilder problem) {
        PolyominoBoard board = new PolyominoBoard();
        GenericProblemBuilder.SolverBuilder builder = GenericProblemBuilder.builder(problem);
        for (Polyomino poly : board) {
            builder.addOptionRow(poly.code());
        }
        return builder;
    }

    public static Board<Polyomino> createPentominoBoard() {
        return new PolyominoBoard();
    }


    private PentominoGenerator() {

    }


}
