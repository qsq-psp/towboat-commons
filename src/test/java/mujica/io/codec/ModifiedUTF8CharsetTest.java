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

    private static final int REPEAT = 424;

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
        caseCodec("ゼンゼロ");
        caseCodec("木质压力板");
        caseCodec("(๑•́ ₃ •̀๑)");
        caseCodec("重离子对撞机");
        caseCodec("⁽⁽ଘ( ˊᵕˋ )ଓ⁾⁾");
        caseCodec("《关于费尔巴哈的提纲》");
        caseCodec("一、白炽：火焰，灯，碳弧，石灰光；");
        caseCodec("群馬のほのかを誘って奥多摩の御岳山へ");
        caseCodec("2005年的第23届“量子结构中的空间与时间”索尔维会议");
        caseCodec("华盛顿州的LIGO干涉仪则能听到哥伦比亚河上每年春季大坝的泄洪声");
        caseCodec("流感的全名叫influenza，该词的本义就来源于最初的超自然影响力的解释。");
        caseCodec("我们可以使用 vis-viva 方程（机械能守恒）来估计行星的轨道和速度之间的关系");
        caseCodec("在南非，一只猫鼬正坐在一块岩石上，岩石上布满了二十七亿年前雨滴留下的痕迹。");
        caseCodec("能级分裂大小正比于磁场大小（塞曼效应），或正比于电场强度平方（斯塔克效应）。");
        caseCodec("很多常见的“感冒药”并不直接作用于病毒本身，而是调节人体自身的生命活动以达到缓解症状的效果"); // length should be less than 50
    }

    @Test
    public void case4() throws IOException {
        caseCodec("\ud869\udf00\ud869\udf0d"); // CJK Extension C
        caseCodec("\ud86a\udc40\ud86a\udddf\ud86a\udf65"); // CJK Extension C
        caseCodec("\ud86c\uddb3\ud86c\uddda\ud86c\udea5\ud86d\udddf"); // CJK Extension C
        caseCodec("\ud86d\udf32\ud86d\udf3f"); // CJK Extension C
        caseCodec("\ud86d\udf66\ud86d\udfde"); // CJK Extension D
        caseCodec("\ud86e\udc2c\ud86e\udc72"); // CJK Extension E
        caseCodec("\ud86e\udd78\ud86e\udd71\ud86e\ude6e"); // CJK Extension E
        caseCodec("\ud86e\ude5d\ud86e\udf42\ud86e\udf43\ud86e\udf8a"); // CJK Extension E
        caseCodec("\ud872\udce1\ud872\udd02"); // CJK Extension E
        caseCodec("\ud873\udeb0\ud873\udeb1"); // CJK Extension F
        caseCodec("\ud873\udf05\ud873\udf66\ud874\udc4e"); // CJK Extension F
        caseCodec("\ud875\udedc\ud877\ude21\ud877\ude36\ud87a\udd2b"); // CJK Extension F
        caseCodec("\ud87a\udd36\ud87a\udf61"); // CJK Extension F
        caseCodec("\ud880\udc0c\ud880\udc40"); // CJK Extension G
        caseCodec("\ud880\udc55\ud880\udd7b\ud880\uddb1"); // CJK Extension G
        caseCodec("\ud881\udc3c\ud881\udc2d\ud881\udc43\ud882\udd5e"); // CJK Extension G
        caseCodec("\ud884\udd3f\ud884\udd59"); // CJK Extension G
        caseCodec("\ud884\udd47\ud884\udd50\ud884\ude47"); // CJK Extension G
        caseCodec("\ud884\ude9a\ud884\udec3\ud884\udf11"); // CJK Extension G
        caseCodec("\ud884\udfaa\ud885\ude75"); // CJK Extension H
        caseCodec("\ud887\udd81\ud887\udd9a\ud888\udc8d"); // CJK Extension H
        caseCodec("\ud887\udc7a\ud887\udc83\ud888\udebe"); // CJK Extension H
        caseCodec("\ud888\udf8a\ud886\uddb7"); // CJK Extension H
        caseCodec("\ud87a\udff0\ud87a\udff7"); // CJK Extension I
        caseCodec("\ud87b\udce8\ud87b\udda1\ud87b\ude5b"); // CJK Extension I
        caseCodec("\u26bd\ud83c\udfb1");
        caseCodec("\ud83d\ude07微笑天使");
        caseCodec("\ud83e\udd23笑得满地打滚");
        caseCodec("\ud83c\udf83\u2764\ufe0f"); // the last is a variation selector
        caseCodec("\u0030\ufe0f\u20e3\ud83c\udf80"); // the second is a variation selector
        caseCodec("\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc66\u200d\ud83d\udc66"); // family: woman, woman, boy, boy
        caseCodec("\ud83d\ude23\ud83d\ude1e\ud83d\ude13\ud83d\ude29\ud83d\ude2b\ud83d\ude24");
        caseCodec("\ud83e\udd21\ud83d\udc79\ud83d\udc7a\ud83d\udc7b\ud83d\udc7d\ud83d\udc7e\ud83e\udd16\ud83d\ude3a");
        caseCodec("\ud83c\udfd8\ufe0f\ud83c\udfda\ufe0f\ud83c\udfe0\ud83c\udfe1\ud83c\udfe2\ud83c\udfe3\ud83c\udfe4\ud83c\udfe5\ud83c\udfe6");
    }
}
