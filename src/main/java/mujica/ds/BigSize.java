package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/5/26.
 */
@CodeHistory(date = "2025/5/26")
public interface BigSize {

    int sizeInInt();

    int sizeInExactInt() throws RuntimeException;

    int sizeInClampedInt();

    long sizeInLong();

    long sizeInExactLong() throws RuntimeException;

    long sizeInClampedLong();

    @NotNull
    BigInteger sizeInBig();
}
