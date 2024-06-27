package indi.um.json.io;

import indi.um.json.api.JsonConsumer;
import indi.um.util.text.HexCase;

/**
 * Created in infrastructure on 2021/12/30, named JsonWriter.
 * Created on 2022/6/4, named Writer.
 */
public interface Writer extends JsonConsumer, WriterStates, HexCase {

    void setHexCase(boolean upper);

    void setMantissa(int count);

    void setIndent(int count);
}
