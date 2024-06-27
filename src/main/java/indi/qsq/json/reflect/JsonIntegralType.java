package indi.qsq.json.reflect;

import indi.qsq.json.api.ParseHint;
import indi.qsq.util.text.Quote;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created in infrastructure on 2022/3/21.
 * Recreated on 2022/6/10.
 */
abstract class JsonIntegralType extends JsonType {

    private static final long serialVersionUID = 0xDC218CAD5E0E00C5L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonIntegralType.class);

    JsonIntegralType() {
        super();
    }

    /**
     * Different from ParseHint.ACCEPT_STRING
     */
    static final int ACCEPT_STRING = ParseHint.ACCEPT_DEC_UNITS | ParseHint.ACCEPT_BIN_UNITS
            | ParseHint.ACCEPT_DEC_STRING | ParseHint.ACCEPT_HEX_STRING | ParseHint.ACCEPT_BIN_STRING;

    private static final Pattern DEC_UNITS = Pattern.compile("(([+\\-]?|0[XxBb])[0-9A-Fa-f]+)[hHkKwWMGTPE]B?");
    private static final Pattern BIN_UNITS = Pattern.compile("(([+\\-]?|0[XxBb])[0-9A-Fa-f]+)[KMGTPE]iB?");

    protected Long parseUnits(String value, JsonConverter jc) {
        if (value == null || value.isBlank() || !anyParseConfig(ACCEPT_STRING)) {
            if (jc.logEnabled()) {
                if (value == null) {
                    value = "";
                }
                LOGGER.debug("Refuse to parse string {} to integral in {}", Quote.DEFAULT.apply(value), jc);
            }
            return null; // mean UNDEFINED
        }
        Matcher matcher;
        if (anyParseConfig(ParseHint.ACCEPT_DEC_UNITS) && (matcher = DEC_UNITS.matcher(value)).matches()) {
            long multiplier = 1L;
            switch (value.charAt(matcher.end(1))) {
                case 'h':
                case 'H':
                    multiplier = 10L;
                    break;
                case 'k':
                case 'K':
                    multiplier = 1000L;
                    break;
                case 'w':
                case 'W':
                    multiplier = 10000L;
                    break;
                case 'M':
                    multiplier = 1000_000L;
                    break;
                case 'G':
                    multiplier = 1000_000_000L;
                    break;
                case 'T':
                    multiplier = 1000_000_000_000L;
                    break;
                case 'P':
                    multiplier = 1000_000_000_000_000L;
                    break;
                case 'E':
                    multiplier = 1000_000_000_000_000_000L;
                    break;
                default:
                    if (jc.logEnabled()) {
                        LOGGER.warn("Unexpected decimal unit {} in {}", Quote.DEFAULT.apply(value), jc);
                    }
                    break;
            }
            return multiply(parseRadix(matcher.group(1), jc), multiplier, jc);
        }
        if (anyParseConfig(ParseHint.ACCEPT_BIN_UNITS) && (matcher = BIN_UNITS.matcher(value)).matches()) {
            long multiplier = 1L;
            switch (value.charAt(matcher.end(1))) {
                case 'K':
                    multiplier = 0x400L; // 2 ** 10
                    break;
                case 'M':
                    multiplier = 0x100_000L; // 2 ** 20
                    break;
                case 'G':
                    multiplier = 0x40_000_000L; // 2 ** 30
                    break;
                case 'T':
                    multiplier = 0x10_000_000_000L; // 2 ** 40
                    break;
                case 'P':
                    multiplier = 0x4_000_000_000_000L; // 2 ** 50
                    break;
                case 'E':
                    multiplier = 0x1_000_000_000_000_000L; // 2 ** 60
                    break;
                default:
                    if (jc.logEnabled()) {
                        LOGGER.warn("Unexpected binary unit {} in {}", Quote.DEFAULT.apply(value), jc);
                    }
                    break;
            }
            return multiply(parseRadix(matcher.group(1), jc), multiplier, jc);
        }
        return parseRadix(value, jc);
    }

    private static final Pattern DEC_STRING = Pattern.compile("[+\\-]?\\d+");
    private static final Pattern HEX_STRING = Pattern.compile("0[Xx][0-9A-Fa-f]+");
    private static final Pattern BIN_STRING = Pattern.compile("0[Bb][01]+");

    private Long parseRadix(String value, JsonConverter jc) {
        final int length = value.length();
        try {
            if (anyParseConfig(ParseHint.ACCEPT_DEC_STRING) && DEC_STRING.matcher(value).matches()) {
                return Long.parseLong(value);
            }
            if (anyParseConfig(ParseHint.ACCEPT_HEX_STRING) && HEX_STRING.matcher(value).matches()) {
                return Long.parseLong(value, 2, length, 16);
            }
            if (anyParseConfig(ParseHint.ACCEPT_BIN_STRING) && BIN_STRING.matcher(value).matches()) {
                return Long.parseLong(value, 2, length, 2);
            }
            if (jc.logEnabled()) {
                LOGGER.warn("Fail to parse radix of String {} in {}", Quote.DEFAULT.apply(value), jc);
            }
        } catch (ArithmeticException e) {
            if (jc.logEnabled()) {
                LOGGER.warn("Fail to parse radix of String {} in {}", Quote.DEFAULT.apply(value), jc, e);
            }
        }
        return null; // mean UNDEFINED
    }

    /**
     * b is always positive
     */
    @Nullable
    private Long multiply(@Nullable Long a, long b, JsonConverter jc) {
        if (b == 1L) {
            return a; // quick pass
        }
        if (a != null) {
            return multiplyExact(a, b, jc);
        } else {
            return null; // mean UNDEFINED
        }
    }

    /**
     * b is always positive
     */
    private long multiplyExact(long a, long b, JsonConverter jc) {
        long c = a * b;
        if (jc.logEnabled()) { // check overflow
            long aa = Math.abs(a);
            long ab = Math.abs(b);
            if (((aa | ab) >>> (Integer.SIZE - 1) != 0) && c / b != a) {
                LOGGER.warn("Long multiplication {} * {} overflow in {}", a, b, jc);
            }
        }
        return c;
    }
}
