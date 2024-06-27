package indi.qsq.json.io;

import indi.qsq.json.api.DatabaseColumnType;
import indi.qsq.util.ds.Index;
import indi.qsq.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2022/10/4.
 */
public class IndexedColumn implements Serializable {

    private static final long serialVersionUID = 0x6BD78D514B1D125DL;

    final TypedString key;

    @NotNull
    final DatabaseColumnType columnType;

    @Index(start = 1)
    final int columnIndex;

    public IndexedColumn(TypedString key, @NotNull DatabaseColumnType columnType, @Index(start = 1) int columnIndex) {
        super();
        this.key = key;
        this.columnType = columnType;
        this.columnIndex = columnIndex;
    }
}
