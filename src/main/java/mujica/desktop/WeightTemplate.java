package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

@CodeHistory(date = "2026/10/3")
public class WeightTemplate implements Serializable {

    private static final long serialVersionUID = 0x8A2F7DE672AD5C85L;

    final float[] weights;

    final boolean reverseLine;

    WeightTemplate(float[] weights, boolean reverseLine) {
        super();
        this.weights = weights;
        this.reverseLine = reverseLine;
    }

    public WeightTemplate(int columnCount, boolean reverseLine) {
        this(createWeightArray(columnCount), reverseLine);
    }

    @NotNull
    private static float[] createWeightArray(int columnCount) {
        if (columnCount <= 0) {
            throw new IllegalArgumentException();
        }
        final float[] weights = new float[2 * columnCount];
        Arrays.fill(weights, 1.0f);
        return weights;
    }

    public int getColumnCount() {
        return weights.length >> 1;
    }

    public float getShrinkWeight(int columnIndex) {
        return weights[columnIndex << 1];
    }

    public float getGrowWeight(int columnIndex) {
        return weights[(columnIndex << 1) | 1];
    }

    public void setShrinkWeight(int columnIndex, float value) {
        if (value >= 0.0f) {
            weights[columnIndex << 1] = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGrowWeight(int columnIndex, float value) {
        if (value >= 0.0f) {
            weights[(columnIndex << 1) | 1] = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setShrinkWeights(float value) {
        if (value >= 0.0f) {
            int length = weights.length;
            for (int index = 0; index < length; index += 2) {
                weights[index] = value;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGrowWeights(float value) {
        if (value >= 0.0f) {
            int length = weights.length;
            for (int index = 1; index < length; index += 2) {
                weights[index] = value;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @CodeHistory(date = "2026/10/4")
    static class Info {

        transient Component component;
    }

    @CodeHistory(date = "2026/10/4")
    static class Active {

        @NotNull
        final Info[] cache;

        Active(@NotNull WeightTemplate template) {
            super();
            cache = createInfoArray(template.getColumnCount());
        }

        @NotNull
        private static Info[] createInfoArray(int length) {
            final Info[] array = new Info[length];
            for (int index = 0; index < length; index++) {
                array[index] = new Info();
            }
            return array;
        }
    }
}
