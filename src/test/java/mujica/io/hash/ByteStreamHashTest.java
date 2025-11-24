package mujica.io.hash;

import io.netty.buffer.ByteBufUtil;
import mujica.io.view.ByteSequence;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2024/12/13", project = "Ultramarine")
@CodeHistory(date = "2025/4/13")
public class ByteStreamHashTest {

    @BeforeClass
    public static void initializeNetty() {
        ByteBufUtil.hexDump(new byte[0]);
    }

    static byte[] filledByteArray(int value, int length) {
        final byte[] array = new byte[length];
        Arrays.fill(array, (byte) value);
        return array;
    }

    private void caseHash(@NotNull byte[] input, @NotNull String expectedHexOutput, @NotNull BytesHash actualAlgorithm) {
        Assert.assertEquals(
                expectedHexOutput,
                actualAlgorithm.apply(input).toHexString(false)
        );
    }
    
    private void caseHash(@NotNull byte[] input, @NotNull String expectedHexOutput, @NotNull ByteBlockByteHashCore actualCore) {
        caseHash(input, expectedHexOutput, new SimpleByteBlockByteStreamHash(actualCore));
        if (actualCore instanceof ByteBlockBitHashCore) {
            caseHash(input, expectedHexOutput, new SimpleByteBlockBitStreamHash(((ByteBlockBitHashCore) actualCore)));
        }
    }

    private void caseHash(@NotNull String input, @NotNull String expectedHexOutput, @NotNull ByteBlockByteHashCore actualCore) {
        caseHash(input.getBytes(StandardCharsets.UTF_8), expectedHexOutput, actualCore);
    }

    private final FuzzyContext fc = new FuzzyContext();

    private static final int REPEAT = 30;

    private static final int SIZE = 100;
    
