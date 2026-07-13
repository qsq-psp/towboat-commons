package mujica.json.provided.netty;

import io.netty.handler.codec.http.HttpHeaders;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@CodeHistory(date = "2026/5/29")
public class HttpHeadersTransformer implements JsonContextTransformer<HttpHeaders> {

    @Override
    public void transform(@NotNull HttpHeaders map, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        for (Map.Entry<String, String> entry : map) {
            out.key(entry.getKey());
            out.stringValue(entry.getValue());
        }
        out.closeObject();
    }
}
