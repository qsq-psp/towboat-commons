package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.security.Principal;

@CodeHistory(date = "2026/8/31")
public class PrincipalTransformer implements JsonContextTransformer<Principal> {

    @Override
    public void transform(Principal in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
        }
        out.closeObject();
    }
}
