package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantInterface;

import java.io.IOException;

/**
 * Created on 2025/4/17.
 */
@CodeHistory(date = "2025/4/17")
@ConstantInterface
public interface Base64StreamingEncoder extends Base64StreamingCodec {

    int FLAG_STOP_ON_NULL           = 0x0001;
    int FLAG_STOP_ON_LINE_FEED      = 0x0002;
    int FLAG_STOP_ON_FLUSH          = 0x0004; // no use in InputStream
    int FLAG_STOP_ON_CLOSE          = 0x0008; // no use in InputStream

    int FLAG_CONSUME_NULL           = 0x0010;
    int FLAG_CONSUME_LINE_FEED      = 0x0020;
    int FLAG_CONSUME_FLUSH          = 0x0040; // no use in InputStream
    int FLAG_CONSUME_CLOSE          = 0x0080; // no use in InputStream

    int FLAG_URL                    = 0x0100; // use '-' and '_' instead of '+' and '/'

    int CHAR_62                     = '+';
    int CHAR_63                     = '/';
    int URL_CHAR_62                 = '-';
    int URL_CHAR_63                 = '_';

    void setFlags(int newFlags);

    boolean hasFlag(int testFlag);

    boolean stop() throws IOException;
}
