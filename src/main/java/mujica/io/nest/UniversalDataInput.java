package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigInteger;

@CodeHistory(date = "2025/10/8")
public interface UniversalDataInput {

    void readFully(@NotNull byte[] array) throws IOException;

    void readFully(@NotNull byte[] array, int offset, int length) throws IOException;

    void skipFully(int length) throws IOException;

    byte readSignedByte() throws IOException;

    int readUnsignedByte() throws IOException;

    short readIntelSignedShort() throws IOException;

    short readNetworkSignedShort() throws IOException;

    int readIntelUnsignedShort() throws IOException;

    int readNetworkUnsignedShort() throws IOException;

    char readIntelChar() throws IOException;

    char readNetworkChar() throws IOException;

    int readIntelSignedInt() throws IOException;

    int readNetworkSignedInt() throws IOException;

    long readIntelUnsignedInt() throws IOException;

    long readNetworkUnsignedInt() throws IOException;

    long readIntelSignedLong() throws IOException;

    long readNetworkSignedLong() throws IOException;

    @NotNull
    BigInteger readIntelUnsignedLong() throws IOException;

    @NotNull
    BigInteger readNetworkUnsignedLong() throws IOException;

    @NotNull
    String readAscii(int length) throws IOException;

    @NotNull
    String readUtf8(int byteLength) throws IOException;

    @NotNull
    String readTrimmedAscii(int length) throws IOException;

    @NotNull
    String readTrimmedUtf8(int byteLength) throws IOException;

    @NotNull
    String readIntelUnicode(int charLength) throws IOException;

    @NotNull
    String readNetworkUnicode(int charLength) throws IOException;

    @NotNull
    byte[] readFully(int length) throws IOException;

    @NotNull
    byte[] readZeroTerminatedBytes() throws IOException;

    @NotNull
    String readZeroTerminatedAscii() throws IOException;

    @NotNull
    String readZeroTerminatedUtf8() throws IOException;
}
