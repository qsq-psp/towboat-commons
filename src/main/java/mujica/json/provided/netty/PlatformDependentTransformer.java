package mujica.json.provided.netty;

import io.netty.util.internal.PlatformDependent;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2022/3/25", project = "infrastructure", name = "PlatformDependentValue")
@CodeHistory(date = "2026/6/13")
public final class PlatformDependentTransformer implements JsonContextTransformer<Class<PlatformDependent>>, JsonStructure {

    public static final PlatformDependentTransformer INSTANCE = new PlatformDependentTransformer();

    @Override
    public void transform(@Nullable Class<PlatformDependent> ignore, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("isAndroid");
            out.booleanValue(PlatformDependent.isAndroid());
            out.key("isWindows");
            out.booleanValue(PlatformDependent.isWindows());
            out.key("isOsx");
            out.booleanValue(PlatformDependent.isOsx());
            out.key("maybeSuperUser");
            out.booleanValue(PlatformDependent.maybeSuperUser());
            out.key("javaVersion");
            out.numberValue(PlatformDependent.javaVersion());
            out.key("canEnableTcpNoDelayByDefault");
            out.booleanValue(PlatformDependent.canEnableTcpNoDelayByDefault());
            out.key("hasUnsafe");
            out.booleanValue(PlatformDependent.hasUnsafe());
            out.key("isUnaligned");
            out.booleanValue(PlatformDependent.isUnaligned());
            out.key("directBufferPreferred");
            out.booleanValue(PlatformDependent.directBufferPreferred());
            out.key("maxDirectMemory");
            out.numberValue(PlatformDependent.maxDirectMemory());
            out.key("bitMode");
            out.numberValue(PlatformDependent.bitMode());
            out.key("addressSize");
            out.numberValue(PlatformDependent.addressSize());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(PlatformDependent.class, jh, null);
    }
}
