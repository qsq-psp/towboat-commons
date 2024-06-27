package indi.qsq.json.io;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created on 2023/10/17.
 */
public class PropertiesBufferedReaderTest {

    /**
     * @param in input at last, for it contains multiple lines
     */
    private void assertProperties(@NotNull String out, @NotNull String in) {
        final JsonStringWriter writer = new JsonStringWriter();
        try (Reader reader = new StringReader(in)) {
            PropertiesBufferedReader testedReader = new PropertiesBufferedReader(reader);
            testedReader.read(writer);
            assertEquals(out, writer.get());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void case0() {
        assertProperties("{}", "");
        assertProperties("{}", "\n");
        assertProperties("{}", "\r\n");
    }
}
