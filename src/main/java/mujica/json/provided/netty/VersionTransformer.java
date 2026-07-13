package mujica.json.provided.netty;

import io.netty.util.Version;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@CodeHistory(date = "2022/3/25", project = "infrastructure", name = "NettyVersionValue")
@CodeHistory(date = "2026/5/25")
public class VersionTransformer implements JsonContextTransformer<Version>, JsonStructure {

    public static final VersionTransformer INSTANCE = new VersionTransformer();

    @Override
    public void transform(@NotNull Version in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("artifactId");
            out.stringValue(in.artifactId());
            out.key("artifactVersion");
            out.stringValue(in.artifactVersion());
            out.key("buildTime");
            out.numberValue(in.buildTimeMillis());
            out.key("commitTime");
            out.numberValue(in.commitTimeMillis());
            out.key("shortCommitHash");
            out.stringValue(in.shortCommitHash());
            out.key("longCommitHash");
            out.stringValue(in.longCommitHash());
            out.key("repositoryStatus");
            out.stringValue(in.repositoryStatus());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        jh.openObject();
        for (Map.Entry<String, Version> entry : Version.identify().entrySet()) {
            jh.key(entry.getKey());
            transform(entry.getValue(), jh, null);
        }
        jh.closeObject();
    }
}
