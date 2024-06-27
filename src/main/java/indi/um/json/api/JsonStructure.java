package indi.um.json.api;

import org.jetbrains.annotations.NotNull;

/**
 * Created in webbiton on 2020/12/16, named JsonStructure.
 * Recreated in infrastructure on 2021/9/19.
 * Recreated on 2021/6/24.
 *
 * The standard accompany method is void toJsonEntries(@NotNull JsonConsumer jc);
 */
@FunctionalInterface
public interface JsonStructure {

    void toJson(@NotNull JsonConsumer jc);
}
