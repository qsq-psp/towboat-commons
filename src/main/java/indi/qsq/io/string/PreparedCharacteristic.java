package indi.qsq.io.string;

/**
 * Created on 2024/7/2.
 */
public interface PreparedCharacteristic {

    /**
     * U+0000, \0
     */
    int CONTAINS_NULL = 0x00000001;

    /**
     * https://util.unicode.org/UnicodeJsps/list-unicodeset.jsp?a=%5B%3ACC%3A%5D
     * General category "Cc" in the Unicode specification.
     * [#x00-#x1F] | [#x7F-#x9F]
     */
    int CONTAINS_CONTROL = 0x00000002;

    /**
     * U+0022, "
     */
    int CONTAINS_QUOTATION_MARK = 0x00000004;

    /**
     * U+0027, '
     */
    int CONTAINS_APOSTROPHE = 0x00000008;

    /**
     * cp >= 0x80
     */
    int CONTAINS_NON_ASCII = 0x00000010;

    /**
     * cp >= 0x10000
     */
    int CONTAINS_NON_BMP = 0x00000020;

    /**
     * [not used] {@link Character::isJavaIdentifierStart} JavaLetter ::= '$' | [A-Z] | "_" | [a-z] | ...
     * JavaLetter ::= [A-Z] | "_" | [a-z]
     * {@link Character::isJavaIdentifierPart} JavaLetterOrDigit ::= JavaLetter | [0-9]
     * IdentifierChars ::= JavaLetter (JavaLetterOrDigit)*
     */
    int VALID_IDENTIFIER = 0x00000100;

    /**
     * Name is used as element tag name and attribute key
     * NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     * NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
     * Name ::= NameStartChar (NameChar)*
     */
    int VALID_XML_NAME = 0x00000200;

    /**
     * Do not contains quotation mark (") and backslash (\)
     */
    int DIRECT_LITERAL = 0x00010000;

    /**
     * Do not contains quotation mark ("), backslash (\), and control characters
     */
    int DIRECT_JSON = 0x00020000;

    /**
     * Do not contains quotation mark ("), less-than sign (<), and ampersand (&)
     */
    int DIRECT_XML_ATTRIBUTE_VALUE = 0x00040000;

    /**
     * Do not contains less-than sign (<) and ampersand (&)
     */
    int DIRECT_XML_CHAR_DATA = 0x00080000;
}
