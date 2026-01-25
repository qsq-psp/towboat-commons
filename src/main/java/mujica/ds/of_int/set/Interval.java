package mujica.ds.of_int.set;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2018/7/4", project = "existence", name = "BoundI")
@CodeHistory(date = "2025/3/9", name = "IntInterval")
public interface Interval {

    int getLeft();

    int getRight();
}
