package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

import java.text.Collator;

@CodeHistory(date = "2023/3/30", project = "LeetInAction", name = "IntegralCollator")
@CodeHistory(date = "2023/4/18", project = "Ultramarine")
@CodeHistory(date = "2025/3/4")
public class NumberSegmentsCollator extends Collator {

    private int radix = 10;

    public int getRadix() {
        return radix;
    }

    public void setRadix(int radix) {
        if (!(Character.MIN_RADIX <= radix && radix <= Character.MAX_RADIX)) {
            throw new IllegalArgumentException();
        }
        this.radix = radix;
    }

    @Override
    public int compare(String source, String target) {
        final NumberSegmentsCollationKey sourceKey = getCollationKey(source);
        final NumberSegmentsCollationKey targetKey = getCollationKey(target);
        final int strength = getStrength();
        if (strength <= PRIMARY) {
            return sourceKey.weakCompareTo(targetKey);
        } else if (strength <= SECONDARY) {
            return sourceKey.normalCompareTo(targetKey);
        } else {
            return sourceKey.strongCompareTo(targetKey);
        }
    }

    public int normalizedCompare(String source, String target) {
        return Integer.signum(compare(source, target));
    }

    @Override
    public NumberSegmentsCollationKey getCollationKey(String source) {
        return new NumberSegmentsCollationKey(source, radix);
    }

    @Override
    public int hashCode() {
        return radix;
    }
}
