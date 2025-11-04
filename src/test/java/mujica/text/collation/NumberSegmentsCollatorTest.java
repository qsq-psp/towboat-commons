package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2025/10/15.
 */
@CodeHistory(date = "2025/10/15")
@SuppressWarnings("SpellCheckingInspection")
public class NumberSegmentsCollatorTest {

    @Test
    public void caseToZero10() {
        Assert.assertEquals("0sdnqnaa0nr", NumberSegmentsCollationKey.toZero("8sdnqnaa7nr", 10));
        Assert.assertEquals("dbyjpcu0zsf", NumberSegmentsCollationKey.toZero("dbyjpcu0zsf", 10));
        Assert.assertEquals("0plxnncbt0", NumberSegmentsCollationKey.toZero("7plxnncbt72", 10));
        Assert.assertEquals("t0n0hqf", NumberSegmentsCollationKey.toZero("t7713n5hqf", 10));
    }
}
