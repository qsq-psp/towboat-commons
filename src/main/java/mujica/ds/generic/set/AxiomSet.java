package mujica.ds.generic.set;

import mujica.ds.DataStructure;
import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Set;

@CodeHistory(date = "2025/6/4")
public interface AxiomSet<E> extends Set<E>, DataStructure {

    E intern(E element, boolean force);

    E getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;

    @ReferencePage(title = "set.pop()", href = "https://docs.python.org/3/library/stdtypes.html#frozenset.pop")
    E removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;
}
