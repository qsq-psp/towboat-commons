package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

import java.text.CollationKey;

@CodeHistory(date = "2023/3/30", project = "LeetInAction", name = "IntegralCollationKey")
@CodeHistory(date = "2023/4/18", project = "Ultramarine")
@CodeHistory(date = "2025/3/4")
public class NumberSegmentsCollationKey extends CollationKey {

    protected NumberSegmentsCollationKey(String source) {
        super(source);
    }

    @Override
    public int compareTo(CollationKey target) {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
