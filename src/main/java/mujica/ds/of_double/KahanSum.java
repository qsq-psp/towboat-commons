package mujica.ds.of_double;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2026/1/4")
@ReferencePage(title = "Kahan's summation algorithm", href = "http://en.wikipedia.org/wiki/Kahan_summation_algorithm")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.KahanSummation")
public class KahanSum extends PublicDoubleSlot {

    private static final long serialVersionUID = 0x56ECFD599E030064L;

    public double correction;

    @Override
    public void setDouble(double newValue) {
        correction = 0.0;
        value = newValue;
    }

    public void addDouble(double delta) {
        final double newDelta = delta - correction;
        final double newValue = value + newDelta;
        correction = newValue - value - newDelta;
        value = newValue;
    }
}
