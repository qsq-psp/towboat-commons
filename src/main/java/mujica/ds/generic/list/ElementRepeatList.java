package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created on 2026/1/24.
 */
@CodeHistory(date = "2026/1/24")
public class ElementRepeatList<E> extends PeriodicList<E> {

    private static final long serialVersionUID = 0xF8219D0994EF9421L;

    @Nullable
    protected E element;

    public ElementRepeatList(@Nullable E element, int times) {
        super();
        this.element = element;
        this.size = times;
    }

    public ElementRepeatList(@Nullable E element) {
        this(element, 1);
    }

    public ElementRepeatList(int times) {
        this(null, times);
    }

    @Override
    public int times() {
        return size;
    }

    @Override
    public int period() {
        return 1;
    }

    @Override
    public E get(int index) {
        if (0 <= index && index < size) {
            return element;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean contains(Object element) {
        return size != 0 && Objects.equals(this.element, element);
    }

    @Override
    public int indexOf(Object element) {
        if (size != 0 && Objects.equals(this.element, element)) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object element) {
        if (size != 0 && Objects.equals(this.element, element)) {
            return size - 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean add(E element) {
        if (size == 0) {
            this.element = element;
            size = 1;
            modCount++;
            return true;
        } else if (Objects.equals(this.element, element)) {
            size++;
            modCount++;
            return true;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void add(int index, E element) {
        if (0 <= index && index <= size) {
            if (size == 0) {
                this.element = element;
                size = 1;
                modCount++;
            } else if (Objects.equals(this.element, element)) {
                size++;
                modCount++;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public E set(int index, E newElement) {
        if (0 <= index && index < size) {
            if (size == 1) {
                E oldElement = this.element;
                this.element = newElement;
                modCount++;
                return oldElement;
            } else if (Objects.equals(this.element, newElement)) {
                return newElement;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public E remove(int index) {
        if (0 <= index && index < size) {
            size--;
            modCount++;
            return this.element;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean remove(Object element) {
        if (size != 0 && Objects.equals(this.element, element)) {
            size--;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        this.element = null;
        size = 0;
        modCount++;
    }
}
