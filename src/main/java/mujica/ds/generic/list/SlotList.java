package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Assigned;

@CodeHistory(date = "2025/6/24")
public interface SlotList<E> {

    int size();

    @Assigned
    int store(E element);

    boolean canLoad(@Assigned int pointer);

    E load(@Assigned int pointer);

    E delete(@Assigned int pointer);
}
