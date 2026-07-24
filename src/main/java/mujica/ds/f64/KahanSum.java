package mujica.ds.f64;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2026/1/4")
@ReferencePage(title = "Kahan's summation algorithm", href = "http://en.wikipedia.org/wiki/Kahan_summation_algorithm")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.KahanSummation")
public class KahanSum extends F64 {

    private static final long serialVersionUID = 0x56ECFD599E030064L;

    double correction;

    @Override
    public void setF64(double newValue) {
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
