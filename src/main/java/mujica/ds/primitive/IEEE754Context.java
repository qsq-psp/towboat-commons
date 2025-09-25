package mujica.ds.primitive;

import mujica.reflect.modifier.Assigned;

/**
 * Created on 2025/6/16.
 */
public interface IEEE754Context extends RealContext {

    @Assigned
    int nan();

    @Assigned
    int positiveInfinity();

    @Assigned
    int negativeInfinity();

    void unitInLastPlace(@Assigned int srcVariable, @Assigned int dstVariable);
}
