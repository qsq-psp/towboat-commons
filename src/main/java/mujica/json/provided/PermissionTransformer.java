package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.security.Permission;

/**
 * Created on 2026/5/5.
 */
public class PermissionTransformer implements JsonContextTransformer<Permission> {

    static final FastString ACTIONS = new FastString("actions");

    @Override
    public void transform(Permission in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey(ACTIONS);
            out.stringValue(in.getActions());
        }
        out.closeObject();
    }
}
