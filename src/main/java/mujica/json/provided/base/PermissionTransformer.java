package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.Permission;

@CodeHistory(date = "2026/5/5")
public class PermissionTransformer implements JsonContextTransformer<Permission> {

    static final FastString ACTIONS = new FastString("actions");

    @Override
    public void transform(@NotNull Permission permission, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(permission.getName());
            out.key(ACTIONS);
            out.stringValue(permission.getActions());
        }
        out.closeObject();
    }
}
