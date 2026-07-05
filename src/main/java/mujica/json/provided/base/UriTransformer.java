package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

/**
 * Created on 2026/5/1.
 */
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
    public void transform(@NotNull URI in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String str = in.getScheme();
            if (str != null) {
                out.stringKey(SCHEME);
                out.stringValue(str);
            }
            str = in.getRawAuthority();
            if (str != null) {
                out.stringKey(AUTHORITY);
                out.stringValue(str);
            }
            str = in.getRawUserInfo();
            if (str != null) {
                out.stringKey(USER_INFO);
                out.stringValue(str);
            }
            str = in.getHost();
            if (str != null) {
                out.stringKey(HOST);
                out.stringValue(str);
            }
            int port = in.getPort();
            if (port != -1) {
                out.stringKey(PORT);
                out.numberValue(port);
            }
            str = in.getRawPath();
            if (str != null) {
                out.stringKey(PATH);
                out.stringValue(str);
            }
            str = in.getRawQuery();
            if (str != null) {
                out.stringKey(QUERY);
                out.stringValue(str);
            }
            str = in.getRawFragment();
            if (str != null) {
                out.stringKey(FRAGMENT);
                out.stringValue(str);
            }
        }
        out.closeObject();
    }
}
