package com.radu.dlx.problem.polyomino;

import com.radu.dlx.problem.ReflectionSymmetry;
import com.radu.dlx.problem.RotationalSymmetry;

import java.util.EnumSet;
import java.util.Iterator;

enum Pentomino {
    O(GenericPolyomino.create("O", new int[]{0, 1, 2, 3, 4}, new int[]{0, 0, 0, 0, 0}, RotationalSymmetry.C2, ReflectionSymmetry.ACHIRAL)),
    P(GenericPolyomino.create("P", new int[]{0, 0, 1, 1, 2}, new int[]{0, 1, 0, 1, 0})),
    Z(GenericPolyomino.create("Z", new int[]{0, 1, 1, 1, 2}, new int[]{0, 0, 1, 2, 2}, RotationalSymmetry.C2H, ReflectionSymmetry.CHIRAL)),
    W(GenericPolyomino.create("W", new int[]{0, 0, 1, 1, 2}, new int[]{0, 1, 1, 2, 2}, RotationalSymmetry.C1, ReflectionSymmetry.ACHIRAL)),
    R(GenericPolyomino.create("R", new int[]{1, 2, 0, 1, 1}, new int[]{0, 0, 1, 1, 2})),
    Y(GenericPolyomino.create("Y", new int[]{0, 1, 2, 3, 2}, new int[]{0, 0, 0, 0, 1})),
    Q(GenericPolyomino.create("Q", new int[]{0, 0, 1, 2, 3}, new int[]{0, 1, 0, 0, 0})),
    S(GenericPolyomino.create("S", new int[]{0, 1, 2, 2, 3}, new int[]{1, 1, 1, 0, 0})),
    T(GenericPolyomino.create("T", new int[]{0, 0, 0, 1, 2}, new int[]{0, 1, 2, 1, 1}, RotationalSymmetry.C1, ReflectionSymmetry.ACHIRAL)),
    V(GenericPolyomino.create("V", new int[]{0, 1, 2, 2, 2}, new int[]{0, 0, 0, 1, 2}, RotationalSymmetry.C4, ReflectionSymmetry.ACHIRAL)),
    X(GenericPolyomino.create("X", new int[]{0, 1, 2, 1, 1}, new int[]{1, 1, 1, 0, 2}, RotationalSymmetry.C4, ReflectionSymmetry.ACHIRAL)),
    U(GenericPolyomino.create("U", new int[]{0, 1, 2, 0, 2}, new int[]{0, 1, 0, 1, 1}, RotationalSymmetry.C2V, ReflectionSymmetry.ACHIRAL));

    private static final EnumSet<Pentomino> PENTOMINOS = EnumSet.allOf(Pentomino.class);

    private final Polyomino polyomino;

    Pentomino(Polyomino polyomino) {
        this.polyomino = polyomino;
    }

    public static Iterator<Polyomino> iterator() {
        return PENTOMINOS.stream().map(p -> p.polyomino).iterator();
    }

}
