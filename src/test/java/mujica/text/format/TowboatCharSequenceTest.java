package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2023/10/20", project = "Ultramarine", name = "TowboatCharSequenceTest")
@CodeHistory(date = "2026/3/15")
public class TowboatCharSequenceTest {

    @Test
    public void caseCommonPrefixLength() {
        assertEquals(0, TowboatCharSequence.commonPrefixLength("", ""));
        assertEquals(0, TowboatCharSequence.commonPrefixLength("face", ""));
        assertEquals(0, TowboatCharSequence.commonPrefixLength("face", "book"));
        assertEquals(0, TowboatCharSequence.commonPrefixLength("+---", "---"));
        assertEquals(1, TowboatCharSequence.commonPrefixLength("utility", "user"));
        assertEquals(1, TowboatCharSequence.commonPrefixLength("append", "abandon"));
        assertEquals(2, TowboatCharSequence.commonPrefixLength("++v", "++xy"));
        assertEquals(3, TowboatCharSequence.commonPrefixLength("!!!", "!!!"));
        assertEquals(3, TowboatCharSequence.commonPrefixLength("...", "...*"));
        assertEquals(3, TowboatCharSequence.commonPrefixLength("phalanx", "phantasm"));
        assertEquals(0, TowboatCharSequence.commonPrefixLength("编码器", "报警器"));
        assertEquals(0, TowboatCharSequence.commonPrefixLength("高锰酸钾", "聚氯乙烯"));
        assertEquals(2, TowboatCharSequence.commonPrefixLength("玻璃窗", "玻璃杯"));
    }

    @Test
    public void caseCommonSuffixLength() {
        assertEquals(0, TowboatCharSequence.commonSuffixLength("", ""));
        assertEquals(0, TowboatCharSequence.commonSuffixLength("work", ""));
        assertEquals(0, TowboatCharSequence.commonSuffixLength("work", "sheet"));
        assertEquals(0, TowboatCharSequence.commonSuffixLength("pig", "piggy"));
        assertEquals(1, TowboatCharSequence.commonSuffixLength("people", "taste"));
        assertEquals(2, TowboatCharSequence.commonSuffixLength("lonely", "suddenly"));
        assertEquals(3, TowboatCharSequence.commonSuffixLength("lonely", "lovely"));
        assertEquals(3, TowboatCharSequence.commonSuffixLength(":/:", ":/:"));
        assertEquals(4, TowboatCharSequence.commonSuffixLength("enable", "formidable"));
        assertEquals(4, TowboatCharSequence.commonSuffixLength("pipette", "etiquette"));
        assertEquals(5, TowboatCharSequence.commonSuffixLength("pharmacology", "seismology"));
        assertEquals(0, TowboatCharSequence.commonSuffixLength("大熊猫", "海南菠萝"));
        assertEquals(1, TowboatCharSequence.commonSuffixLength("三文鱼", "鲤鱼"));
        assertEquals(2, TowboatCharSequence.commonSuffixLength("垃圾", "可回收垃圾"));
    }

    @Test
    public void casePrefixKMP() {
        assertArrayEquals(new int[] {0, 0, 0, 1, 2, 3, 0}, TowboatCharSequence.prefixKMP("abcabcd"));
        assertArrayEquals(new int[] {0, 1, 0, 1, 2, 2, 3}, TowboatCharSequence.prefixKMP("aabaaab"));
    }

    @Test
    public void casePrefixZ() {
        assertArrayEquals(new int[] {0, 4, 3, 2, 1}, TowboatCharSequence.prefixZ("aaaaa"));
        assertArrayEquals(new int[] {0, 2, 1, 0, 2, 1, 0}, TowboatCharSequence.prefixZ("aaabaab"));
        assertArrayEquals(new int[] {0, 0, 1, 0, 3, 0, 1}, TowboatCharSequence.prefixZ("abacaba"));
    }
}
