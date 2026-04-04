package mujica.io.codec;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2026/2/26.
 */
@CodeHistory(date = "2026/2/26")
@SuppressWarnings("SpellCheckingInspection")
public class UTF8PushPullDecoderTest {

    private static final int REPEAT = 94;

    private final RandomContext rc = new RandomContext();

    private void caseEncode(@NotNull String string) {
        final CharBuffer in = CharBuffer.wrap(string);
        final byte[] data = new byte[3 * string.length()];
        final ByteBuffer out = ByteBuffer.wrap(data);
        final UTF8PushPullEncoder encoder = new UTF8PushPullEncoder();
        while (in.hasRemaining()) {
            if (rc.nextBoolean()) {
                encoder.push(in.get(), out);
            } else {
                out.put(encoder.pull(in));
            }
        }
        if (rc.nextBoolean()) {
            encoder.finishPush(out);
        } else {
            while (encoder.finishPullHasNext()) {
                out.put(encoder.finishPullAsByte());
            }
        }
        Assert.assertEquals(string, new String(data, 0, out.position(), StandardCharsets.UTF_8));
    }

    private void caseDecode(@NotNull String string) {
        final byte[] data = string.getBytes(StandardCharsets.UTF_8);
        final ByteBuffer in = ByteBuffer.wrap(data);
        final StringBuilder out = new StringBuilder();
        final PushPullDecoder decoder = new UTF8PushPullDecoder();
        while (in.hasRemaining()) {
            if (rc.nextBoolean()) {
                decoder.push(in.get(), out);
            } else {
                out.append(decoder.pull(in));
            }
        }
        if (rc.nextBoolean()) {
            decoder.finishPush(out);
        } else {
            while (decoder.finishPullHasNext()) {
                out.append(decoder.finishPullAsChar());
            }
        }
        Assert.assertEquals(string, out.toString());
    }

    private void caseCodec(@NotNull String string) {
        caseEncode(string);
        caseDecode(string);
        string = string.repeat(rc.nextInt(2, REPEAT));
        caseEncode(string);
        caseDecode(string);
    }

    @Test
    public void case1() {
        caseCodec("\r");
        caseCodec("\t!");
        caseCodec("C\r\n");
        caseCodec("[01]");
        caseCodec("{world}");
        caseCodec("impl<'a>");
        caseCodec("101 Switching Protocols");
        caseCodec("<packaging>jar</packaging>");
        caseCodec("QFN1cHByZXNzV2FybmluZ3MoIlNwZWxsQ2hlY2tpbmdJbnNwZWN0aW9uIik=");
    }

    @Test
    public void case2() {
        caseCodec("Ferrös");
        caseCodec("abčabçy̆");
        caseCodec("Grüße, Jürgen");
        caseCodec("α is alpha, β is beta");
        caseCodec("Per Martin-Löf");
        caseCodec("Zażółć gęślą jaźń");
        caseCodec("ὈΔΥΣΣΕΎΣ");
        caseCodec("παραβολή"); // parabola
        caseCodec("Une valeur peut être soit une chaîne de caractères entre guillemets");
    }

    @Test
    public void case3() {
        caseCodec("ℏ"); // Planck constant over two pi
        caseCodec("亮");
        caseCodec("700℃");
        caseCodec("自然");
        caseCodec("平行线");
        caseCodec("资治通鉴");
        caseCodec("Löwe 老虎 Léopard Gepardi");
        caseCodec("桃花潭水深千尺，不及汪伦送我情。");
        caseCodec("토근들의 어떤 쌍 사이에 공백을 삽입할수 있다.");
        caseCodec("オブジェクトは、順序付けされない名前/値のペアのセットです。");
    }

    @Test
    public void case4() {
        caseCodec("\ud869\udf00\ud869\udf0d"); // CJK Extension C
        caseCodec("\ud86a\udc40\ud86a\udddf\ud86a\udf65草堂春睡足，窗外日迟迟。"); // CJK Extension C
        caseCodec("\ud86c\uddb3\ud86c\uddda\ud86c\udea5\ud86d\udddf"); // CJK Extension C
        caseCodec("\ud86d\udf32\ud86d\udf3f,"); // CJK Extension C
        caseCodec("\ud86d\udf66\ud86d\udfde;"); // CJK Extension D
        caseCodec("\ud86e\udc2c\ud86e\udc72"); // CJK Extension E
        caseCodec("\ud86e\udd78\ud86e\udd71\ud86e\ude6e"); // CJK Extension E
        caseCodec("\ud86e\ude5d\ud86e\udf42\ud86e\udf43\ud86e\udf8a"); // CJK Extension E
        caseCodec("\ud872\udce1\ud872\udd02."); // CJK Extension E
        caseCodec("\ud873\udeb0\ud873\udeb1"); // CJK Extension F
        caseCodec("\ud873\udf05\ud873\udf66\ud874\udc4e\t"); // CJK Extension F
        caseCodec("\ud875\udedc\ud877\ude21\ud877\ude36\ud87a\udd2b"); // CJK Extension F
        caseCodec("{\ud87a\udd36\ud87a\udf61}"); // CJK Extension F
        caseCodec("\ud880\udc0c\ud880\udc40/"); // CJK Extension G
        caseCodec("\ud880\udc55\ud880\udd7b\ud880\uddb1"); // CJK Extension G
        caseCodec("\ud881\udc3c\ud881\udc2d\ud881\udc43\ud882\udd5e"); // CJK Extension G
        caseCodec("\ud884\udd3f\ud884\udd59"); // CJK Extension G
        caseCodec("\ud884\udd47\ud884\udd50\ud884\ude47"); // CJK Extension G
        caseCodec("\ud884\ude9a\ud884\udec3\ud884\udf11"); // CJK Extension G
        caseCodec("\ud884\udfaa\ud885\ude75!"); // CJK Extension H
        caseCodec("\ud887\udd81\ud887\udd9a\ud888\udc8d\n"); // CJK Extension H
        caseCodec("\ud887\udc7a\ud887\udc83\ud888\udebe投鼠忌器"); // CJK Extension H
        caseCodec("[\ud888\udf8a\ud886\uddb7]"); // CJK Extension H
        caseCodec("-\ud87a\udff0\ud87a\udff7"); // CJK Extension I
        caseCodec("+\ud87b\udce8\ud87b\udda1\ud87b\ude5b"); // CJK Extension I
        caseCodec("\ud83d\ude16");
        caseCodec("\ud834\udd1emusic");
        caseCodec("\ud83d\udc4c is OK");
        caseCodec("给我打电话\ud83e\udd19");
        caseCodec("\ud83c\udf83\u2764\ufe0f"); // the last is a variation selector
        caseCodec("\u0030\ufe0f\u20e3\ud83c\udf80"); // the second is a variation selector
        caseCodec("\ud83d\ude4f双手合十（希望人没事）");
        caseCodec("\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc66\u200d\ud83d\udc66"); // family: woman, woman, boy, boy
        caseCodec("\ud83e\uddf7\ud83e\uddfe\ud83c\udf46\ud83c\udf40\ud83e\udd55\ud83d\udeb2");
        caseCodec("\ud83e\udd21\ud83d\udc79\ud83d\udc7a\ud83d\udc7b\ud83d\udc7d\ud83d\udc7e\ud83e\udd16\ud83d\ude3a");
    }
}
