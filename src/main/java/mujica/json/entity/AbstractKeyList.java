package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.function.Predicate;

@CodeHistory(date = "2025/10/2")
public abstract class AbstractKeyList extends AbstractList<Object> {

    @Override
    public abstract int size();

    @Override
    @NotNull
    public abstract Object get(@Index(of = "this", supportsNegative = true) int index);

    @NotNull
    public abstract Predicate<Object> getAsPredicate(@Index(of = "this", supportsNegative = true) int index);

    /**
     * @return -1 if the item do not represent one index
     */
    public abstract int getAsIndex(@Index(of = "this", supportsNegative = true) int index);

    /**
     * @return null if the item do not represent one name
     */
    @Nullable
    public abstract String getAsName(@Index(of = "this", supportsNegative = true) int index);
}
