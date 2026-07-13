package mujica.json.provided.netty;

import io.netty.handler.codec.http.HttpScheme;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/10")
public class HttpSchemeTransformer implements JsonContextTransformer<HttpScheme> {

    public static final HttpSchemeTransformer INSTANCE = new HttpSchemeTransformer();

    @Override
    public void transform(@NotNull HttpScheme scheme, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.stringValue(scheme.name()); // name() returns AsciiString
    }
}
