package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;

import java.io.Serializable;

@CodeHistory(date = "2026/6/5")
@DirectSubclass({Zero.class, Next.class})
public abstract class NaturalType implements Serializable {

    private static final long serialVersionUID = 0xF6000A2D56F3F1EBL;

    public abstract int ordinal();
}
