package mujica.math.geometry.g2d;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created in Ultramarine on 2022/9/7, named TransformPointSequence.
 * Recreated on 2025/4/1.
 */
public class IterativeTransformedPointSequence extends PointSequence {

    private static final long serialVersionUID = 0x80cb04cc3bddda60L;

    protected final Transform2 transform;

    protected final Point origin;

    public IterativeTransformedPointSequence(int size, Transform2 transform, Point origin) {
        super(size);
        this.transform = transform;
        this.origin = origin;
    }

    @Override
    protected void set(int index) {
        reusedPoint.setPoint(origin);
        while (index-- > 0) {
            transform.transform(reusedPoint, reusedPoint);
        }
    }

    @Override
    @NotNull
    public Point[] toArray() {
        final int size = this.size;
        if (size > 0) {
            Point[] array = newArray(size);
            array[0].setPoint(origin);
            for (int index = 1; index < size; index++) {
                transform.transform(array[index - 1], array[index]);
            }
            return array;
        } else {
            return EMPTY;
        }
    }

    @Override
    public void forEach(Consumer<? super Point> action) {
        final int size = this.size;
        if (size > 0) {
            Point point = new Point(origin);
            action.accept(point);
            for (int index = 1; index < size; index++) {
                transform.transform(point, point);
                action.accept(point);
            }
        }
    }

    @Override
    @NotNull
    public Iterator<Point> iterator() {
        return new TransformedPointIterator();
    }

    /**
     * Created on 2022/7/8.
     */
    private class TransformedPointIterator implements Iterator<Point> {

        final Point point = new Point(origin);

        int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Point next() {
            if (index++ != 0) {
                transform.transform(point, point);
            }
            return point;
        }
    }
}
