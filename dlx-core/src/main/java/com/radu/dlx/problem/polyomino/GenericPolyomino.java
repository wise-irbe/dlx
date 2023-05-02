package com.radu.dlx.problem.polyomino;

import com.radu.dlx.Utilities;
import com.radu.dlx.problem.ReflectionSymmetry;
import com.radu.dlx.problem.RotationalSymmetry;

import java.util.Objects;
import java.util.function.BiConsumer;

//TODO: Add xn,yn -> we can move some bound checking here
public final class GenericPolyomino implements Polyomino {
    //TODO: can we avoid sorting?
    private static final BiConsumer<int[], int[]> DICTIONARY_SORTER = new DictionaryInsertionSort();

    private static final int XS_IDX = 0;
    private static final int YS_IDX = 1;
    private static final int ROT_IDX = 2;
    private static final int REFLECT_IDX = 3;

    private static final int OFFSET_IDX = 0;
    private static final int DELTA_IDX = 1;

    private static final int ROTATION_0 = 0;
    private static final int ROTATION_90 = 1;
    private static final int ROTATION_180 = 2;
    private static final int ROTATION_270 = 3;
    private static final int ROTATION_360 = 4;

    private final int size;

    private final int[] xs;
    private final int[] ys;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String name;
    private final int rotation;
    private final RotationalSymmetry rotational;
    private final ReflectionSymmetry reflection;
    private final int reflected;


    public static Polyomino create(String name, int[] xs, int[] ys) {
        return new GenericPolyomino(name, xs, ys, 0);
    }

    public static Polyomino create(String name, int[] xs, int[] ys, RotationalSymmetry rotational, ReflectionSymmetry reflection) {
        return new GenericPolyomino(name, xs, ys, 0, rotational, reflection);
    }

    private GenericPolyomino(String name, int[] xs, int[] ys, int reflected, RotationalSymmetry rotational, ReflectionSymmetry reflection) {
        this.name = name;
        size = xs.length;
        this.xs = xs.clone();
        this.ys = ys.clone();
        this.reflected = reflected;
        DICTIONARY_SORTER.accept(this.xs, this.ys);
        int[] calc = findOffsetAndDelta(this.xs);//TODO: for sorted array, we can obtain answer way faster
        x = calc[OFFSET_IDX];
        width = calc[DELTA_IDX];
        calc = findOffsetAndDelta(this.ys);
        y = calc[OFFSET_IDX];
        height = calc[DELTA_IDX];
        rotation = ROTATION_0;
        this.rotational = rotational;
        this.reflection = reflection;
    }

    private GenericPolyomino(String name, int[] xs, int[] ys, int reflected) {
        this(name, xs, ys, reflected, RotationalSymmetry.C1, ReflectionSymmetry.CHIRAL);
    }

    private GenericPolyomino(Polyomino polyomino, int drot) {
        this.name = polyomino.name();
        size = polyomino.size();
        int[][] xy = rotate(polyomino.xs(), polyomino.ys(), ROTATION_360, drot);
        xs = xy[XS_IDX];
        ys = xy[YS_IDX];
        DICTIONARY_SORTER.accept(xs, ys);
        int[] calc = findOffsetAndDelta(xs);
        x = calc[OFFSET_IDX];
        width = calc[DELTA_IDX];
        calc = findOffsetAndDelta(ys);
        y = calc[OFFSET_IDX];
        height = calc[DELTA_IDX];
        rotation = polyomino.rotation() + drot;
        rotational = polyomino.rotational();
        reflection = polyomino.reflection();
        reflected = polyomino.reflected();
    }

    private GenericPolyomino(Polyomino polyomino, int dx, int dy) {
        this.name = polyomino.name();
        size = polyomino.size();
        int[] newXs = translate(polyomino.xs(), dx);
        int[] newYs = translate(polyomino.ys(), dy);
        DICTIONARY_SORTER.accept(newXs, newYs);
        this.xs = newXs;
        this.ys = newYs;
        int[] calc = findOffsetAndDelta(newXs);
        x = calc[OFFSET_IDX];
        width = calc[DELTA_IDX];
        calc = findOffsetAndDelta(newYs);
        y = calc[OFFSET_IDX];
        height = calc[DELTA_IDX];
        rotation = polyomino.rotation();
        rotational = polyomino.rotational();
        reflection = polyomino.reflection();
        reflected = polyomino.reflected();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("%s(x=%d,y=%d,rot=%d,refl=%d):", name, x, y, rotation, reflected));
        for (int i = 0, len = xs.length; i < len; i++) {
            int xi = xs[i];
            int yi = ys[i];
            result.append(Utilities.decode(yi)).append(Utilities.decode(xi));
            if (i + 1 < len) {
                result.append('-');
            }

        }

        int[][] area = new int[height][width];

