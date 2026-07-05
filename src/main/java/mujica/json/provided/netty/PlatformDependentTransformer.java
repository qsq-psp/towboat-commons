package mujica.json.provided.netty;

import io.netty.util.internal.PlatformDependent;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/3/25", project = "infrastructure", name = "PlatformDependentValue")
@CodeHistory(date = "2026/6/13")
public class PlatformDependentTransformer implements JsonContextTransformer<Class<PlatformDependent>>, JsonStructure {

    public static final PlatformDependentTransformer INSTANCE = new PlatformDependentTransformer();

    @Override
    public void transform(Class<PlatformDependent> ignore, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("isAndroid");
            out.booleanValue(PlatformDependent.isAndroid());
            out.stringKey("isWindows");
            out.booleanValue(PlatformDependent.isWindows());
            out.stringKey("isOsx");
            out.booleanValue(PlatformDependent.isOsx());
            out.stringKey("maybeSuperUser");
            out.booleanValue(PlatformDependent.maybeSuperUser());
            out.stringKey("javaVersion");
            out.numberValue(PlatformDependent.javaVersion());
            out.stringKey("canEnableTcpNoDelayByDefault");
            out.booleanValue(PlatformDependent.canEnableTcpNoDelayByDefault());
            out.stringKey("hasUnsafe");
            out.booleanValue(PlatformDependent.hasUnsafe());
            out.stringKey("isUnaligned");
            out.booleanValue(PlatformDependent.isUnaligned());
            out.stringKey("directBufferPreferred");
            out.booleanValue(PlatformDependent.directBufferPreferred());
            out.stringKey("maxDirectMemory");
            out.numberValue(PlatformDependent.maxDirectMemory());
            out.stringKey("bitMode");
            out.numberValue(PlatformDependent.bitMode());
            out.stringKey("addressSize");
            out.numberValue(PlatformDependent.addressSize());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(PlatformDependent.class, jh, null);
    }
}
