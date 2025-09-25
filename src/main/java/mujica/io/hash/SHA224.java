package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/11/9", project = "Ultramarine")
@CodeHistory(date = "2024/12/20", project = "OSHI", name = "SHA224Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "FIPS PUB 180-4", href = "https://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf")
public class SHA224 extends SHA256 {

    private static final long serialVersionUID = 0x23e14119331fc5a4L;

    private static final int[] INITIAL = {
            0xc1059ed8,
            0x367cd507,
            0x3070dd17,
            0xf70e5939,
            0xffc00b31,
            0x68581511,
            0x64f98fa7,
            0xbefa4fa4
    };

    @Override
    public int resultBytes() {
        return 28;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public SHA224 clone() {
        final SHA224 that = new SHA224();
        cloneArray(this.out, that.out);
        that.blockCount = this.blockCount;
        return that;
    }

    @Override
    public void start() {
        cloneArray(INITIAL, out);
        blockCount = 0;
    }

    @Override
    public int intLength() {
        return 7;
    }

    @Override
    public int getInt(int index) {
        if (index >= 7) {
            throw new IndexOutOfBoundsException();
        }
        return out[index];
    }

    @Override
    public String toString() {
        return "SHA224";
    }
}
