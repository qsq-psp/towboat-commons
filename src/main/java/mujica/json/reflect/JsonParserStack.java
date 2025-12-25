package mujica.json.reflect;

import mujica.ds.generic.list.TruncateList;
import mujica.json.entity.ClassifiedJsonConsumer;
import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/20")
class JsonParserStack extends TruncateList<JsonParserFrame> implements ClassifiedJsonConsumer {
    @Override
    public void openArray() {

    }

    @Override
    public void closeArray() {

    }

    @Override
    public void openObject() {

    }

    @Override
    public void closeObject() {

    }

    @Override
    public void stringKey(@NotNull String key) {

    }

    @Override
    public void stringKey(@NotNull FastString key) {

    }

    @Override
    public void jsonValue(@NotNull String value) {

    }

    @Override
    public void nullValue() {

    }

    @Override
    public void booleanValue(boolean value) {

    }

    @Override
    public void numberValue(long value) {

    }

    @Override
    public void numberValue(double value) {

    }

    @Override
    public void numberValue(@NotNull FastNumber value) {

    }

    @Override
    public void stringValue(@NotNull String value) {

    }

    @Override
    public void stringValue(@NotNull FastString value) {

    }
}
