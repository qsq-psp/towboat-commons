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
public class InitializationBufferedReaderTest {

    /**
     * @param in input at last, for it contains multiple lines
     */
    private void assertInitialization(@NotNull String out, @NotNull String in) {
        final JsonStringWriter writer = new JsonStringWriter();
        try (Reader reader = new StringReader(in)) {
            InitializationBufferedReader testedReader = new InitializationBufferedReader(reader);
            testedReader.read(writer);
            assertEquals(out, writer.get());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void case00() {
        assertInitialization("{}", "");
        assertInitialization("{}", "\n");
        assertInitialization("{}", "\r\n");
    }

    @Test
    public void case01() {
        assertInitialization("{\"port\":8080}", "port = 8080");
        assertInitialization("{\"level\":-3}", "level = -3");
        assertInitialization("{\"channel.buffer.limit\":16384}", "channel.buffer.limit=16384");
    }

    @Test
    public void case02() {
        assertInitialization("{\"name\":\"John\"}", "name = \"John\"");
        assertInitialization("{\"class\":\"java.io.IOException\"}", "class = \"java.io.IOException\"");
        assertInitialization("{\"split.sign\":\"=\"}", "split.sign = \"=\"");
    }

    @Test
    public void case03() {
        assertInitialization(
                "{\"month\":\"Feb\",\"date\":20}",
                "month = \"Feb\"\n"
                + "; abbr\n"
                + "date = 20\n"
                + "; start from 1"
        );
        assertInitialization(
                "{\"index\":37,\"tag\":\"\"}",
                "index = 37\r\n"
                + "; start from 0\r\n"
                + "tag = \"\"\r\n"
                + "; no tag"
        );
    }

    @Test
    public void case04() {
        assertInitialization(
                "{\"style\":{\"color\":\"#fecc73\",\"borderWidth\":2}}",
                "[style]\n"
                + "color = \"#fecc73\"\n"
                + "borderWidth = 2"
        );
        assertInitialization(
                "{\"charset\":\"GBK\",\"server\":{\"address\":\"197.2.133.209\",\"port\":7003}}",
                "charset = \"GBK\"\n"
                + "[server]\n"
                + "address = \"197.2.133.209\"\n"
                + "port = 7003"
        );
    }

    @Test
    public void case05() {
        assertInitialization(
                "{\"common\":{\"application.directory\":\"/application\",\"application.dispatcher.catchException\":true},"
                + "\"product\":{\"resources.database.master.driver\":\"pdo_mysql\",\"resources.database.master.hostname\":\"127.0.0.1\",\"resources.database.master.port\":3306},"
                + "\"develop\":{\"resources.database.slave.database\":\"test\",\"resources.database.slave.username\":\"root\",\"resources.database.slave.password\":\"123456\",\"resources.database.slave.charset\":\"UTF8\"},"
                + "\"test\":{}}",
                "[common]\n"
                + "application.directory = \"/application\"\n"
                + "application.dispatcher.catchException = TRUE\n"
                + "[product]\n"
                + "resources.database.master.driver = \"pdo_mysql\"\n"
                + "resources.database.master.hostname = \"127.0.0.1\"\n"
                + "resources.database.master.port = 3306\n"
                + "[develop]\n"
                + "resources.database.slave.database = \"test\"\n"
                + "resources.database.slave.username = \"root\"\n"
                + "resources.database.slave.password = \"123456\"\n"
                + "resources.database.slave.charset = \"UTF8\"\n"
                + "[test]"
        );
    }
}
