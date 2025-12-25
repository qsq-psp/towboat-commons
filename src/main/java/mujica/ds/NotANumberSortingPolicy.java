package mujica.ds;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/10/28")
public enum NotANumberSortingPolicy {

    START, END, MIDDLE, SMALLEST, LARGEST, ORIGINAL_POSITION, ANY_POSITION, THROW;

    private static final long serialVersionUID = 0xced55a0ceff165b4L;
}
