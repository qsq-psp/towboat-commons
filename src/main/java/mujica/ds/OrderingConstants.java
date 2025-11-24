package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantInterface;

@CodeHistory(date = "2025/11/20")
@ConstantInterface
public interface OrderingConstants {

    int STRICT_DESCENDING   = 1;
    int EQUAL               = 1 << 1;
    int STRICT_ASCENDING    = 1 << 2;
    int NAN                 = 1 << 3;
}
