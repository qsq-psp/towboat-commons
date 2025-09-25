package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/5/24")
public interface DataStructure extends HealthAware, Cloneable, Serializable {

    @NotNull
    DataStructure duplicate();

    @NotNull
    String summaryToString();

    @NotNull
    String detailToString();
}
