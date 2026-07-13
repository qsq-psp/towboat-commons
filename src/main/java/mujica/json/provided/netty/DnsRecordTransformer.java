package mujica.json.provided.netty;

import io.netty.handler.codec.dns.DnsRecord;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/7/3.
 */
public class DnsRecordTransformer implements JsonContextTransformer<DnsRecord> {

    @Override
    public void transform(@NotNull DnsRecord dnsRecord, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("name");
            out.stringValue(dnsRecord.name());
            out.key("type");
            out.stringValue(dnsRecord.type().name());
        }
        {
            int dnsClass = dnsRecord.dnsClass();
            out.key("dnsClass");
            switch (dnsClass) {
                case DnsRecord.CLASS_IN:
                    out.stringValue("IN");
                    break;
                case DnsRecord.CLASS_CSNET:
                    out.stringValue("CSNET");
                    break;
                case DnsRecord.CLASS_CHAOS:
                    out.stringValue("CHAOS");
                    break;
                case DnsRecord.CLASS_HESIOD:
                    out.stringValue("HESIOD");
                    break;
                case DnsRecord.CLASS_NONE:
                    out.stringValue("NONE");
                    break;
                case DnsRecord.CLASS_ANY:
                    out.stringValue("ANY");
                    break;
                default:
                    out.numberValue(dnsClass);
                    break;
            }
        }
        out.closeObject();
    }
}