    private void fuzzHash(@NotNull MessageDigest expectedAlgorithm, @NotNull ByteBlockByteHashCore actualCore) {
        final List<BytesHash> actualAlgorithms = new ArrayList<>(3);
        actualAlgorithms.add(new SimpleByteBlockByteStreamHash(actualCore));
        if (actualCore instanceof ByteBlockBitHashCore) {
            actualAlgorithms.add(new SimpleByteBlockBitStreamHash(((ByteBlockBitHashCore) actualCore)));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] input = fc.nextByteArray(fc.nextInt(SIZE));
            expectedAlgorithm.reset();
            String expectedHexOutput = ByteBufUtil.hexDump(expectedAlgorithm.digest(input));
            for (BytesHash actualAlgorithm : actualAlgorithms) {
                try {
                    Assert.assertEquals(
                            expectedHexOutput,
                            actualAlgorithm.apply(input).toHexString(false)
                    );
                } catch (AssertionError e) {
                    System.out.println(actualAlgorithm);
                    throw e;
                }
            }
        }
    }

    @Test
    public void caseXxHash32() {
        final ByteBlockByteHashCore core = new XxHash32();
        caseHash("", "02cc5d05", core);
        caseHash("z", "a73026ce", core);
        caseHash("if", "ad493904", core);
        caseHash("bee", "ae7e67e9", core);
        caseHash("hack", "c795dc33", core);
        caseHash("three", "9e1247f8", core);
        caseHash("github", "9f11eae1", core);
        caseHash("uniform", "2d4686e8", core);
        caseHash("creative", "fabea83b", core);
        caseHash("$$104445*", "5dfdab3c", core);
        caseHash("-89882200/", "c5386582", core);
        caseHash("-.3397114++", "a468f9cf", core);
        caseHash("@wp::::::::;", "957edaed", core);
    }

    @Test
    public void caseMurmurHash32() {
        final ByteBlockByteHashCore core = new MurmurHash32();
        caseHash("", "00000000", core);
        caseHash("w", "ff439d1f", core);
        caseHash("OK", "275fd7ab", core);
        caseHash("TCP", "be39e3dd", core);
        caseHash("gift", "6b6405dc", core);
        caseHash("world", "fb963cfb", core);
        caseHash("victor", "401359d1", core);
        caseHash("missile", "6dc450e0", core);
        caseHash("bookmark", "fcd4b400", core);
        caseHash("&39278309", "c3b683ea", core);
        caseHash("|475405849", "748b69cd", core);
        caseHash("6 6 6 6 6 6", "6df54e99", core);
        caseHash("*oiaoiaoaio;", "89757ae4", core);
    }

    @Test
    public void caseMurmurHash128() {
        final ByteBlockByteHashCore core = new MurmurHash128();
        caseHash("", "00000000000000000000000000000000", core);
        caseHash("w", "de0591e6d17f6d9fa1c57e04a50b9088", core);
        caseHash("OK", "d2702da8185dd5d533913d767bddc05d", core);
        caseHash("TCP", "c00b5c565445c81d912f5038db481495", core);
        caseHash("gift", "f28b5712cc3855eec20c30e03c3c87f5", core);
        caseHash("world", "71c5790af0fb84eac4e4ecc371358e3a", core);
        caseHash("victor", "80c56c57d471831021206f7fca5cce8d", core);
        caseHash("missile", "3baeba07dc3b02e1b12167960d0f8d49", core);
        caseHash("bookmark", "8a2760acae712a9e60e0db88e477df46", core);
        caseHash("&39278309", "7aaebe2dbcc311315023729adb502971", core);
        caseHash("|475405849", "acb7f7cb4a4901c326319c929659fce8", core);
        caseHash("6 6 6 6 6 6", "186d5e16a73df517d4945a74fea2b5e4", core);
        caseHash("*oiaoiaoaio;", "b90882568b27e977415540ee15cbe254", core);
    }
    
    @Test
    public void caseMD2() {
        final ByteBlockByteHashCore core = new MD2();
        caseHash("", "8350e5a3e24c153df2275c9f80692773", core);
        caseHash("a", "32ec01ec4a6dac72c0ab96fb34c0b5d1", core);
        caseHash("abc", "da853b0d3f88d99b30283a69e6ded6bb", core);
        caseHash("message digest", "ab4f496bfb2a530b219ff33031fe06b0", core);
        caseHash("abcdefghijklmnopqrstuvwxyz", "4e8ddff3650292ab5a4108c3aa47940b", core);
        caseHash(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                "da33def2a42df13975352846c30338cd", core
        );
        caseHash(
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890",
                "d5976f79d83d3a0dc9806c3c66f3efd8", core
        );
    }
    
    @Test
    public void fuzzMD2() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("MD2"), new MD2());
    }
    
    @Test
    public void caseMD4() {
        final ByteBlockByteHashCore core = new MD4();
        caseHash("", "31d6cfe0d16ae931b73c59d7e0c089c0", core);
        caseHash("a", "bde52cb31de33e46245e05fbdbd6fb24", core);
        caseHash("abc", "a448017aaf21d8525fc10ae87aa6729d", core);
        caseHash("message digest", "d9130a8164549fe818874806e1c7014b", core);
        caseHash("abcdefghijklmnopqrstuvwxyz", "d79e1c308aa5bbcdeea8ed63df412da9", core);
        caseHash(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                "043f8582f241db351ce627e153e7f0e4", core
        );
        caseHash(
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890",
                "e33b4ddc9c38f2199c3e7b164fcc0536", core
        );
    }
    
    @Test
    public void caseMD5() {
        final ByteBlockByteHashCore core = new MD5();
        caseHash("", "d41d8cd98f00b204e9800998ecf8427e", core);
        caseHash("a", "0cc175b9c0f1b6a831c399e269772661", core);
        caseHash("abc", "900150983cd24fb0d6963f7d28e17f72", core);
        caseHash("message digest", "f96b697d7cb7938d525a2f31aaf161d0", core);
        caseHash("abcdefghijklmnopqrstuvwxyz", "c3fcd3d76192e4007dfb496cca67e13b", core);
        caseHash(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                "d174ab98d277d9f5a5611c2c9f419d9f", core
        );
        caseHash(
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890",
                "57edf4a22be3c955ac49da2e2107b67a", core
        );
    }
    
    @Test
    public void fuzzMD5() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("MD5"), new MD5());
    }
    
    @Test
    public void fuzzSHA1() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA-1"), new SHA1());
    }
    
    @Test
    public void caseSHA224() {
        final ByteBlockByteHashCore core = new SHA224();
        caseHash(
                filledByteArray(0x41, 1000),
                "a8d0c66b5c6fdfd836eb3c6d04d32dfe66c3b1f168b488bf4c9c66ce", core
        );
        caseHash(
                filledByteArray(0x99, 1005),
                "cb00ecd03788bf6c0908401e0eb053ac61f35e7e20a2cfd7bd96d640", core
        );
    }
    
    @Test
    public void fuzzSHA224() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA-224"), new SHA224());
    }
    
    @Test
    public void caseSHA256() {
        final ByteBlockByteHashCore core = new SHA256();
        caseHash(
                filledByteArray(0x41, 1000),
                "c2e686823489ced2017f6059b8b239318b6364f6dcd835d0a519105a1eadd6e4", core
        );
        caseHash(
                filledByteArray(0x55, 1005),
                "f4d62ddec0f3dd90ea1380fa16a5ff8dc4c54b21740650f24afc4120903552b0", core
        );
    }
    
    @Test
    public void fuzzSHA256() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA-256"), new SHA256());
    }

    @Test
    public void caseSHA384() {
        final ByteBlockByteHashCore core = new SHA384();
        caseHash(
                filledByteArray(0x41, 1000),
                "7df01148677b7f18617eee3a23104f0eed6bb8c90a6046f715c9445ff43c30d6" +
                "9e9e7082de39c3452fd1d3afd9ba0689", core
        );
        caseHash(
                filledByteArray(0x55, 1005),
                "1bb8e256da4a0d1e87453528254f223b4cb7e49c4420dbfa766bba4adba44eec" +
                "a392ff6a9f565bc347158cc970ce44ec", core
        );
    }
    
    @Test
    public void fuzzSHA384() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA-384"), new SHA384());
    }
    
    @Test
    public void caseSHA512() {
        final ByteBlockByteHashCore core = new SHA512();
        caseHash(
                new byte[1000000],
                "ce044bc9fd43269d5bbc946cbebc3bb711341115cc4abdf2edbc3ff2c57ad4b1" +
                "5deb699bda257fea5aef9c6e55fcf4cf9dc25a8c3ce25f2efe90908379bff7ed", core
        );
    }
    
    @Test
    public void fuzzSHA512() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA-512"), new SHA512());
    }
    
    @Test
    @Ignore
    public void fuzzSHA3() throws NoSuchAlgorithmException {
        fuzzHash(MessageDigest.getInstance("SHA3-224"), new SHA3(224));
        fuzzHash(MessageDigest.getInstance("SHA3-256"), new SHA3(256));
        fuzzHash(MessageDigest.getInstance("SHA3-384"), new SHA3(384));
        fuzzHash(MessageDigest.getInstance("SHA3-512"), new SHA3(512));
    }

    private void fuzzHash(@NotNull Checksum expectedAlgorithm, @NotNull ByteStreamHash actualAlgorithm) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] input = fc.nextByteArray(fc.nextInt(SIZE));
            expectedAlgorithm.reset();
            expectedAlgorithm.update(input);
            Assert.assertEquals(
                    expectedAlgorithm.getValue(),
                    actualAlgorithm.apply(ByteSequence.of(input)).getLongAll()
            );
        }
    }

    @Test
    public void fuzzBigBooleanCRC32() {
        fuzzHash(new CRC32(), BigBooleanCRC.crc32());
    }

    @Test
    public void fuzzBigBooleanCRC32C() {
        fuzzHash(new CRC32C(), BigBooleanCRC.crc32C());
    }

    @Test
    public void fuzzSimpleIntSizedCRC32() {
        fuzzHash(new CRC32(), SimpleIntSizedCRC.crc32());
    }

    @Test
    public void fuzzSimpleIntSizedCRC32C() {
        fuzzHash(new CRC32C(), SimpleIntSizedCRC.crc32C());
    }

    @Test
    public void fuzzGeneralLongSizedCRC32() {
        fuzzHash(new CRC32(), new GeneralLongSizedCRC(CrcSpec.CRC32));
    }

    @Test
    public void fuzzGeneralLongSizedCRC32C() {
        fuzzHash(new CRC32C(), new GeneralLongSizedCRC(CrcSpec.CRC32C));
    }

    @Test
    public void fuzzAdler32() {
        fuzzHash(new java.util.zip.Adler32(), new mujica.io.hash.Adler32());
    }

    private void checkLinearity(@NotNull ByteStreamHash algorithm) {
        // repeats for byte linearity
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int length = fc.nextInt(SIZE) + 2;
            int flipIndex0 = fc.nextInt(length - 1);
            int flipIndex1 = fc.nextInt(length - 1);
            if (flipIndex1 >= flipIndex0) {
                flipIndex1++;
            }
            byte[] input = fc.nextByteArray(length);
            long result0 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex0] ^= -1;
            long result1 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex1] ^= -1;
            long result2 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex0] ^= -1;
            long result3 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            Assert.assertEquals(0, result0 ^ result1 ^ result2 ^ result3);
        }
        // repeats for bit linearity
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int length = fc.nextInt(SIZE) + 1;
            int flipIndex0 = fc.nextInt(length * Byte.SIZE - 1);
            int flipIndex1 = fc.nextInt(length * Byte.SIZE - 1);
            if (flipIndex1 >= flipIndex0) {
                flipIndex1++;
            }
            byte[] input = fc.nextByteArray(length);
            long result0 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex0 >>> 3] ^= 1 << (0x7 & flipIndex0);
            long result1 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex1 >>> 3] ^= 1 << (0x7 & flipIndex1);
            long result2 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            input[flipIndex0 >>> 3] ^= 1 << (0x7 & flipIndex0);
            long result3 = algorithm.apply(ByteSequence.of(input)).getLongAll();
            Assert.assertEquals(0, result0 ^ result1 ^ result2 ^ result3);
        }
    }

    @Test
    public void checkLinearityBooleanCRC() {
        checkLinearity(BigBooleanCRC.crc32());
        checkLinearity(BigBooleanCRC.crc32C());
    }

    @Test
    public void checkLinearitySimpleIntSizedCRC() {
        checkLinearity(SimpleIntSizedCRC.crc32());
        checkLinearity(SimpleIntSizedCRC.crc32C());
    }

    @Test
    public void checkLinearityGeneralLongSizedCRC() {
        checkLinearity(new GeneralLongSizedCRC(CrcSpec.CRC32));
        checkLinearity(new GeneralLongSizedCRC(CrcSpec.CRC32C));
    }
}
