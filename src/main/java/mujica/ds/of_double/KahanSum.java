package mujica.ds.of_double;

import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;

/**
 * Created on 2026/1/4.
 */
@ReferencePage(title = "Kahan's summation algorithm", href = "http://en.wikipedia.org/wiki/Kahan_summation_algorithm")
@ReferenceCode(groupId = "it.unimi.dsi", artifactId = "dsiutils", version = "2.7.4", fullyQualifiedName = "it.unimi.dsi.util.KahanSummation")
public class KahanSum extends PublicDoubleSlot {

    public double correction;

    @Override
    public double setDouble(double newValue) {
        correction = 0.0;
        return super.setDouble(newValue);
    }

    public void addDouble(double delta) {
        double newDelta = delta - correction;
        double newValue = value + newDelta;
        correction = newValue - value - newDelta;
        value = newValue;
    }
}
