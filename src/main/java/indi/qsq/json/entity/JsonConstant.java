package indi.um.json.entity;

/**
 * Created on 2022/7/8.
 */
public enum JsonConstant {

    UNDEFINED, NULL, PRESENT;

    private static final long serialVersionUID = 0x38FCC7EBDC1F7A01L;

    public String toString() {
        return name().toLowerCase();
    }
}
