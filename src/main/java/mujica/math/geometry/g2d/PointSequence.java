package mujica.math.geometry.g2d;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractList;

/**
 * Created in Ultramarine on 2022/9/7, named PointList.
 * Recreated on 2025/4/1.
 */
public class PointSequence extends AbstractList<Point> implements Serializable {

    private static final long serialVersionUID = 0x73d933e83ecf4affL;

    public static final Point[] EMPTY = new Point[0];

    public static Point[] newArray(int length) {
        final Point[] array = new Point[length];
        for (int index = 0; index < length; index++) {
            array[index] = new Point();
        }
        return array;
    }

    protected int size;

    protected transient Point reusedPoint;

    public PointSequence(int size, Point reusedPoint) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.reusedPoint = reusedPoint;
    }

    public PointSequence(int size) {
        this(size, new Point());
    }

    public PointSequence() {
        this(1, new Point());
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        reusedPoint = new Point();
    }

    /**
     * To be override by subclasses
     */
    protected void set(int index) {
        reusedPoint.reset();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Point get(int index) {
        if (0 <= index && index < size) {
            set(index);
            return reusedPoint;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
