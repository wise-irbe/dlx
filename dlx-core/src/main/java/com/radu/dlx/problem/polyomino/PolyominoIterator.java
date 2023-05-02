package com.radu.dlx.problem.polyomino;

import com.radu.dlx.problem.Board;

import java.util.Iterator;
import java.util.NoSuchElementException;

class PolyominoIterator implements Iterator<Polyomino> {
    private static final int X_IDX = 0;
    private static final int Y_IDX = 1;
    private static final int ROT_IDX = 2;
    private static final int REFLECT_IDX = 3;
    
    private final int[][] permutations;
    private final Polyomino seed;
    private final int maxX;
    private final int maxY;
    private final int maxRot;

    private Polyomino polyomino;

    PolyominoIterator(Board<Polyomino> board, Polyomino seed, int[][] permutations) {
        polyomino = seed;
        this.seed = seed;
        this.permutations = permutations;
        maxX = board.maxX();
        maxY = board.maxY();
        maxRot = board.maxRot();
    }

    boolean hasPolyomino = false;
    int pos = 0;

    @Override
    public boolean hasNext() {
        if (!hasCandidate()) {
            hasPolyomino = false;
            return hasPolyomino;
        }
        if (pos == 0 && isValid(polyomino)) {
            pos++;
            hasPolyomino = true;
        } else {
            hasPolyomino = findPermutation();
        }
        return hasPolyomino;
    }

    private boolean hasCandidate() {
        return permutations.length > 0 && pos + 1 < permutations.length;
    }

    @Override
    public Polyomino next() {
        if (!hasPolyomino) {
            throw new NoSuchElementException();
        }
        return polyomino;
    }

    private boolean findPermutation() {
        boolean found = findPermutation(pos++);
        while (!found && hasCandidate()) {
            found = findPermutation(pos++);
        }
        return found;
    }

    private boolean findPermutation(int initPos) {
        return next(permutations[initPos]);
    }

    private boolean next(int[] permutation) {
        return setIfValid(permutation, generate(permutation, seed));
    }

    private boolean setIfValid(int[] permutation, Polyomino candidate) {
        if (candidate == null) {
            return false;
        }
        boolean transformed = false;

        if (!candidate.equals(seed)
                && !polyomino.equals(candidate)
                && isValid(candidate)
                && candidate.equalsTo(permutation)) {
            transformed = true;
            polyomino = candidate;//Q 0,0,0,1//0,0,3,1 // Q:00-01-02-03-13
        }
        return transformed;
    }

    private Polyomino generate(int[] permutation, Polyomino init) {
        int dx = permutation[X_IDX];
        int dy = permutation[Y_IDX];
        int drot = permutation[ROT_IDX];
        int dreflect = permutation[REFLECT_IDX];
        Polyomino result = init;
        boolean hasReflection = dreflect != 0 && hasReflection(result);
        if (hasReflection) {
            result = result.reflectX();
        }
        if (hasReflection && dx == 0 && dy == 0 && drot == 0) {
            return result;
        }
        if (dreflect == 0 || hasReflection) {
            if (dx != 0 && dy != 0 && drot != 0 && !hasSymmetricalRotation(result, drot)) {
                Polyomino candidate = result.rotate(drot);
                if (hasTranslateX(candidate, dx) && hasTranslateY(candidate, dy)) {
                    candidate = candidate.translateY(dy);
                    return candidate.translateX(dx);
                }
            } else if (dx != 0 && dy != 0) {
                if (hasTranslateX(result, dx)) {
                    Polyomino candidate = result.translateX(dx);
                    if (hasTranslateY(candidate, dy)) {
                        return candidate.translateY(dy);
                    }
                }
            } else if (dx == 0 && dy != 0 && drot != 0 && !hasSymmetricalRotation(result, drot)) {
                Polyomino candidate = result.rotate(drot);
                if (hasTranslateY(candidate, dy)) {
                    return candidate.translateY(dy);
                }
            } else if (dx != 0 && drot != 0 && !hasSymmetricalRotation(result, drot)) {
                Polyomino candidate = result.rotate(drot);
                if (hasTranslateX(candidate, dx)) {
                    return candidate.translateX(dx);
                }
            } else if (dx != 0) {
                if (hasTranslateX(result, dx)) {
                    return result.translateX(dx);
                }
            } else if (dy != 0) {
                if (hasTranslateY(result, dy)) {
                    return result.translateY(dy);
                }
            } else if (drot != 0) {
                if (hasRotate(result, dx, dy, drot)) {
                    return result.rotate(drot);
                }
            }
        }
        return null;
    }

