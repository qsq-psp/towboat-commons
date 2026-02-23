package mujica.io.codec;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2026/2/19")
public class ModifiedUTF8CharsetTest {

    private static final int REPEAT = 896;

    private final RandomContext rc = new RandomContext();

    private void caseEncoderOnce(@NotNull String string) throws IOException {
        final byte[] data = string.getBytes(TowboatCharsetProvider.MUTF8);
        final int length = data.length;
        Assert.assertTrue(length < 0x10000); // 65535, maximum unsigned short
        final byte[] lengthData = new byte[length + 2];
        {
            ByteBuffer byteBuffer = ByteBuffer.wrap(lengthData);
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
            byteBuffer.putShort((short) length);
            byteBuffer.put(data);
        }
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(lengthData))) {
            Assert.assertEquals(string, dis.readUTF());
        }
    }

    private void caseDecoderOnce(@NotNull String string) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            dos.writeUTF(string);
            dos.flush();
        }
        final byte[] lengthData = bos.toByteArray();
        final ByteBuffer byteBuffer = ByteBuffer.wrap(lengthData);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        final int length = 0xffff & byteBuffer.getShort();
        Assert.assertEquals(string, new String(lengthData, byteBuffer.position(), length, TowboatCharsetProvider.MUTF8));
    }

    private void caseCodec(@NotNull String string) throws IOException {
        caseEncoderOnce(string);
        caseDecoderOnce(string);
        string = string.repeat(rc.nextInt(2, REPEAT));
        caseEncoderOnce(string);
        caseDecoderOnce(string);
    }

    @Test
    public void case1() throws IOException {
        caseCodec(" ");
        caseCodec("|-");
        caseCodec("www");
        caseCodec("JQKA");
        caseCodec("#b07219");
        caseCodec("SIGKILL");
        caseCodec("<packaging>pom</packaging>");
        caseCodec("71F772D81199D3A7E05397BE0A0AB82A");
    }

    @Test
    public void case2() throws IOException {
        caseCodec("naïve");
        caseCodec("Göteborg"); // Gothenburg
        caseCodec("Στοιχεῖα"); // Elements
        caseCodec("La Géométrie"); // geometry
        caseCodec("Les Météores"); // meteors
        caseCodec("Niccolò Fontana");
        caseCodec("Västra Götalands län");
        caseCodec("Sylvestre-François Lacroix");
        caseCodec("Algebraischen Theorie der Körper");
        caseCodec("L'Invention nouvelle en l'algèbre");
        caseCodec("Vollständige Anleitung zur Algebra");
        caseCodec("Über die Darstellung ganz willkürlicher Functionen durch Sinus-und Cosinusreihen");
    }

    @Test
    public void case3() throws IOException {
        caseCodec("啊");
        caseCodec("皮肤");
        caseCodec("生火腿");
        caseCodec("空气开关");
        caseCodec("木质压力板");
        caseCodec("重离子对撞机");
        caseCodec("在南非，一只猫鼬正坐在一块岩石上，岩石上布满了二十七亿年前雨滴留下的痕迹。");
    }
}
