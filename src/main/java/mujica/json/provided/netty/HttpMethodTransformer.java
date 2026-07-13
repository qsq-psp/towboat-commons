package mujica.json.provided.netty;

import io.netty.handler.codec.http.HttpMethod;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2022/9/6", project = "Ultramarine", name = "HttpMethodValueSerializer")
@CodeHistory(date = "2026/5/29")
public class HttpMethodTransformer implements JsonContextTransformer<HttpMethod> {

    @Override
    public void transform(@NotNull HttpMethod method, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.stringValue(method.name());
    }
}
