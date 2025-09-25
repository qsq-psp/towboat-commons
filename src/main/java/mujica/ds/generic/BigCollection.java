package mujica.ds.generic;

import mujica.ds.BigSize;
import mujica.reflect.modifier.CodeHistory;

import java.util.Collection;

@CodeHistory(date = "2025/5/26")
public interface BigCollection<E> extends Collection<E>, BigSize {
}
