package com.radu.dlx.problem.polyomino;

import com.radu.dlx.problem.Board;

import java.util.Iterator;

class PolyominoBoard implements Board<Polyomino> {
    //TODO: Generalize to other board sizes
    int maxX = 20;
    int maxY = 3;
    int maxRot = 4;
    int maxReflect = 2;
    int polynominoSize = 5;//TODO: if we have different order polynominos, we cannot assume this
    //TODO: Create polyomino type class that will contain information on the polyonimo -> ditch the enum Pentomino


    @Override
    public Iterator<Polyomino> iterator() {
        final Iterator<Polyomino> pentominos = Pentomino.iterator();
        final Iterator<Polyomino> currentIterator = createIterator(pentominos.next());
        return new Iterator<>() {
            Iterator<Polyomino> iterator = currentIterator;

            @Override
            public boolean hasNext() {
                boolean hasNext = iterator.hasNext();
                if (!hasNext && pentominos.hasNext()) {
                    iterator = createIterator(pentominos.next());
                    return iterator.hasNext();
                }
                return hasNext;
            }

            @Override
            public Polyomino next() {
                return iterator.next();
            }
        };
    }

    private int[][] generatePermutations() {
        int[][] permutations = new int[maxX * maxY * maxRot * maxReflect][4];
        int i = 0;
        for (int row = 0; row < maxY; row++) {
            for (int col = 0; col < maxX; col++) {
                for (int rot = 0; rot < maxRot; rot++) {
                    permutations[i++] = new int[]{col, row, rot, 0};
                }
            }
        }
        for (int row = 0; row < maxY; row++) {
            for (int col = 0; col < maxX; col++) {
                for (int rot = 0; rot < maxRot; rot++) {
                    permutations[i++] = new int[]{col, row, rot, 1};
                }
            }
        }
        return permutations;
    }

    private Iterator<Polyomino> createIterator(Polyomino seed) {
        int[][] permutations = generatePermutations();
        return new PolyominoIterator(this, seed, permutations);
    }

    @Override
    public int maxX() {
        return maxX;
    }

    @Override
    public int maxY() {
        return maxY;
    }

    @Override
    public int maxRot() {
        return maxRot;
    }
}
