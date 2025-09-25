package mujica.math.geometry.g2d;

import mujica.math.algebra.discrete.ClampedMath;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created in Ultramarine on 2022/9/7, named TransformPointMatrix.
 * Recreated on 2025/4/1.
 * The two dimensions of the matrix are A and B
 */
public class IterativeTransformedPointMatrix extends PointSequence {

    private static final long serialVersionUID = 0x89f5e3240126f74bL;

    private static int multiply(int sizeA, int sizeB) {
        if (sizeA < 0 || sizeB < 0) {
            throw new IllegalArgumentException();
        }
        return ClampedMath.INSTANCE.multiply(sizeA, sizeB);
    }

    protected final int sizeA, sizeB;

    @NotNull
    protected final Transform2 transformA, transformB;

    @NotNull
    protected final Point origin;

    public IterativeTransformedPointMatrix(int sizeA, @NotNull Transform2 transformA, int sizeB, @NotNull Transform2 transformB, @NotNull Point origin) {
        super(multiply(sizeA, sizeB));
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.transformA = transformA;
        this.transformB = transformB;
        this.origin = origin;
    }

    @Override
    protected void set(int index) {
        reusedPoint.setPoint(origin);
        while (index >= sizeB) {
            index -= sizeB;
            transformA.transform(reusedPoint, reusedPoint);
        }
        while (index-- > 0) {
            transformB.transform(reusedPoint, reusedPoint);
        }
    }

    @Override
    public void forEach(Consumer<? super Point> action) {
        if (size > 0) {
            Point pointB = new Point(origin);
            action.accept(pointB);
            for (int indexB = 1; indexB < sizeB; indexB++) {
                transformB.transform(pointB, pointB);
                action.accept(pointB);
            }
            if (sizeA > 1) {
                Point pointA = new Point(origin);
                for (int indexA = 1; indexA < sizeA; indexA++) {
                    transformA.transform(pointA, pointA);
                    pointB.setPoint(pointA);
                    action.accept(pointB);
                    for (int indexB = 1; indexB < sizeB; indexB++) {
                        transformB.transform(pointB, pointB);
                        action.accept(pointB);
                    }
                }
            }
        }
    }

    @Override
    @NotNull
    public Iterator<Point> iterator() {
        return new TransformedPointIterator();
    }

    /**
     * Created on 2022/9/7.
     */
    private class TransformedPointIterator implements Iterator<Point> {

        final Point pointA = new Point(origin);
        final Point pointB = new Point();

        int indexA;
        int indexB;

        @Override
        public boolean hasNext() {
            return indexA < sizeA;
        }

        @Override
        public Point next() {
            // todo
            return pointB;
        }
    }
}
