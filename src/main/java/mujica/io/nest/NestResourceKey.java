package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/8")
public interface NestResourceKey {

    @CodeHistory(date = "2025/10/8")
    class Gzip implements NestResourceKey {

        @NotNull
        public final String name;

        public Gzip(@NotNull String name) {
            super();
            this.name = name;
        }
    }

    @CodeHistory(date = "2025/11/8")
    class GzipMain extends Gzip {

        public GzipMain(@NotNull String name) {
            super(name);
        }
    }
}
// *.tar https://www.gnu.org/software/tar/manual/html_node/Standard.html
// *.zip https://pkwaredownloads.blob.core.windows.net/pkware-general/Documentation/APPNOTE-6.3.9.TXT
