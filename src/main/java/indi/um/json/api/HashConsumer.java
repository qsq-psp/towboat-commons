package indi.um.json.api;

import org.jetbrains.annotations.NotNull;

/**
 * Created in webbiton on 2021/5/3, named HashHandler.
 * Recreated on 2022/3/26.
 */
public interface HashConsumer extends JsonConsumer {

    @Override
    void openArray();

    @Override
    void closeArray();

    void openLoop();

    void closeLoop();

    void openSet();

    void closeSet();

    @Override
    void openObject();

    @Override
    void closeObject();

    @Override
    void key(@NotNull String key);

    @Override
    void jsonValue(@NotNull CharSequence json);
}
