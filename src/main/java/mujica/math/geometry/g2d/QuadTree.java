package mujica.math.geometry.g2d;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created on 2022/7/25.
 */
public class QuadTree<P extends Point> extends AbstractCollection<P> implements Serializable {

    private static final long serialVersionUID = 0xfc375d688f077610L;

    private final Point pivot = new Point();

    private Collection<P> section1, section2, section3, section4; // not null

    public QuadTree() {
        super();
        clear();
    }

    @Override
    public int size() {
        return section1.size() + section2.size() + section3.size() + section4.size();
    }

    @Override
    public boolean isEmpty() {
        return section1.isEmpty() && section2.isEmpty() && section3.isEmpty() && section4.isEmpty();
    }

    private Collection<P> section(Point point) {
        if (point.x < pivot.x) {
            if (point.y < pivot.y) {
                return section3;
            } else {
                return section2;
            }
        } else {
            if (point.y < pivot.y) {
                return section4;
            } else {
                return section1;
            }
        }
    }

    private Collection<P> afterAdd(Collection<P> section) {
        // TODO
        return section;
    }

    @Override
    public boolean contains(Object obj) {
        if (obj instanceof Point) {
            return section((Point) obj).contains(obj);
        }
        return false;
    }

    @Override
    public boolean add(P point) {
        if (point.x < pivot.x) {
            if (point.y < pivot.y) {
                section3.add(point);
                section3 = afterAdd(section3);
            } else {
                section2.add(point);
                section2 = afterAdd(section2);
            }
        } else {
            if (point.y < pivot.y) {
                section4.add(point);
                section4 = afterAdd(section4);
            } else {
                section1.add(point);
                section1 = afterAdd(section1);
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        if (obj instanceof Point) {
            return section((Point) obj).remove(obj);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends P> that) {
        int mask = 0;
        for (P point : that) {
            if (point.x < pivot.x) {
                if (point.y < pivot.y) {
                    section3.add(point);
                    mask |= 0b0100;
                } else {
                    section2.add(point);
                    mask |= 0b0010;
                }
            } else {
                if (point.y < pivot.y) {
                    section4.add(point);
                    mask |= 0b1000;
                } else {
                    section1.add(point);
                    mask |= 0b0001;
                }
            }
        }
        if (mask == 0) {
            return false;
        }
        if ((mask & 0b0001) != 0) {
            section1 = afterAdd(section1);
        }
        if ((mask & 0b0010) != 0) {
            section2 = afterAdd(section2);
        }
        if ((mask & 0b0100) != 0) {
            section3 = afterAdd(section3);
        }
        if ((mask & 0b1000) != 0) {
            section4 = afterAdd(section4);
        }
        return true;
    }

    @Override
    public void clear() {
        section1 = new ArrayList<>();
        section2 = new ArrayList<>();
        section3 = new ArrayList<>();
        section4 = new ArrayList<>();
    }

    @Override
    public Iterator<P> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<P> {

        private int area;

        private Iterator<P> itr;

        @Override
        public boolean hasNext() {
            while (itr == null) {
                switch (area++) {
                    case 0:
                        itr = section1.iterator();
                        break;
                    case 1:
                        itr = section2.iterator();
                        break;
                    case 2:
                        itr = section3.iterator();
                        break;
                    case 3:
                        itr = section4.iterator();
                        break;
                    default:
                        return false;
                }
                if (!itr.hasNext()) {
                    itr = null;
                }
            }
            return true;
        }

        @Override
        public P next() {
            while (itr == null) {
                switch (area++) {
                    case 0:
                        itr = section1.iterator();
                        break;
                    case 1:
                        itr = section2.iterator();
                        break;
                    case 2:
                        itr = section3.iterator();
                        break;
                    case 3:
                        itr = section4.iterator();
                        break;
                    default:
                        throw new IndexOutOfBoundsException();
                }
                if (!itr.hasNext()) {
                    itr = null;
                }
            }
            final P p = itr.next();
            if (!itr.hasNext()) {
                itr = null;
            }
            return p;
        }
    }

    @Override
    public void forEach(Consumer<? super P> action) {
        section1.forEach(action);
        section2.forEach(action);
        section3.forEach(action);
        section4.forEach(action);
    }
}
