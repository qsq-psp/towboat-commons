package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.api.SerializeFrom;

import java.util.HashMap;
import java.util.Map;

import static indi.qsq.json.reflect.ClosureConfig.*;

/**
 * Created on 2022/7/12.
 *
 * Container class for ClosureConfig MAP, extends ClosureConfig for constants convenience
 */
final class ClosurePreset {

    /**
     * ClosureConfig MAP is not put in class ClosureConfig to prevent class loading deadlock
     */
    static final HashMap<String, ClosureConfig> MAP = new HashMap<>();

    static void put(String name, String serializer, boolean subClass) {
        final PolyfillClosureConfig closureConfig = (new PolyfillClosureConfig()).setSerializer(serializer);
        if (subClass) {
            closureConfig.setSubClassConfig();
        }
        MAP.put(name, closureConfig);
    }

    private static void putBoolean() {
        final ClosureConfig config = new PolyfillClosureConfig("indi.qsq.json.value.BooleanValueSerializer");
        MAP.put("boolean", config);
        MAP.put("java.lang.Boolean", config);
    }

    private static void putIntegral() {
        final ClosureConfig config = new PolyfillClosureConfig("indi.qsq.json.value.IntegralValueSerializer");
        config.parseConfig = ParseHint.ACCEPT_STRING;
        MAP.put("byte", config);
        MAP.put("java.lang.Byte", config);
        MAP.put("short", config);
        MAP.put("java.lang.Short", config);
        MAP.put("int", config);
        MAP.put("java.lang.Integer", config);
        MAP.put("long", config);
        MAP.put("java.lang.Long", config);
        MAP.put("java.math.BigInteger", config);
        MAP.put("java.util.concurrent.atomic.AtomicInteger", config);
        MAP.put("java.util.concurrent.atomic.AtomicLong", config);
        MAP.put("java.util.concurrent.atomic.LongAccumulator", config);
        MAP.put("java.util.concurrent.atomic.LongAdder", config);
    }

    private static void putFractional() {
        final ClosureConfig config = new PolyfillClosureConfig("indi.qsq.json.value.FractionalValueSerializer");
        config.parseConfig = ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_DOT_STRING | ParseHint.ACCEPT_SCI_STRING;
        config.serializeConfig = SerializeFrom.INFINITE | SerializeFrom.NAN;
        MAP.put("float", config);
        MAP.put("java.lang.Float", config);
        MAP.put("double", config);
        MAP.put("java.lang.Double", config);
        MAP.put("java.math.BigDecimal", config);
        MAP.put("java.util.concurrent.atomic.DoubleAccumulator", config);
        MAP.put("java.util.concurrent.atomic.DoubleAdder", config);
    }

    private static void putString() {
        final ClosureConfig config = new PolyfillClosureConfig("indi.qsq.json.value.StringValueSerializer");
        config.parseConfig = ParseHint.ACCEPT_BOOLEAN | ParseHint.ACCEPT_NUMBER;
        // classes that implements CharSequence
        MAP.put("java.lang.String", config);
        MAP.put("java.lang.StringBuilder", config);
        MAP.put("java.lang.StringBuffer", config);
        MAP.put("io.netty.util.AsciiString", config);
        // classes that has flat toString()
        MAP.put("java.util.UUID", config);
        MAP.put("java.net.URI", config);
        MAP.put("java.net.URL", config);
    }

    private static void putManagementBeans() {
        ClosureConfig config = (new PolyfillClosureConfig()).setSubClassConfig(
                (new ClosureConfig()).setClosureConfig(FLAG_SUPER_CLASS | FLAG_SUPER_INTERFACE)
        ).setClosureConfig(FLAG_COLLECT_SETTERS | FLAG_COLLECT_GETTERS);
        MAP.put("java.lang.management.ThreadMXBean", config);
        MAP.put("java.lang.management.ThreadInfo", config); // not used in ThreadMXBean
        MAP.put("java.lang.management.MemoryMXBean", config);
        MAP.put("java.lang.management.MemoryPoolMXBean", config);
        MAP.put("java.lang.management.MemoryUsage", config); // used in MemoryMXBean and MemoryPoolMXBean
        MAP.put("java.lang.management.CompilationMXBean", config);
        MAP.put("java.lang.management.OperatingSystemMXBean", config);
        config = (new PolyfillClosureConfig()).setSubClassConfig(new ClosureConfig());
        MAP.put("javax.management.NotificationEmitter", config);
    }

