package mujica.json.provided.netty;

import io.netty.handler.codec.dns.DnsMessage;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/7/2.
 */
public class DnsMessageTransformer implements JsonContextTransformer<DnsMessage> {

    @Override
    public void transform(DnsMessage dnsMessage, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("id");
            out.numberValue(dnsMessage.id());
            out.stringKey("operation");
            out.stringValue(dnsMessage.opCode().toString());
            out.stringKey("recursionDesired");
            out.booleanValue(dnsMessage.isRecursionDesired());
            // todo
        }
        out.closeObject();
    }
}
