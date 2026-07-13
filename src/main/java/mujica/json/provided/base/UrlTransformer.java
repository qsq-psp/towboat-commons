package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Created on 2026/6/4.
 */
public class UrlTransformer implements JsonContextTransformer<URL> {

    public static final UrlTransformer INSTANCE = new UrlTransformer();

    static final FastString PROTOCOL = new FastString("protocol");

    static final FastString FILE = new FastString("file");

    static final FastString REFERENCE = new FastString("reference");

    @Override
    public void transform(@NotNull URL in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String str = in.getQuery();
            if (str != null) {
                out.key(UriTransformer.QUERY);
                out.stringValue(str);
            }
            str = in.getPath();
            if (str != null) {
                out.key(UriTransformer.PATH);
                out.stringValue(str);
            }
            str = in.getUserInfo();
            if (str != null) {
                out.key(UriTransformer.USER_INFO);
                out.stringValue(str);
            }
            str = in.getAuthority();
            if (str != null) {
                out.key(UriTransformer.AUTHORITY);
                out.stringValue(str);
            }
            out.key(UriTransformer.PORT);
            out.numberValue(in.getPort());
            out.key(PROTOCOL);
            out.stringValue(in.getProtocol());
            str = in.getHost();
            if (str != null) {
                out.key(UriTransformer.HOST);
                out.stringValue(str);
            }
            str = in.getFile();
            if (str != null) {
                out.key(FILE);
                out.stringValue(str);
            }
            str = in.getRef();
            if (str != null) {
                out.key(REFERENCE);
                out.stringValue(str);
            }
        }
        out.closeObject();
    }
}
