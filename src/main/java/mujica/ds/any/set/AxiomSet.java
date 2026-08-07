package mujica.ds.any.set;

import mujica.ds.common.DataStructure;
import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Set;

@CodeHistory(date = "2025/6/4")
public interface AxiomSet<E> extends Set<E>, DataStructure {

    E intern(E element, boolean force);

    E getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;

    E removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;
}