    static {
        putBoolean();
        putIntegral();
        putFractional();
        putString();
        put("java.lang.Class", "indi.qsq.json.value.ClassValueSerializer", false); // this class is final
        put("java.lang.ClassLoader", "indi.qsq.json.value.ClassLoaderValueSerializer", true);
        put("java.lang.Runtime", "indi.qsq.json.value.RuntimeValueSerializer", false); // this class is singleton
        put("java.lang.Runtime$Version", "indi.qsq.json.value.RuntimeVersionValueSerializer", false); // this class is final
        put("java.lang.Thread", "indi.qsq.json.value.ThreadValueSerializer", true);
        put("java.lang.ThreadGroup", "indi.qsq.json.value.ThreadGroupValueSerializer", false); // do not cover subclasses
        put("java.util.Locale", "indi.qsq.json.value.LocaleValueSerializer", false); // this class is final
        put("java.util.Locale$LanguageRange", "indi.qsq.json.value.LanguageRangeValueSerializer", false); // this class is final
        put("java.util.Date", "indi.qsq.json.value.DateValueSerializer", true); // java.util.Date has subclasses
        put("java.util.Calendar", "indi.qsq.json.value.CalendarValueSerializer", true); // java.util.Calendar has subclasses
        put("java.util.TimeZone", "indi.qsq.json.value.TimeZoneValueSerializer", true);
        put("java.time.ZoneId", "indi.qsq.json.value.ZoneIdValueSerializer", true);
        put("java.nio.ByteBuffer", "indi.qsq.json.value.ByteBufferValueSerializer", true);
        put("java.nio.charset.Charset", "indi.qsq.json.value.CharsetValueSerializer", true);
        put("java.nio.file.FileStore", "indi.qsq.json.value.FileStoreValueSerializer", true);
        put("java.nio.file.FileSystem", "indi.qsq.json.value.FileSystemValueSerializer", true);
        put("java.nio.file.attribute.FileTime", "indi.qsq.json.value.FileTimeValueSerializer", false); // this class is final
        putManagementBeans();
        put("java.awt.GraphicsDevice", "indi.qsq.json.value.GraphicsDeviceValueSerializer", true);
        put("java.awt.image.ColorModel", "indi.qsq.json.value.ColorModelValueSerializer", true);
        put("java.awt.color.ColorSpace", "indi.qsq.json.value.ColorSpaceValueSerializer", true);
        put("java.awt.Font", "indi.qsq.json.value.FontValueSerializer", true);
        put("org.xml.sax.Attributes", "indi.qsq.json.value.SaxAttributesValueSerializer", true);
        put("org.xml.sax.Locator", "indi.qsq.json.value.SaxLocatorValueSerializer", true);
        put("io.netty.buffer.ByteBuf", "indi.qsq.json.value.NettyBufferValueSerializer", true);
        put("io.netty.channel.Channel", "indi.qsq.json.value.NettyChannelValueSerializer", true);
        put("io.netty.channel.EventLoopGroup", "indi.qsq.json.value.NettyEventLoopGroupValueSerializer", true);
        put("io.netty.handler.codec.http.HttpMethod", "indi.qsq.json.value.HttpMethodValueSerializer", false); // do not cover subclasses
        put("io.netty.handler.codec.http.HttpResponseStatus", "indi.qsq.json.value.HttpResponseStatusValueSerializer", false); // do not cover subclasses
    }

    static final int QUEUE_SIZE = 64;

    static final Class<?>[] QUEUE = new Class<?>[QUEUE_SIZE];

    /**
     * @return null if not found
     */
    static synchronized ClosureConfig get(Class<?> clazz) {
        final Class<?>[] queue = QUEUE;
        int wi = 0;
        queue[wi++] = clazz;
        for (int ri = 0; ri < wi; ri++) {
            clazz = queue[ri];
            ClosureConfig config = MAP.get(clazz.getName());
            if (config != null) {
                if (ri != 0) {
                    config = config.getSubClassConfig();
                }
                if (config != null) {
                    return config;
                }
            }
            Class<?> clazz1 = clazz.getSuperclass();
            if (clazz1 != null && wi < QUEUE_SIZE) {
                queue[wi++] = clazz1;
            }
            for (Class<?> clazz2 : clazz.getInterfaces()) {
                if (wi < QUEUE_SIZE) {
                    queue[wi++] = clazz2;
                }
            }
        }
        return null;
    }

    static ReflectClass create(Class<?> clazz) {
        ClosureConfig config = get(clazz);
        if (config == null) {
            config = (new ClosureConfig()).setClosureConfig(FLAG_COLLECT_FIELDS | FLAG_COLLECT_SETTERS | FLAG_COLLECT_GETTERS | FLAG_SUPER_CLASS);
        }
        return config.load(clazz);
    }

    static synchronized ConversionConfig query(String name) {
        return MAP.get(name);
    }

    static synchronized void queryAll(JsonConsumer jc) {
        jc.openObject();
        for (Map.Entry<String, ClosureConfig> entry : MAP.entrySet()) {
            jc.key(entry.getKey());
            entry.getValue().toJson(jc);
        }
        jc.closeObject();
    }

    /**
     * No instance
     */
    private ClosurePreset() {}
}
