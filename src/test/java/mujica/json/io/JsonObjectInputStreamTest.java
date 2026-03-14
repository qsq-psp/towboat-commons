package mujica.json.io;

import mujica.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created on 2026/3/6.
 */
@CodeHistory(date = "2026/3/6")
public class JsonObjectInputStreamTest {

    private static final int REPEAT = 19;

    private static final int SIZE = 12;

    private final FuzzyContext fc = new FuzzyContext();

    private void readIntArray1D(@NotNull String in, @NotNull int[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.assertArrayEquals(out, is.readIntArray1());
        }
    }

    @Test
    public void caseReadIntArray1D() throws IOException {
        readIntArray1D(" []", new int[] {});
        readIntArray1D("[30] ", new int[] {30});
        readIntArray1D("[2,1,66]", new int[] {2, 1, 66});
        readIntArray1D(" [500 \r\n]", new int[] {500});
        readIntArray1D(" \n[\n78220]", new int[] {78220});
        readIntArray1D("[-504,-3,-2,-0]", new int[] {-504, -3, -2, 0});
        readIntArray1D("[ 2, -7, -1, 9000]", new int[] {2, -7, -1, 9000});
    }

    @Test
    public void fuzzReadIntArray1D() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int[] expect = fc.nextIntArray(fc.nextInt(SIZE));
            readIntArray1D(Arrays.toString(expect), expect);
        }
    }

    private void readIntArray2D(@NotNull String in, @NotNull int[][] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.assertArrayEquals(out, is.readIntArray2());
        }
    }

    @Test
    public void caseReadIntArray2D() throws IOException {
        readIntArray2D("[]\r\n", new int[][] {});
        readIntArray2D("[ [ ] ]", new int[][] {{}});
        readIntArray2D("[[0],[0],[0]]", new int[][] {{0}, {0}, {0}});
        readIntArray2D("\r\n\r\n[[-3],[2007],[-69940]]", new int[][] {{-3}, {2007}, {-69940}});
        readIntArray2D("[[4, 5], [3, 6]]", new int[][] {{4, 5}, {3, 6}});
        readIntArray2D("[[2025]\t, [5, 5]\t, [16, 40, 22]\t]\t", new int[][] {{2025}, {5, 5}, {16, 40, 22}});
        readIntArray2D("[\n[8, 8, 8, 8, 8, 160000]\n]", new int[][] {{8, 8, 8, 8, 8, 160000}});
    }

    @Test
    public void fuzzReadIntArray2D() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            int[][] expect = new int[size][];
            for (int index = 0; index < size; index++) {
                expect[index] = fc.nextIntArray(fc.nextInt(SIZE));
            }
            readIntArray2D(Arrays.deepToString(expect), expect);
        }
    }

    private void caseReadStringArray(@NotNull String in, @NotNull String[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.assertArrayEquals(out, is.readStringArray());
        }
    }

    @Test
    public void caseReadStringArray() throws IOException {
        caseReadStringArray("[      ]", new String[] {});
        caseReadStringArray("[\"\"]", new String[] {""});
        caseReadStringArray("[\"\",\"A\"]", new String[] {"", "A"});
        caseReadStringArray("[\"@\",\"~~\"]", new String[] {"@", "~~"});
        caseReadStringArray("[\"()\",\"[]\",\"{}\"]", new String[] {"()", "[]", "{}"});
        caseReadStringArray("[\"which\", \"\\r\\n\", \"003\"]", new String[] {"which", "\r\n", "003"});
    }
}