        for (int i = 0, width = xs.length; i < width; i++) {
            area[ys[i] - y][xs[i] - x] = -1;
        }
        result.append('\n');
        if (y > 0) {
            for (int i = 0; i < y; i++) {
                result.append("o".repeat(x + width)).append('\n');
            }
        }
        for (int i = 0; i < height; i++) {
            if (x > 0) {
                result.append("o".repeat(x));
            }
            for (int j = 0; j < width; j++) {
                if (area[i][j] == -1) {
                    result.append("x");
                } else {
                    result.append("o");
                }
            }
            result.append('\n');
        }
        return result.toString();//Q:00-01-02-03-13 (68,70)
    }

    @Override
    public boolean canTranslateX(int maxX, int dx) {
        return maxX >= x + dx + width;
    }

    @Override
    public boolean canTranslateY(int maxY, int dy) {
        return maxY >= y + dy + height;
    }

    @Override
    public Polyomino translateX(int dx) {
        return new GenericPolyomino(this, dx, 0);
    }

    @Override
    public Polyomino translateY(int dy) {
        return new GenericPolyomino(this, 0, dy);
    }

    private int[] translate(int[] s, int d) {
        if (d == 0) {
            return s;
        }
        int[] result = new int[s.length];
        for (int i = 0, len = s.length; i < len; i++) {
            result[i] = s[i] + d;
        }
        return result;
    }

    @Override
    public boolean canRotate(int maxX, int maxY, int maxRot, int rot, int dx, int dy, int drot) {
        if (hasSymmetricRotation(maxRot, rot, drot)) {
            return false;
        }
        int truncRot = normaliseRotation(maxRot, drot);
        if (truncRot >= maxRot || truncRot == ROTATION_0) {
            return false;
        } else if (truncRot == ROTATION_90 || truncRot == ROTATION_270) {
            return maxX >= x + dx + height && maxY >= y + dy + width;
        } else if (truncRot == ROTATION_180) {
            return true;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean hasNonSymmetricReflection() {
        return reflection == ReflectionSymmetry.CHIRAL;
    }

    @Override
    public boolean hasSymmetricRotation(int maxRot, int rot, int drot) {
        if (drot == ROTATION_0 || rotational == RotationalSymmetry.C4) {
            return true;
        }
        int truncRot = normaliseRotation(maxRot, drot) - rot;
        return (rotational == RotationalSymmetry.C2 || rotational == RotationalSymmetry.C2V) && (truncRot == ROTATION_180)
                || rotational == RotationalSymmetry.C2H && (truncRot == ROTATION_180 || truncRot == ROTATION_270);
    }

    private int normaliseRotation(int maxRot, int drot) {
        return drot < ROTATION_0 ? drot + maxRot : drot;
    }

    @Override
    public Polyomino rotate(int drot) {
        return new GenericPolyomino(this, drot);
    }

    @Override
    public Polyomino reflectX() {
        return new GenericPolyomino(name, reflect(xs), ys, 1, rotational, reflection);
    }

    private int[][] rotate(int[] xs, int[] ys, int maxRot, int drot) {
        int x = findMin(xs);
        int y = findMin(ys);

        int[][] result = new int[2][];
        int truncRot = normaliseRotation(maxRot, drot);
        if (truncRot == ROTATION_0) {
            result[XS_IDX] = xs.clone();
            result[YS_IDX] = ys.clone();
        } else if (truncRot == ROTATION_270) {
            result[XS_IDX] = translateToOrigin(ys.clone(), x);
            result[YS_IDX] = translateToOrigin(reflect(xs), y);
        } else if (truncRot == ROTATION_180) {
            result[XS_IDX] = translateToOrigin(reflect(xs), x);
            result[YS_IDX] = translateToOrigin(reflect(ys), y);
        } else if (truncRot == ROTATION_90) {
            result[XS_IDX] = translateToOrigin(reflect(ys), x);
            result[YS_IDX] = translateToOrigin(xs.clone(), y);
        } else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    private int[] translateToOrigin(int[] s, int s0) {
        int min = findMin(s);
        if (min - s0 != 0) {
            int delta = min - s0;
            for (int i = 0, len = s.length; i < len; i++) {
                s[i] = s[i] - delta;
            }
        }
        return s;
    }

    private int[] reflect(int[] s) {
        int[] result = new int[s.length];
        int max = findMax(s);
        for (int i = 0, len = s.length; i < len; i++) {
            result[i] = max - s[i];
        }
        return result;
    }

    @Override
    public boolean equalsTo(int[] params) {
        int x = params[XS_IDX];
        int y = params[YS_IDX];
        int rotation = params[ROT_IDX];
        int reflected = params[REFLECT_IDX];
        return this.x == x && this.y == y && this.rotation == rotation && this.reflected == reflected;
    }

    private int[] findOffsetAndDelta(int[] vals) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0, len = vals.length; i < len; i++) {
            int val = vals[i];
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }
        int[] result = new int[2];
        result[OFFSET_IDX] = min;
        result[DELTA_IDX] = max - min + 1;
        return result;
    }

    private int findMax(int[] vals) {
        int max = Integer.MIN_VALUE;
        for (int i = 0, len = vals.length; i < len; i++) {
            int val = vals[i];
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    private int findMin(int[] vals) {
        int min = Integer.MAX_VALUE;
        for (int i = 0, len = vals.length; i < len; i++) {
            int val = vals[i];
            if (val < min) {
                min = val;
            }
        }
        return min;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public String[] code() {
        String[] result = new String[xs.length + 1];
        result[0] = name; //TODO: test with names and without
        for (int i = 0, len = xs.length; i < len; i++) {
            result[i + 1] = Utilities.decode(ys[i]) + Utilities.decode(xs[i]);
        }
        return result;
    }

    @Override
    public int[] xs() {
        return xs;
    }

    @Override
    public int[] ys() {
        return ys;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int rotation() {
        return rotation;
    }

    @Override
    public RotationalSymmetry rotational() {
        return rotational;
    }

    @Override
    public ReflectionSymmetry reflection() {
        return reflection;
    }

    @Override
    public int reflected() {
        return reflected;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericPolyomino that = (GenericPolyomino) o;
        return x == that.x &&
                y == that.y &&
                rotation == that.rotation &&
                reflected == that.reflected &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y, rotation);
    }
}
