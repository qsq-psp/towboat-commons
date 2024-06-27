package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.util.ds.Index;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created on 2022/10/4.
 */
public class ResultSetReader implements RecursiveReader {

    final ResultSet resultSet;

    final List<IndexedColumn> columns;

    private int pos;

    public ResultSetReader(ResultSet resultSet, List<IndexedColumn> columns) {
        super();
        this.resultSet = resultSet;
        this.columns = columns;
    }

    @Override
    public void config(int config) {
        // pass
    }

    @Override
    public void read(@NotNull JsonConsumer jc) {
        final int limit = 2 * columns.size();
        try {
            jc.openArray();
            while (resultSet.next()) {
                jc.openObject();
                for (pos = 0; pos < limit; pos++) {
                    IndexedColumn column = columns.get(pos >> 1);
                    if ((pos & 0x1) == 0) {
                        jc.key(column.key);
                    } else {
                        column.columnType.get(resultSet, column.columnIndex, jc);
                    }
                }
                jc.closeObject();
            }
            jc.closeArray();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void skip(@NotNull Consumer<RecursiveReader> callback) {
        pos = (pos + 2) & ~0x1;
        callback.accept(this);
    }

    @Override
    @SuppressWarnings("DefaultAnnotationParam")
    public String raw(@Index(inclusive = true) int fromIndex, @Index(inclusive = false) int toIndex) {
        final JsonStringWriter jc = new JsonStringWriter();
        if ((fromIndex & 0x1) == 0 && fromIndex + 1 == toIndex) {
            IndexedColumn column = columns.get(fromIndex >> 1);
            try {
                column.columnType.get(resultSet, column.columnIndex, jc);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return jc.get();
        } else {
            jc.openObject();
            jc.closeObject();
            return jc.sb.substring(1, jc.sb.length() - 1);
        }
    }

    @Override
    public void setPosition(int pos) {
        this.pos = pos;
    }

    @Override
    public int getPosition() {
        return pos;
    }
}
