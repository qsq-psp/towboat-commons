package indi.qsq.json.api;

import indi.qsq.json.reflect.ConverterFrame;
import indi.qsq.json.reflect.JsonConverter;

/**
 * Created on 2022/7/26.
 */
@FunctionalInterface
public interface FrameStructure {

    ConverterFrame frame(boolean isObject, JsonConverter jv);
}
