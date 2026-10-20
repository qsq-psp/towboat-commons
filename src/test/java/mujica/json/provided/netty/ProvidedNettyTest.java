package mujica.json.provided.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import mujica.json.handler.EarlyKeyCheckAdapter;
import mujica.json.io.JsonIndentStringBuilderWriter;
import mujica.json.io.JsonStringWriter;
import mujica.json.reflect.JsonContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@CodeHistory(date = "2026/6/7")
public class ProvidedNettyTest {

    private static final JsonContext CONTEXT = new JsonContext();

    @BeforeClass
    public static void initContext() {
        CONTEXT.loadProvidedNetty().loadBasic();
    }

    private void caseObject(@NotNull String name, @NotNull Object object) throws IOException {
        final Path currentPath = Path.of("").toAbsolutePath();
        final Path targetPath = Path.of("target", "json-provided").toAbsolutePath();
        if (!(Files.isDirectory(targetPath) && targetPath.startsWith(currentPath))) {
            Assert.fail("target directory");
        }
        final JsonStringWriter jsonWriter = (new JsonIndentStringBuilderWriter()).setIndent("    ");
        CONTEXT.transform(object, new EarlyKeyCheckAdapter<>(jsonWriter, string -> !string.isBlank()));
        try (Writer writer = Files.newBufferedWriter(targetPath.resolve(name + ".json"), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            writer.write(jsonWriter.getString());
            writer.flush();
        }
    }

    private void caseObject(@NotNull Object object) throws IOException {
        caseObject(object.getClass().getName(), object);
    }

    @Test
    public void caseEmptyByteBuf() throws IOException {
        final ByteBuf buf = new EmptyByteBuf(ByteBufAllocator.DEFAULT);
        try {
            caseObject(buf);
        } finally {
            buf.release();
        }
    }

    @Test
    public void caseUnpooledByteBuf() throws IOException {
        final ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeDouble(Math.PI);
            buf.writeFloat(1.0f / 33.0f);
            buf.writeByte(5);
            caseObject(buf);
        } finally {
            buf.release();
        }
    }

    @Test
    public void caseEventExecutorGroup() throws Exception {
        final EventExecutorGroup group = new DefaultEventExecutor();
        try {
            group.execute(Thread::yield);
            caseObject(group);
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @Test
    public void caseGlobalEventExecutor() throws Exception {
        caseObject(GlobalEventExecutor.INSTANCE);
        caseObject(GlobalEventExecutorTransformer.INSTANCE);
    }

    @Test
    public void caseHttpMethod() throws IOException {
        caseObject(HttpMethod.GET);
    }

    @Test
    public void caseHttpResponseStatus() throws IOException {
        caseObject(HttpResponseStatus.NOT_FOUND);
    }

    @Test
    public void caseHttpScheme() throws IOException {
        caseObject(HttpScheme.HTTPS);
    }

    @Test
    public void caseNetUtil() throws IOException {
        caseObject(NetUtilTransformer.INSTANCE);
    }

    @Test
    public void casePlatformDependent() throws IOException {
        caseObject(PlatformDependentTransformer.INSTANCE);
    }

    @Test
    public void caseInetSocketAddress() throws IOException {
        caseObject(new InetSocketAddress(InetAddress.getByAddress(new byte[] {(byte) 192, (byte) 168, 1, 1}), 605));
    }

    @Test
    public void caseDomainSocketAddress() throws IOException {
        caseObject(new DomainSocketAddress("master"));
    }

    @Test
    public void caseLocalAddress() throws IOException {
        caseObject(new LocalAddress("debug"));
    }

    @Test
    public void caseVersion() throws IOException {
        caseObject(VersionTransformer.INSTANCE);
    }
}
