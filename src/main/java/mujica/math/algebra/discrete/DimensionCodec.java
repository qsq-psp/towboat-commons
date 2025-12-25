package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

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
    void decode2(long in, @NotNull int[] out);

    /**
     * @param in length = 4
     */
    int encode4(@NotNull byte[] in);

    /**
     * @param out length = 4 to put method return value
     */
    void decode4(int in, @NotNull byte[] out);

    /**
     * @param in length = 8
     */
    long encode8(@NotNull byte[] in);

    /**
     * @param out length = 8 to put method return value
     */
    void decode8(long in, @NotNull byte[] out);

    /**
     * @param in length known, content not null
     */
    @NotNull
    BigInteger encodeN(@NotNull BigInteger[] in);

    /**
     * @param out length known to put method return value
     */
    void decodeN(@NotNull BigInteger in, @NotNull BigInteger[] out);
}
