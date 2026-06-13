package mujica.json.provided.netty;

import io.netty.handler.codec.http.HttpResponseStatus;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "HttpResponseStatusValueSerializer")
@CodeHistory(date = "2026/5/26")
public class HttpResponseStatusTransformer implements JsonContextTransformer<HttpResponseStatus> {

    @Override
    public void transform(@NotNull HttpResponseStatus in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("code");
            out.numberValue(in.code());
            out.stringKey("reason");
            out.stringValue(in.reasonPhrase());
        }
        out.closeObject();
    }
}
