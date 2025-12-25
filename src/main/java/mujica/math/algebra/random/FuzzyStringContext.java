package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;

/**
 * The base class generates free surrogate (paired or not paired)
 */
@CodeHistory(date = "2025/3/10")
public class FuzzyStringContext extends FuzzyContext {

    private static final long serialVersionUID = 0x635da2c808503d0eL;

    private static final String SPECIAL_CHARS = "\0\u0008\u0009\u000b\u000c\r\n !\"#%&'*./:?\\_`~\u007f\u0085\u00a0" // Control, latin-1 and supplement
            + "\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u200c\u200d\u200e\u200f" // General punctuation
            + "\u2028\u2029\u202a\u202b\u202c\u202d\u202e\u202f\u205f\u3000" // Format control
            + Character.MIN_HIGH_SURROGATE + Character.MAX_HIGH_SURROGATE + Character.MIN_LOW_SURROGATE + Character.MAX_LOW_SURROGATE
            + "\ufffc\ufffd\ufffe\uffff"; // Undefined characters

    public char nextChar() {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
                return (char) source.next(7); // ASCII
            case 0x3:
            case 0x4:
                return (char) source.next(8);
            case 0x5:
            case 0x6:
            case 0x7:
            case 0x8:
            case 0x9:
                return SPECIAL_CHARS.charAt(super.nextInt(SPECIAL_CHARS.length()));
            case 0xa:
                return (char) (Character.MIN_SURROGATE + source.next(11)); // Surrogate
            default:
                return (char) source.next(16);
        }
    }

    public static class WithoutSurrogate {

    }

    public static class AlwaysPairedSurrogate {

    }
}
