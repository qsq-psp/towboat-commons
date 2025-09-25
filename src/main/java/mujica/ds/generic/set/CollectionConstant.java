package mujica.ds.generic.set;

/**
 * Created on 2025/6/5.
 */
public enum CollectionConstant {

    EMPTY, REMOVED, PRESENT;

    private static final long serialVersionUID = 0x06b771d0934428d4L;

    public String toString() {
        switch (this) {
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
