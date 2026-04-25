package mujica.json.io;

import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2026/4/23")
public class JsonWriterTest {

    private void caseFlags(@NotNull JsonStructure in, @NotNull String out, int flags) {
        {
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            writer.setFlags(flags);
            in.json(writer);
            Assert.assertEquals(out, writer.getString());
        }
        {
            JsonStringBufferWriter writer = new JsonStringBufferWriter();
            writer.setFlags(flags);
            in.json(writer);
            Assert.assertEquals(out, writer.getString());
        }
    }

    @Test
    public void caseInfinityToNull() {
        caseFlags(jh -> jh.numberValue(Float.NEGATIVE_INFINITY), "null", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
        caseFlags(jh -> jh.numberValue(Float.POSITIVE_INFINITY), "null", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
        caseFlags(jh -> jh.numberValue(Double.NEGATIVE_INFINITY), "null", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
        caseFlags(jh -> jh.numberValue(Double.POSITIVE_INFINITY), "null", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
        caseFlags(jh -> {
            jh.openArray();
            {
                jh.booleanValue(true);
                jh.numberValue(0);
                jh.numberValue(Double.POSITIVE_INFINITY);
            }
            jh.closeArray();
        }, "[true,0,null]", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
        caseFlags(jh -> {
            jh.openObject();
            {
                jh.stringKey("x");
                jh.numberValue(-2);
                jh.stringKey("y");
                jh.numberValue(Double.NEGATIVE_INFINITY);
            }
            jh.closeObject();
        }, "{\"x\":-2,\"y\":null}", JsonWriter.ConfigFlags.INFINITY_TO_NULL);
    }

    @Test
    public void caseNaNToNull() {
        caseFlags(jh -> jh.numberValue(Float.NaN), "null", JsonWriter.ConfigFlags.NAN_TO_NULL);
        caseFlags(jh -> jh.numberValue(Double.NaN), "null", JsonWriter.ConfigFlags.NAN_TO_NULL);
        caseFlags(jh -> {
            jh.openArray();
            {
                jh.numberValue(Float.NaN);
                jh.stringValue("maze");
                jh.nullValue();
            }
            jh.closeArray();
        }, "[null,\"maze\",null]", JsonWriter.ConfigFlags.NAN_TO_NULL);
        caseFlags(jh -> {
            jh.openObject();
            {
                jh.stringKey("weight");
                jh.numberValue(Double.NaN);
                jh.stringKey("serial");
                jh.numberValue(9907L);
            }
            jh.closeObject();
        }, "{\"weight\":null,\"serial\":9907}", JsonWriter.ConfigFlags.NAN_TO_NULL);
    }

    private void caseSkippedValue(@NotNull JsonStructure in, @NotNull String out) {
        {
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            in.json(writer);
            Assert.assertEquals(out, writer.getString());
        }
        {
            JsonStringBufferWriter writer = new JsonStringBufferWriter();
            in.json(writer);
            Assert.assertEquals(out, writer.getString());
        }
        /*
        {
            JsonByteBufWriter writer = new JsonByteBufWriter(Unpooled.buffer());
            try {
                in.json(writer);
                Assert.assertEquals(out, writer.content().toString(StandardCharsets.UTF_8));
            } finally {
                writer.release();
            }
        }
        //*/
    }

    @Test
    public void caseSkippedValue() {
        caseSkippedValue(JsonHandler::skippedValue, "");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.skippedValue();
            }
            jh.closeArray();
        }, "[]");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.skippedValue();
                jh.skippedValue();
            }
            jh.closeArray();
        }, "[]");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.booleanValue(false);
                jh.skippedValue();
            }
            jh.closeArray();
        }, "[false]");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.skippedValue();
                jh.booleanValue(false);
            }
            jh.closeArray();
        }, "[false]");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.numberValue(5);
                jh.skippedValue();
                jh.numberValue(4);
            }
            jh.closeArray();
        }, "[5,4]");
        caseSkippedValue(jh -> {
            jh.openArray();
            {
                jh.skippedValue();
                jh.stringValue("Mary");
                jh.stringValue("Jim");
                jh.skippedValue();
            }
            jh.closeArray();
        }, "[\"Mary\",\"Jim\"]");
    }

    @Test
    public void caseSkippedKeyValue1() {
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("dummy");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("error");
                jh.skippedValue();
                jh.stringKey("wrong");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("valid");
                jh.stringValue("queue");
                jh.stringKey("fake");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{\"valid\":\"queue\"}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("transparent");
                jh.skippedValue();
                jh.stringKey("solid");
                jh.emptyArrayValue();
            }
            jh.closeObject();
        }, "{\"solid\":[]}");
    }

    @Test
    public void caseSkippedKeyValue2() {
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("\\");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("..\"");
                jh.skippedValue();
                jh.stringKey("\"!");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("valid");
                jh.stringValue("queue");
                jh.stringKey("\"fake\"");
                jh.skippedValue();
            }
            jh.closeObject();
        }, "{\"valid\":\"queue\"}");
        caseSkippedValue(jh -> {
            jh.openObject();
            {
                jh.stringKey("\\\"transparent\\\"");
                jh.skippedValue();
                jh.stringKey("solid");
                jh.emptyArrayValue();
            }
            jh.closeObject();
        }, "{\"solid\":[]}");
    }
}
