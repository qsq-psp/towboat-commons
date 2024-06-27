package indi.qsq.json.io;

import indi.qsq.util.value.EnumMapping;

/**
 * Created in webbiton on 2020/12/16, named JsonBlobBuilder.State.
 * Created on 2022/6/12.
 */
public interface WriterStates {

    int STATE_START = 0;
    int STATE_END = 1;
    int STATE_NEW_ARRAY = 2;
    int STATE_ARRAY = 3;
    int STATE_NEW_OBJECT = 4;
    int STATE_OBJECT = 5;
    int STATE_KEY = 6;
    int STATE_JSONP = 7;

    EnumMapping WRITER_STATE = (new EnumMapping())
            .addDefault("start", STATE_START)
            .add("end", STATE_END)
            .add("new array", STATE_NEW_ARRAY)
            .add("array", STATE_ARRAY)
            .add("new object", STATE_NEW_OBJECT)
            .add("object", STATE_OBJECT)
            .add("key", STATE_KEY)
            .add("jsonp", STATE_JSONP);
}
