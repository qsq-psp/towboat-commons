package indi.qsq.json.reflect;

import indi.qsq.json.api.ParseHint;
import indi.qsq.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2022/3/21.
 */
class JsonFractionalType extends JsonType {

    private static final long serialVersionUID = 0x638255CC8D7E77E0L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFractionalType.class);
    
    protected JsonFractionalType() {
        super();
    }

    /**
     * Different from ParseHint.ACCEPT_STRING
     */
    static final int ACCEPT_STRING = JsonIntegralType.ACCEPT_STRING | ParseHint.ACCEPT_DOT_STRING | ParseHint.ACCEPT_SCI_STRING;

    private static final Pattern DEC_UNITS = Pattern.compile("([+\\-\\d.eE]+)([pnmcd]|[hHkKwWMGTPEZY]B?)");
    private static final Pattern BIN_UNITS = Pattern.compile("([+\\-\\d.eE]+)([pnm]i|[KMGTPEZY]iB?)");
    
    protected Double parseUnits(String value, @NotNull JsonConverter jv) {
        if (value == null || value.isBlank() || !anyParseConfig(ACCEPT_STRING)) {
            if (jv.logEnabled()) {
                if (value == null) {
                    value = "";
                }
                LOGGER.debug("Refuse to parse String {} to decimal in {}", Quote.DEFAULT.apply(value), jv);
            }
            return null; // mean UNDEFINED
        }
        Matcher matcher;
        if (anyParseConfig(ParseHint.ACCEPT_DEC_UNITS) && (matcher = DEC_UNITS.matcher(value)).matches()) {
            double multiplier = 1;
            switch (value.charAt(matcher.end(1))) {
                case 'p':
                    multiplier = 0.000_000_000_001;
                    break;
                case 'n':
                    multiplier = 0.000_000_001;
                    break;
                case 'm':
                    multiplier = 0.001;
                    break;
                case 'c':
                    multiplier = 0.01;
                    break;
                case 'd':
                    multiplier = 0.1;
                    break;
                case 'h':
                case 'H':
                    multiplier = 10.0;
                    break;
                case 'k':
                case 'K':
                    multiplier = 1000.0;
                    break;
                case 'w':
                case 'W':
                    multiplier = 10000.0;
                    break;
                case 'M':
                    multiplier = 1000_000.0;
                    break;
                case 'G':
                    multiplier = 1000_000_000.0;
                    break;
                case 'T':
                    multiplier = 1000_000_000_000.0;
                    break;
                case 'P':
                    multiplier = 1000_000_000_000_000.0;
                    break;
                case 'E':
                    multiplier = 1000_000_000_000_000_000.0;
                    break;
                case 'Z':
                    multiplier = 1000_000_000_000_000_000_000.0;
                    break;
                case 'Y':
                    multiplier = 1000_000_000_000_000_000_000_000.0;
                    break;
                default:
                    LOGGER.warn("Unexpected decimal unit {}", value);
                    break;
            }
            return multiply(parseForms(matcher.group(1), jv), multiplier);
        }
        if (anyParseConfig(ParseHint.ACCEPT_BIN_UNITS) && (matcher = BIN_UNITS.matcher(value)).matches()) {
            double multiplier = 1;
            switch (value.charAt(matcher.end(1))) {
                case 'p':
                    multiplier = 1.0 / 0x10_000_000_000L; // 2 ** -40
                    break;
                case 'n':
                    multiplier = 1.0 / 0x40_000_000L; // 2 ** -30
                    break;
                case 'm':
                    multiplier = 1.0 / 0x400L; // 2 ** -10
                    break;
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
                    LOGGER.warn("Unexpected binary unit {}", value);
                    break;
            }
            return multiply(parseForms(matcher.group(1), jv), multiplier);
        }
        return parseForms(value, jv);
    }

    private static final Pattern DOT_STRING = Pattern.compile("[+\\-]?((\\d*\\.\\d*|\\d+)|NaN|Infinity)");
    private static final Pattern SCI_STRING = Pattern.compile("[+\\-]?((\\d*\\.\\d*|\\d+)[eE][+\\-]?\\d+|NaN|Infinity)");

    private Double parseForms(String value, @NotNull JsonConverter jv) {
        try {
            if (anyParseConfig(ParseHint.ACCEPT_DOT_STRING) && DOT_STRING.matcher(value).matches()) {
                return checkSpecial(Double.parseDouble(value), jv);
            }
            if (anyParseConfig(ParseHint.ACCEPT_SCI_STRING) && SCI_STRING.matcher(value).matches()) {
                return checkSpecial(Double.parseDouble(value), jv);
            }
            if (jv.logEnabled()) {
                LOGGER.warn("Fail to parse form of String {} in {}", Quote.DEFAULT.apply(value), jv);
            }
        } catch (ArithmeticException e) {
            if (jv.logEnabled()) {
                LOGGER.warn("Fail to parse form of String {} in {}", Quote.DEFAULT.apply(value), jv, e);
            }
        }
        return null; // mean UNDEFINED
    }

    private Double checkSpecial(double value, @NotNull JsonConverter jv) {
        if (Double.isFinite(value)) {
            return value;
        }
        if (Double.isNaN(value)) {
            if (anyParseConfig(ParseHint.ACCEPT_NAN_STRING)) {
                return value;
            } else {
                if (jv.logEnabled()) {
                    LOGGER.warn("Unexpected NaN in {}", jv);
                }
                return null; // mean UNDEFINED
            }
        } else {
            if (anyParseConfig(ParseHint.ACCEPT_INFINITY_STRING)) {
                return value;
            } else {
                if (jv.logEnabled()) {
                    LOGGER.warn("Unexpected Infinity in {}", jv);
                }
                return null; // mean UNDEFINED
            }
        }
    }

    @Nullable
    private Double multiply(@Nullable Double a, double b) {
        if (a != null) {
            return a * b;
        } else {
            return null;
        }
    }
}
