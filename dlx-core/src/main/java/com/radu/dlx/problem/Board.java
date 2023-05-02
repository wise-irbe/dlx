package com.radu.dlx.problem;

//TODO: Apply this to Sudoku board/NQueens problem as well
public interface Board<T> extends Iterable<T> {

    int maxX();

    int maxY();

    int maxRot();
}
