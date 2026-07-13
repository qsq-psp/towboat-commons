package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ObjectInputFilter;

@CodeHistory(date = "2026/6/3")
public class FilterInfoTransformer implements JsonContextTransformer<ObjectInputFilter.FilterInfo> {

    public static final FilterInfoTransformer INSTANCE = new FilterInfoTransformer();

    @Override
    public void transform(@NotNull ObjectInputFilter.FilterInfo in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            Class<?> serialClass = in.serialClass();
            if (serialClass != null) {
                out.key(ClassLoaderTransformer.CLASS);
                out.stringValue(serialClass.getName());
            }
        }
        {
            long arrayLength = in.arrayLength();
            if (arrayLength != -1L) {
                out.stringValue("arrayLength");
                out.numberValue(arrayLength);
            }
        }
        {
            out.stringValue("depth");
            out.numberValue(in.depth());
            out.stringValue("references");
            out.numberValue(in.references());
            out.stringValue("streamBytes");
            out.numberValue(in.streamBytes());
        }
        out.closeObject();
    }
}
