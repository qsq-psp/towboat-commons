package indi.um.json.api;

import indi.um.json.reflect.ConverterFrame;
import indi.um.json.reflect.JsonConverter;

/**
 * Created on 2022/7/26.
 */
@FunctionalInterface
public interface FrameStructure {

    ConverterFrame frame(boolean isObject, JsonConverter jv);
}
