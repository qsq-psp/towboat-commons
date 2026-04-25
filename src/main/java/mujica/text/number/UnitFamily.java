package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2026/4/17")
public class UnitFamily {

    @CodeHistory(date = "2026/4/17")
    static class Unit {

        final String name;

        final long scale;

        Unit(String name, long scale) {
            super();
            this.name = name;
            this.scale = scale;
        }
    }

    @NotNull
    final ArrayList<Unit> unitList = new ArrayList<>();
}
