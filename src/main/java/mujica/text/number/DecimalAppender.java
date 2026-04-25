package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2018/7/9", project = "existence", name = "DoubleToStringConverter")
@CodeHistory(date = "2025/2/25", name = "DecimalToStringFunction")
@CodeHistory(date = "2026/3/18")
public interface DecimalAppender extends Serializable {

    void acceptFloat(float value, @NotNull StringBuilder out);

    void acceptDouble(double value, @NotNull StringBuilder out);
}
