package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/11/7", project = "UltraIO")
@CodeHistory(date = "2024/11/9", project = "Ultramarine")
@CodeHistory(date = "2024/12/21", project = "OSHI", name = "SHA384Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "FIPS PUB 180-4", href = "https://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf")
public class SHA384 extends SHA512 {

    private static final long serialVersionUID = 0x5a379bc7bdb9d278L;

    @Override
    public int resultBytes() {
        return 48;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public SHA384 clone() {
        final SHA384 that = new SHA384();
        cloneArray(this.out, that.out);
        that.blockCount = this.blockCount;
        return that;
    }

    @Override
    public void start() {
        System.arraycopy(INITIAL, 8, out, 0, 8);
        blockCount = 0;
    }

    @Override
    public int longLength() {
        return 6;
    }

    @Override
    public long getLong(int index) {
        if (index >= 6) {
            throw new IndexOutOfBoundsException();
        }
        return out[index];
    }

    @Override
    public String toString() {
        return "SHA384";
    }
}
