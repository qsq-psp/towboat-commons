package mujica.ds.of_double;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/7/3", project = "existence", name = "HasDoubleValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/20")
public interface DoubleSlot {

    @ReferencePage(title = "MAX_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER")
    long MAX_SAFE_LONG = (1L << 53) - 1;

    @ReferencePage(title = "MIN_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MIN_SAFE_INTEGER")
    long MIN_SAFE_LONG = -MAX_SAFE_LONG;

    @ReferencePage(title = "Number.EPSILON", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/EPSILON")
    double EPSILON = Math.ulp(1.0);

    double getDouble();

    double setDouble(double newValue);

    default double setDouble(@NotNull Number newValue) {
        return setDouble(newValue.doubleValue());
    }

    default double setDouble(@NotNull DoubleSlot newValue) {
        return setDouble(newValue.getDouble());
    }

    static void exchange(@NotNull DoubleSlot a, @NotNull DoubleSlot b) {
        a.setDouble(b.setDouble(a.getDouble()));
    }
}
