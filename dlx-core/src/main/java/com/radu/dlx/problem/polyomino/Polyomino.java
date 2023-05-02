package com.radu.dlx.problem.polyomino;

import com.radu.dlx.problem.ReflectionSymmetry;
import com.radu.dlx.problem.RotationalSymmetry;

public interface Polyomino {
    int x();

    int y();

    int height();

    int width();

    String[] code();

    int[] xs();

    int[] ys();

    boolean canTranslateX(int max, int x);

    boolean canTranslateY(int maxY, int y);

    boolean canRotate(int maxX, int maxY, int maxRot, int rot, int dx, int dy, int drot);

    boolean hasNonSymmetricReflection();

    boolean hasSymmetricRotation(int maxRot, int rot, int drot);

    Polyomino translateX(int x);

    Polyomino translateY(int y);

    Polyomino rotate(int rotate);

    Polyomino reflectX();

    boolean equalsTo(int[] param);

    String toString();

    String name();

    int rotation();

    RotationalSymmetry rotational();

    ReflectionSymmetry reflection();

    int reflected();

    int size();
}
