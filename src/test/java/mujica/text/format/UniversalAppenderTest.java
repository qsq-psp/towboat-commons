package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2026/1/28.
 */
@CodeHistory(date = "2026/1/28")
public class UniversalAppenderTest {

    @Test
    public void testIntAll() {
        final UniversalStringifier stringifier = new UniversalStringifierUsingStringBuilder(UniversalAppender.createIntAll());
        Assert.assertEquals("0", stringifier.apply(0));
        Assert.assertEquals("2700", stringifier.apply(2700));
        Assert.assertEquals("-30025", stringifier.apply(-30025));
        Assert.assertEquals("[]", stringifier.apply(new int[0]));
        Assert.assertEquals("[0, 0, 0, 0]", stringifier.apply(new int[4]));
        Assert.assertEquals("[3, 2, 1, -9, -8]", stringifier.apply(new int[] {3, 2, 1, -9, -8}));
        Assert.assertEquals("[[1, 0], [0, 1]]", stringifier.apply(new int[][] {{1, 0}, {0, 1}}));
        Assert.assertEquals("[[399, 109881, -2077], null]", stringifier.apply(new int[][] {{399, 109881, -2077}, null}));
    }
}
