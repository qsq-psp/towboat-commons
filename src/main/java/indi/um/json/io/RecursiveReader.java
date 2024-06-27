package indi.um.json.io;

import indi.um.util.ds.Index;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created in infrastructure on 2021/12/30.
 * Created on 2022/6/5.
 */
public interface RecursiveReader extends SyncReader {

    void skip(@NotNull Consumer<RecursiveReader> callback);

    @SuppressWarnings("DefaultAnnotationParam")
    String raw(@Index(inclusive = true) int fromIndex, @Index(inclusive = false) int toIndex);

    void setPosition(int pos);

    int getPosition();
}