    private boolean hasRotate(Polyomino candidate, int dx, int dy, int drot) {
        return candidate.canRotate(maxX, maxY, maxRot, dx, dy, candidate.rotation(), drot);
    }

    private boolean hasReflection(Polyomino candidate) {
        return candidate.hasNonSymmetricReflection();
    }

    private boolean hasSymmetricalRotation(Polyomino candidate, int drot) {
        return candidate.hasSymmetricRotation(maxRot, candidate.rotation(), drot);
    }

    private boolean hasTranslateX(Polyomino candidate, int dx) {
        return candidate.canTranslateX(maxX, dx) && candidate.canTranslateY(maxY, 0);
    }

    private boolean hasTranslateY(Polyomino candidate, int dy) {
        return candidate.canTranslateY(maxY, dy) && candidate.canTranslateX(maxX, 0);
    }

    private boolean isValid(Polyomino candidate) {
        boolean barrier = candidate.height() == maxY;
        int[] edges = edgeAreas(candidate);
        int leftArea = edges[0];
        int rightArea = edges[1];
        int leftSurfaceArea = candidate.x() * maxY
                + leftArea;
        int rightSurfaceArea = (maxX - (candidate.x() + candidate.width())) * maxY
                + rightArea;
        if (!barrier) {
            if (rightArea != 0 && leftArea != 0) {
                int area = Integer.min(leftSurfaceArea, rightSurfaceArea);
                return area == 0 || area > 4;
            } else {
                return true;
            }
        }
        return isValidArea(candidate, leftSurfaceArea)
                && isValidArea(candidate, rightSurfaceArea);
    }

    private boolean isValidArea(Polyomino polyomino, int area) {
        return area == 0 || area % polyomino.size() == 0;
    }

    private int[] edgeAreas(Polyomino candidate) {
        int x = candidate.x();
        int[] xs = candidate.xs();
        int[] ys = candidate.ys();
        int[][] area = new int[maxY][candidate.width()];

        for (int i = 0, width = xs.length; i < width; i++) {
            area[ys[i]][xs[i] - x] = -1;
        }

        int[] areas = new int[5];
        int areaId = 0;
        for (int i = 0, height = maxY; i < height; i++) {
            for (int j = 0, width = candidate.width(); j < width; j++) {
                int id = areaId;
                if (area[i][j] == 0) {
                    int up = i - 1;
                    int left = j - 1;
                    if (j > 0 && area[i][left] > 0) {//look left
                        id = area[i][left]; //use the left id
                        areas[id]++;
                        if (i > 0 && area[up][j] > 0) {//look up and set all directly upper cells to the same id
                            int newId = area[up][j];
                            while (up > 0 && area[up][j] > 0) {
                                areas[newId]--;
                                areas[id]++;
                                area[up][j] = id;
                                up--;
                            }
                        }
                    } else if (i > 0 && area[i - 1][j] > 0) {//look up
                        id = area[up][j];
                        areas[id]++;
                        if (j > 0 && area[i][j - 1] > 0) {//look left
                            int newId = area[i][left];
                            while (left > 0 && area[i][left] > 0) {
                                areas[newId]--;
                                areas[id]++;
                                area[i][left] = id;
                                left--;
                            }
                        }
                    } else if (area[i][j] == 0) {//look here
                        id = ++areaId;
                        areas[id]++;
                    }
                    if (id > 0) {
                        area[i][j] = id;
                    }
                } else if (area[i][j] == -1) {
                    continue;
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        int[] leftAreas = new int[5];
        int[] rightAreas = new int[5];

        for (int i = 0; i < maxY; i++) {
            int id = area[i][candidate.width() - 1];
            if (id > 0) {
                rightAreas[id] = areas[id];
            }
        }

        for (int i = 0; i < maxY; i++) {
            int id = area[i][0];
            if (id > 0 && rightAreas[id] == 0) {
                leftAreas[id] = areas[id];
            }
        }

        int rightArea = 0;
        for (int i = 0, len = rightAreas.length; i < len; i++) {
            rightArea += rightAreas[i];
        }
        int leftArea = 0;
        for (int i = 0, len = leftAreas.length; i < len; i++) {
            if (rightAreas[i] == 0) {
                leftArea += leftAreas[i];
            }
        }
        return new int[]{leftArea, rightArea};
    }
}
