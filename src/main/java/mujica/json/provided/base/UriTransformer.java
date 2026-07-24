package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

@CodeHistory(date = "2026/5/1")
public class UriTransformer implements JsonContextTransformer<URI> {

    public static final UriTransformer INSTANCE = new UriTransformer();

    static final FastString SCHEME = new FastString("scheme");

    static final FastString AUTHORITY = new FastString("authority");

    static final FastString USER_INFO = new FastString("userInfo");

    static final FastString HOST = new FastString("host");

    static final FastString PORT = new FastString("port");

    static final FastString PATH = new FastString("path");

    static final FastString QUERY = new FastString("query");

    static final FastString FRAGMENT = new FastString("query");

    @Override
    public void transform(@NotNull URI uri, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String str = uri.getScheme();
            if (str != null) {
                out.key(SCHEME);
                out.stringValue(str);
            }
            str = uri.getRawAuthority();
            if (str != null) {
                out.key(AUTHORITY);
                out.stringValue(str);
            }
            str = uri.getRawUserInfo();
            if (str != null) {
                out.key(USER_INFO);
                out.stringValue(str);
            }
            str = uri.getHost();
            if (str != null) {
                out.key(HOST);
                out.stringValue(str);
            }
            int port = uri.getPort();
            if (port != -1) {
                out.key(PORT);
                out.numberValue(port);
            }
            str = uri.getRawPath();
            if (str != null) {
                out.key(PATH);
                out.stringValue(str);
            }
            str = uri.getRawQuery();
            if (str != null) {
                out.key(QUERY);
                out.stringValue(str);
            }
            str = uri.getRawFragment();
            if (str != null) {
                out.key(FRAGMENT);
                out.stringValue(str);
            }
        }
        out.closeObject();
    }
}
