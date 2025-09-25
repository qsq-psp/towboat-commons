package mujica.text.word;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Created on 2022/8/14, named IdentifierFormat.
 * Recreated on 2025/3/6.
 * For phase split and word join.
 */
public enum PhraseStyle {

    AUTO, // auto split, not for join
    UNDERSCORE,
    DASH, // aka kebab-case
    SPACE,
    DOT,
    SLASH,
    ONE_COLON, // :
    TWO_COLONS, // ::
    PARENTHESIS, // aka round brackets, ()
    SQUARE_BRACKETS, // []
    PASCAL, // aka PascalCase, for java class names
    CAMEL, // aka camelCase, for java variables
    URI_SEGMENT,
    CHEMICAL_COMPOUND,
    ABBR; // only first letter, not for split

    private static final long serialVersionUID = 0xeecd8ef5ec5ea382L;

    @NotNull
    public Iterable<String> spliterator(@NotNull String string) {
        if (string.isEmpty()) {
            return Collections.emptyList();
        }
        switch (this) {
            default:
            case AUTO:
                return SplitBy.autoWords(string);
            case UNDERSCORE:
                return new SplitBy.SeparatorChar(string, '_');
            case DASH:
                return new SplitBy.SeparatorChar(string, '-');
            case SPACE:
                return new SplitBy.SeparatorChar(string, ' ');
            case DOT:
                return new SplitBy.SeparatorChar(string, '.');
            case SLASH:
                return new SplitBy.SeparatorChar(string, '/');
            case ONE_COLON:
                return new SplitBy.SeparatorChar(string, ':');
            case TWO_COLONS:
                return new SplitBy.SeparatorCharSequence(string, "::");
            case CHEMICAL_COMPOUND:
                return new SplitBy.BeforeUpper(string);
            case PASCAL:
            case CAMEL:
                return new SplitBy.Camel(string); // Camel is more complicated than BeforeUpper, it considers ABBR (continuous upper)
            case ABBR:
                return new SplitBy.BetweenCodePoints(string);
        }
    }

    @NotNull
    public WordJoin join() {
        switch (this) {
            case AUTO:
            case CHEMICAL_COMPOUND:
                return WordJoin.CONCAT;
            case UNDERSCORE:
                return WordJoin.CaseByChar.UNDERSCORE;
            case DASH:
                return WordJoin.CaseByChar.DASH;
            default:
            case SPACE:
                return WordJoin.ByChar.SPACE;
            case DOT:
                return WordJoin.ByChar.DOT;
            case SLASH:
                return WordJoin.ByChar.SLASH;
            case ONE_COLON:
                return WordJoin.ByChar.COLON;
            case TWO_COLONS:
                return WordJoin.ByCharSequence.TWO_COLONS;
            case PASCAL:
                return WordJoin.Case.PASCAL;
            case CAMEL:
                return WordJoin.Case.CAMEL;
            case ABBR:
                return WordJoin.Abbreviation.INSTANCE;
        }
    }
}
