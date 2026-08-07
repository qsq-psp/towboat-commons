package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/7/29")
public class PlainObjectSlot {

    Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
