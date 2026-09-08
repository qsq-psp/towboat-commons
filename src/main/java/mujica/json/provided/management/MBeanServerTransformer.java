package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.management.MBeanServer;

@CodeHistory(date = "2026/8/16")
public class MBeanServerTransformer implements JsonContextTransformer<MBeanServer> {

    static final FastString BEAN_COUNT = new FastString("beanCount");

    static final FastString DEFAULT_DOMAIN = new FastString("defaultDomain");

    static final FastString DOMAINS = new FastString("domains");

    @Override
    public void transform(MBeanServer in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            Integer count = in.getMBeanCount();
            if (count != null) {
                out.key(BEAN_COUNT);
                out.numberValue(count);
            }
        }
        {
            String domain = in.getDefaultDomain();
            if (domain != null) {
                out.key(DEFAULT_DOMAIN);
                out.stringValue(domain);
            }
        }
        {
            String[] domains = in.getDomains();
            if (domains != null) {
                out.key(DOMAINS);
                out.arrayValue(domains);
            }
        }
        out.closeObject();
    }
}
