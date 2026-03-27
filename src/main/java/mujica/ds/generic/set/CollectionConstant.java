package mujica.ds.generic.set;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/7/8", project = "Ultramarine", name = "JsonConstant")
@CodeHistory(date = "2025/6/5")
public enum CollectionConstant {

    UNDEFINED, EMPTY, REMOVED, PRESENT;

    private static final long serialVersionUID = 0x06b771d0934428d4L;

    public String toString() {
        switch (this) {
            case UNDEFINED:
                return "<UNDEFINED>";
            case EMPTY:
                return "<EMPTY>";
            case REMOVED:
                return "<REMOVED>";
            case PRESENT:
                return "<PRESENT>";
            default:
                return "<?>";
        }
    }
}
