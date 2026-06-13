package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import javax.swing.table.AbstractTableModel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;

@CodeHistory(date = "2026/6/1")
public class CharsetTableUI extends PanelUI {

    @CodeHistory(date = "2026/6/1")
    static class CachedCharsetInfo {

        @NotNull
        final Charset charset;

        CachedCharsetInfo(@NotNull Charset charset) {
            super();
            this.charset = charset;
        }
    }

    @CodeHistory(date = "2026/6/1")
    static class CharsetTableModel extends AbstractTableModel {

        static int COLUMN_MODULE = 0;

        static int COLUMN_PACKAGE = 1;

        static int COLUMN_CLASS = 2;

        static int COLUMN_NAME = 3;

        static int COLUMN_ALIAS = 4;

        static int COLUMN_AVERAGE_BYTES_PER_CHAR = 5;

        static int COLUMN_MAX_BYTES_PER_CHAR = 6;

        static int COLUMN_AVERAGE_CHARS_PER_BYTE = 7;

        static int COLUMN_MAX_CHARS_PER_BYTE = 8;

        static int COLUMN_COUNT = 9;

        @NotNull
        final ArrayList<CachedCharsetInfo> charsetList;

        CharsetTableModel() {
            super();
            final SortedMap<String, Charset> map = Charset.availableCharsets();
            charsetList = new ArrayList<>(map.size());
            for (Charset charset : map.values()) {
                charsetList.add(new CachedCharsetInfo(charset));
            }
        }

        @Override
        public int getRowCount() {
            return charsetList.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;
        }
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }
}
