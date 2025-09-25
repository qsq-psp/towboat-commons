package mujica.io.stream;

import java.io.DataInput;

/**
 * Created on 2025/9/15.
 */
public interface LimitedDataInput extends DataInput {

    long getRemaining();

    long setRemaining(long newRemaining);
}
