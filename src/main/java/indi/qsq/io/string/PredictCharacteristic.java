package indi.qsq.io.string;

import indi.qsq.util.reflect.ActuallyChar;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Created on 2024/7/4.
 */
public final class PredictCharacteristic {

    public static boolean isNull(@ActuallyChar int ch) {
        return ch == 0;
    }

    public static boolean isQuotationMark(@ActuallyChar int ch) {
        return ch == '"';
    }

    public static boolean isApostrophe(@ActuallyChar int ch) {
        return ch == '\'';
    }

    public static boolean isNonAscii(@ActuallyChar int ch) {
        return ch >= 0x80;
    }

    public static boolean isNonBMP(@ActuallyChar int ch) {
        return Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE;
    }

    public static boolean isDirectLiteral(@ActuallyChar int ch) {
        return ch != '"' && ch != '\\';
    }

    public static boolean isDirectJson(@ActuallyChar int ch) {
        return isDirectLiteral(ch) && !Character.isISOControl(ch);
    }

    public static boolean isDirectXmlAttributeValue(@ActuallyChar int ch) {
        return ch != '"' && ch != '<' && ch != '&';
    }

    public static boolean isDirectXmlCharData(@ActuallyChar int ch) {
        return ch != '<' && ch != '&';
    }

    @NotNull
    public static Predicate<char[]> charArrayPredicate(@NotNull IntPredicate charPredicate) {
        return ca -> {
            for (int ch : ca) {
                if (charPredicate.test(ch)) {
                    return true;
                }
            }
            return false;
        };
    }

    @NotNull
    public static Predicate<CharSequence> charSequencePredicate(@NotNull IntPredicate charPredicate) {
        return string -> {
            final int length = string.length();
            for (int index = 0; index < length; index++) {
                if (charPredicate.test(string.charAt(index))) {
                    return true;
                }
            }
            return false;
        };
    }

    @NotNull
    public static Predicate<CharSequence> charSequenceValidator(@NotNull IntPredicate charValidator) {
        return string -> {
            final int length = string.length();
            for (int index = 0; index < length; index++) {
                if (!charValidator.test(string.charAt(index))) {
                    return false;
                }
            }
            return true;
        };
    }

    @NotNull
    public static Predicate<CharSequence> charSequenceValidator(@NotNull IntPredicate firstValidator, @NotNull IntPredicate nextValidator) {
        return string -> {
            final int length = string.length();
            if (length == 0 || !firstValidator.test(string.charAt(0))) {
                return false;
            }
            for (int index = 1; index < length; index++) {
                if (!nextValidator.test(string.charAt(index))) {
                    return false;
                }
            }
            return true;
        };
    }

    @NotNull
    public static Predicate<CharSequence> charSequencePredicate(int flag) {
        switch (flag) {
            case PreparedCharacteristic.CONTAINS_NULL:
                return charSequencePredicate(PredictCharacteristic::isNull);
            case PreparedCharacteristic.CONTAINS_CONTROL:
                return charSequencePredicate(Character::isISOControl);
            case PreparedCharacteristic.CONTAINS_QUOTATION_MARK:
                return charSequencePredicate(PredictCharacteristic::isQuotationMark);
            case PreparedCharacteristic.CONTAINS_APOSTROPHE:
                return charSequencePredicate(PredictCharacteristic::isApostrophe);
            case PreparedCharacteristic.CONTAINS_NON_ASCII:
                return charSequencePredicate(PredictCharacteristic::isNonAscii);
            case PreparedCharacteristic.CONTAINS_NON_BMP:
                return charSequencePredicate(PredictCharacteristic::isNonBMP);
            case PreparedCharacteristic.VALID_IDENTIFIER:
                return charSequenceValidator(Character::isJavaIdentifierStart, Character::isJavaIdentifierPart);
            case PreparedCharacteristic.DIRECT_LITERAL:
                return charSequenceValidator(PredictCharacteristic::isDirectLiteral);
            case PreparedCharacteristic.DIRECT_JSON:
                return charSequenceValidator(PredictCharacteristic::isDirectJson);
            case PreparedCharacteristic.DIRECT_XML_ATTRIBUTE_VALUE:
                return charSequenceValidator(PredictCharacteristic::isDirectXmlAttributeValue);
            case PreparedCharacteristic.DIRECT_XML_CHAR_DATA:
                return charSequenceValidator(PredictCharacteristic::isDirectXmlCharData);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int computeFirstFlags(@ActuallyChar int ch) {
        return 0; // todo
    }

    public static int computeNextFlags(@ActuallyChar int ch, int flags) {
        return 0; // todo
    }

    public static int computeFlags(@NotNull CharSequence string) {
        final int length = string.length();
        if (length == 0) {
            return PreparedCharacteristic.DIRECT_LITERAL
                    | PreparedCharacteristic.DIRECT_JSON
                    | PreparedCharacteristic.DIRECT_XML_ATTRIBUTE_VALUE
                    | PreparedCharacteristic.DIRECT_XML_CHAR_DATA;
        }
        int flags = computeFirstFlags(string.charAt(0));
        for (int index = 1; index < length; index++) {
            flags = computeNextFlags(string.charAt(index), flags);
        }
        return flags;
    }
}
