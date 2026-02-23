package mujica.ds.of_double.list;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/10/28")
public enum NotANumberSortingPolicy {

    /**
     * NaN is placed at the start (small index) of sorted sequence
     */
    START,
    /**
     * NaN is placed at the end (large index) of sorted sequence
     */
    END,
    /**
     * NaN is placed in the middle of sorted sequence, between -0.0 and +0.0
     */
    MIDDLE,
    /**
     * NaN is smaller than negative infinity
     */
    SMALLEST,
    /**
     * NaN is larger than positive infinity
     */
    LARGEST,
    /**
     * each NaN is at its original index, after removing all NaN, the sequence is ordered
     */
    ORIGINAL_POSITION,
    /**
     * NaN may be moved to any position, after removing all NaN, the sequence is ordered
     */
    ANY_POSITION,
    /**
     * NaN may be moved to any position, an adjacent pair is ordered if it contains no NaN
     */
    ANY_ORDER,
    /**
     * an ArithmeticException is thrown if there is a NaN
     */
    THROW;

    private static final long serialVersionUID = 0xced55a0ceff165b4L;
}
