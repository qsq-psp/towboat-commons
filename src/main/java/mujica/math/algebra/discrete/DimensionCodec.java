package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created in Ultramarine on 2023/10/6.
 * Recreated on 2025/2/28.
 */
@CodeHistory(date = "2023/10/6", project = "Ultramarine")
@CodeHistory(date = "2025/2/28")
public interface DimensionCodec {

    boolean vectorSigned();

    boolean codeSigned();

    /**
     * @param in length = 2
     */
    long encode2(@NotNull int[] in);

    /**
     * @param out length = 2 to put method return value
     */
    void decode2(long code, @NotNull int[] out);

    /**
     * @param in length = 4
     */
    int encode4(@NotNull byte[] in);

    /**
     * @param out length = 4 to put method return value
     */
    void decode4(int code, @NotNull byte[] out);

    /**
     * @param in length = 8
     */
    long encode8(@NotNull byte[] in);

    /**
     * @param out length = 8 to put method return value
     */
    void decode8(long code, @NotNull byte[] out);

    /**
     * @param in length known, content not null
     */
    @NotNull
    BigInteger encodeN(@NotNull BigInteger[] in);

    /**
     * @param out length known to put method return value
     */
    void decodeN(@NotNull BigInteger code, @NotNull BigInteger[] out);
}
