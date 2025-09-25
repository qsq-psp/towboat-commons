package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;

import java.text.Collator;

@CodeHistory(date = "2023/3/30", project = "LeetInAction", name = "IntegralCollator")
@CodeHistory(date = "2023/4/18", project = "Ultramarine")
@CodeHistory(date = "2025/3/4")
public class NumberSegmentsCollator extends Collator {

    @Override
    public int compare(String source, String target) {
        return 0;
    }

    @Override
    public NumberSegmentsCollationKey getCollationKey(String source) {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
