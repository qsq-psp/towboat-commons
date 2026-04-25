package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2023/10/6")
@CodeHistory(date = "2026/4/25")
public class DimensionCodecTest {

    @Test
    public void caseEncodeUnsignedTriangle() {
        final DimensionCodec codec = UnsignedTriangle.INSTANCE;
        Assert.assertEquals(0L, codec.encode2(new int[] {0, 0}));
        Assert.assertEquals(1L, codec.encode2(new int[] {1, 0}));
        Assert.assertEquals(2L, codec.encode2(new int[] {0, 1}));
        Assert.assertEquals(3L, codec.encode2(new int[] {2, 0}));
        Assert.assertEquals(4L, codec.encode2(new int[] {1, 1}));
        Assert.assertEquals(5L, codec.encode2(new int[] {0, 2}));
        Assert.assertEquals(-3L, codec.encode2(new int[] {-1, -2}));
        Assert.assertEquals(-2L, codec.encode2(new int[] {-2, -1}));
        Assert.assertEquals(-1L, codec.encode2(new int[] {-1, -1}));
    }

    @Test
    public void caseEncodeUnsignedSquare() {
        final DimensionCodec codec = UnsignedSquare.INSTANCE;
        Assert.assertEquals(0L, codec.encode2(new int[] {0, 0}));
        Assert.assertEquals(1L, codec.encode2(new int[] {1, 0}));
        Assert.assertEquals(2L, codec.encode2(new int[] {1, 1}));
        Assert.assertEquals(3L, codec.encode2(new int[] {0, 1}));
        Assert.assertEquals(4L, codec.encode2(new int[] {2, 0}));
        Assert.assertEquals(5L, codec.encode2(new int[] {2, 1}));
        Assert.assertEquals(6L, codec.encode2(new int[] {2, 2}));
        Assert.assertEquals(7L, codec.encode2(new int[] {1, 2}));
        Assert.assertEquals(8L, codec.encode2(new int[] {0, 2}));
        Assert.assertEquals(-1L, codec.encode2(new int[] {0, -1}));
    }
}
