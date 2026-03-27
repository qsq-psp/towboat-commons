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

@CodeHistory(date = "2026/3/6")
public class JsonObjectInputStreamTest {

    private static final int REPEAT = 19;

    private static final int SIZE = 12;

    private final FuzzyContext fc = new FuzzyContext();

    private void readIntArray1D(@NotNull String in, @NotNull int[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.assertArrayEquals(out, is.readIntArray1D());
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

    private void readBadIntArray1D(@NotNull String in) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.fail(Arrays.toString(is.readIntArray1D()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseReadBadIntArray1D() {
        readBadIntArray1D("0");
        readBadIntArray1D("-300");
        readBadIntArray1D("50000%");
        readBadIntArray1D("()");
        readBadIntArray1D("{}");
        readBadIntArray1D("(3)");
        readBadIntArray1D("{4}");
        readBadIntArray1D("[5 6]");
        readBadIntArray1D("[5E6]\t");
        readBadIntArray1D("  [0.5]");
        readBadIntArray1D("[-2 -3]");
        readBadIntArray1D("[1; 3; 20]");
        readBadIntArray1D("[3-6-29]");
        readBadIntArray1D(" [true]");
        readBadIntArray1D(" [\"30\"]");
        readBadIntArray1D("\t['30']\t");
        readBadIntArray1D("[[2000, 4000, 6000]]");
        readBadIntArray1D("2000, 4000, 6000]");
        readBadIntArray1D("[2000, 4000, 6000");
        readBadIntArray1D("\r\n2000, 4000, 6000]");
        readBadIntArray1D("[2000, 4000, 6000\r\n");
        readBadIntArray1D("[, ]");
        readBadIntArray1D("[-222, -555, -999, ]");
        readBadIntArray1D("[, -222, -555, -999, ]");
        readBadIntArray1D("[, -222, -555, -999]");
    }

    private void readIntArray1D(@NotNull String in, int flags, @NotNull int[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.assertArrayEquals(out, is.readIntArray1D());
        }
    }

    @Test
    public void caseConfigReadIntArray1D() throws IOException {
        readIntArray1D("[40088]", JsonReader.FLAG_LEADING_COMMA, new int[] {40088});
        readIntArray1D("[, 40088]", JsonReader.FLAG_LEADING_COMMA, new int[] {40088});
        readIntArray1D("[40088]", JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[40088, ]", JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[40088]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[, 40088]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[40088, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[, 40088, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {40088});
        readIntArray1D("[3396, 71540]", JsonReader.FLAG_LEADING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[, 3396, 71540]", JsonReader.FLAG_LEADING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[3396, 71540]", JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[3396, 71540, ]", JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[3396, 71540]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[, 3396, 71540]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[3396, 71540, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("[, 3396, 71540, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {3396, 71540});
        readIntArray1D("\n[, ]\t\t\t\n", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[] {});
    }

    private void readBadIntArray1D(@NotNull String in, int flags) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.fail(Arrays.toString(is.readIntArray1D()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseConfigBadReadIntArray1D() {
        readBadIntArray1D("[40088, ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray1D("[, 40088, ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray1D("[, 40088]", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray1D("[, 40088, ]\r\n", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray1D("[\r\n96, , ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray1D("[\n, , 208\n]\n", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray1D("\t[, ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray1D("[, ]\t", JsonReader.FLAG_TRAILING_COMMA);
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
            Assert.assertArrayEquals(out, is.readIntArray2D());
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

    private void readBadIntArray2D(@NotNull String in) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.fail(Arrays.toString(is.readIntArray2D()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseReadBadIntArray2D() {
        readBadIntArray2D("7-1");
        readBadIntArray2D("4, 5, 6");
        readBadIntArray2D("[0.5]2000");
        readBadIntArray2D("\t303[]  ");
        readBadIntArray2D(" [2 2, 8 9]");
        readBadIntArray2D("[2, 2; 8, 9]");
        readBadIntArray2D("[2 -2, 8 9]    ");
        readBadIntArray2D("[2, 2; -8, 9]");
        readBadIntArray2D("-[[]]-");
        readBadIntArray2D("-[[2, 32, 432]] ");
        readBadIntArray2D("[1]\t[2]\t");
        readBadIntArray2D("][\r\n][");
        readBadIntArray2D("[[4] [4]]");
        readBadIntArray2D("[[1 1] [2 2]]");
        readBadIntArray2D(" [[1, 1] [2, 2]]");
        readBadIntArray2D("[[1, 1],, [2, 2]]\r\n");
        readBadIntArray2D("[[1, 1], [2, 2], [3]");
        readBadIntArray2D("[[1, 1], [2, 2], [3%]]");
        readBadIntArray2D("    [[1, 1], [2, -]]");
        readBadIntArray2D("\r\n[[1, 1], [_]]");
        readBadIntArray2D("[, ]");
        readBadIntArray2D("[[, ]]");
        readBadIntArray2D("[[-222, -555, -999, ]]");
        readBadIntArray2D("[[, -222, -555, -999, ]]");
        readBadIntArray2D("[[, -222, -555, -999]]");
    }

    private void readIntArray2D(@NotNull String in, int flags, @NotNull int[][] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.assertArrayEquals(out, is.readIntArray2D());
        }
    }

    @Test
    public void caseConfigReadIntArray2D() throws IOException {
        readIntArray2D("[, [20], [3, 4], [-5, -6, 1024]]", JsonReader.FLAG_LEADING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[, \n20], [3, 4], [-5, -6, 1024]]", JsonReader.FLAG_LEADING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[20], [, 3, 4], [, -5, -6, 1024]]\r\n", JsonReader.FLAG_LEADING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[20], [3, 4], [-5, -6, 1024], ]", JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[20], [3, 4], [-5, -6, 1024, ], ]", JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[20], [3, 4, ], [-5, -6, 1024]]", JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[20], [, 3, 4\t, ], [-5, -6, 1024]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[[, 20], [3, 4], [-5, -6, 1024, ]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("\t\t[, [20, ], [3, 4]\r\n, [, -5, -6, 1024], ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{20}, {3, 4}, {-5, -6, 1024}});
        readIntArray2D("[, [5, 4, -3, -2], [], [404, 403]]", JsonReader.FLAG_LEADING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[[, 5, 4, -3, -2], \t[], [404, 403]]", JsonReader.FLAG_LEADING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[[5, 4, -3, -2, ], [], [404, 403]]", JsonReader.FLAG_TRAILING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[[5, 4, -3, -2], [], [404, 403], ]", JsonReader.FLAG_TRAILING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[[, 5, 4, -3, -2, ], []\r\n, [404, 403]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[, [5, 4, -3, -2], [], [, 404, 403], ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{5, 4, -3, -2}, {}, {404, 403}});
        readIntArray2D("[, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {});
        readIntArray2D("[[, ]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new int[][] {{}});
    }

    private void readBadIntArray2D(@NotNull String in, int flags) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.fail(Arrays.deepToString(is.readIntArray2D()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseConfigBadReadIntArray2D() {
        readBadIntArray2D("[[143, ]]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray2D("[[, 143], ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray2D("[[9, 10], ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray2D("[\n[, 299]]", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[, [299, ]]", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[, [8, 13]]", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[, , \r\n[8, 13]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[[8, 13], , ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[[8, , 13]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[, [8, , 13], ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[[1, 2]\t, , [3, 4]]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[, ]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray2D("[[, ]]", JsonReader.FLAG_LEADING_COMMA);
        readBadIntArray2D("[, ]", JsonReader.FLAG_TRAILING_COMMA);
        readBadIntArray2D("[[, ]]", JsonReader.FLAG_TRAILING_COMMA);
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

    private void readStringArray(@NotNull String in, @NotNull String[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.assertArrayEquals(out, is.readStringArray());
        }
    }

    @Test
    public void caseReadStringArray() throws IOException {
        readStringArray("[      ]", new String[] {});
        readStringArray("[\"\"]", new String[] {""});
        readStringArray("[\"\",\"A\"]", new String[] {"", "A"});
        readStringArray("[\"@\",\"~~\"]", new String[] {"@", "~~"});
        readStringArray("[\"()\",\"[]\",\"{}\"]", new String[] {"()", "[]", "{}"});
        readStringArray("[\"which\", \"\\r\\n\", \"003\"]", new String[] {"which", "\r\n", "003"});
    }

    private void readBadStringArray(@NotNull String in) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            Assert.fail(Arrays.toString(is.readStringArray()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseReadBadStringArray() {
        readBadStringArray("word");
        readBadStringArray("'word'");
        readBadStringArray("\"word\"");
        readBadStringArray("(bullet)");
        readBadStringArray("[bullet]");
        readBadStringArray("{bullet}");
        readBadStringArray("{\tbullet\t");
        readBadStringArray("(\"window\")");
        readBadStringArray("[\"window\", \t-3]");
        readBadStringArray("[\"window\", \"queen\", true\t\t]");
        readBadStringArray(" [\"band\", \"sand\", []]");
        readBadStringArray("[[], \"soap\", \"fast\"]\r\n");
        readBadStringArray("[\"A");
        readBadStringArray("[\"A\"");
        readBadStringArray("[\"A\",");
        readBadStringArray("[\"A\", ");
        readBadStringArray("\"A\"]");
        readBadStringArray(", \"A\"]");
        readBadStringArray("\"A\", \"B\"\n");
        readBadStringArray("[, ]");
        readBadStringArray("[\"PET\", \"RAT\", \"GOD\", ]");
        readBadStringArray("[, \"PET\", \"RAT\", \"GOD\", ]");
        readBadStringArray("[, \"PET\", \"RAT\", \"GOD\"]");
    }

    private void readStringArray(@NotNull String in, int flags, @NotNull String[] out) throws IOException {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.assertArrayEquals(out, is.readStringArray());
        }
    }

    @Test
    public void caseConfigReadStringArray() throws IOException {
        readStringArray("[, \"\"]", JsonReader.FLAG_LEADING_COMMA, new String[] {""});
        readStringArray("[\"\", ]", JsonReader.FLAG_TRAILING_COMMA, new String[] {""});
        readStringArray("[, \"\", ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new String[] {""});
        readStringArray("[\"\"]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new String[] {""});
        readStringArray("[, \"AC\", \"DC\"]", JsonReader.FLAG_LEADING_COMMA, new String[] {"AC", "DC"});
        readStringArray("[\"AC\", \"DC\", ]", JsonReader.FLAG_TRAILING_COMMA, new String[] {"AC", "DC"});
        readStringArray("[, \"AC\", \"DC\", ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new String[] {"AC", "DC"});
        readStringArray("[\"AC\", \"DC\"]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new String[] {"AC", "DC"});
        readStringArray("[, ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA, new String[] {});
    }

    private void readBadStringArray(@NotNull String in, int flags) {
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            Assert.fail(Arrays.toString(is.readStringArray()));
        } catch (Exception ignore) {} // AssertionError is not caught
    }

    @Test
    public void caseConfigBadReadStringArray() {
        readBadStringArray("[, ]", JsonReader.FLAG_LEADING_COMMA);
        readBadStringArray("[\"valve\", ]", JsonReader.FLAG_LEADING_COMMA);
        readBadStringArray("[, \"valve\", ]", JsonReader.FLAG_LEADING_COMMA);
        readBadStringArray("[, \"valve\"]", JsonReader.FLAG_TRAILING_COMMA);
        readBadStringArray("[, ]", JsonReader.FLAG_TRAILING_COMMA);
        readBadStringArray("[, \"valve\", ]", JsonReader.FLAG_TRAILING_COMMA);
        readBadStringArray("[, , ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadStringArray("[\"book\", , ]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
        readBadStringArray("[, , \"use\"]", JsonReader.FLAG_LEADING_COMMA | JsonReader.FLAG_TRAILING_COMMA);
    }
}
