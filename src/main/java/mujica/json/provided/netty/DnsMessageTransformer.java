package mujica.json.provided.netty;

import io.netty.handler.codec.dns.DnsMessage;
import io.netty.handler.codec.dns.DnsSection;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/7/2.
 */
public class DnsMessageTransformer implements JsonContextTransformer<DnsMessage> {

    public static final DnsMessageTransformer INSTANCE = new DnsMessageTransformer();

    static void transformDnsSection(@NotNull DnsMessage dnsMessage, @NotNull DnsSection dnsSection, @NotNull JsonHandler out, @Nullable JsonContext context) {
        final int count = dnsMessage.count(dnsSection);
        if (count <= 0) {
            return;
        }
        out.key(dnsSection.name());
        out.openArray();
        for (int index = 0; index < count; index++) {
            DnsRecordTransformer.INSTANCE.transform(dnsMessage.recordAt(dnsSection, index), out, context);
        }
        out.closeArray();
    }

    @Override
    public void transform(@NotNull DnsMessage dnsMessage, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("id");
            out.numberValue(dnsMessage.id());
            out.key("operation");
            out.stringValue(dnsMessage.opCode().toString());
            out.key("recursionDesired");
            out.booleanValue(dnsMessage.isRecursionDesired());
            transformDnsSection(dnsMessage, DnsSection.QUESTION, out, context);
            transformDnsSection(dnsMessage, DnsSection.ANSWER, out, context);
            transformDnsSection(dnsMessage, DnsSection.AUTHORITY, out, context);
            transformDnsSection(dnsMessage, DnsSection.ADDITIONAL, out, context);
        }
        out.closeObject();
    }
}
