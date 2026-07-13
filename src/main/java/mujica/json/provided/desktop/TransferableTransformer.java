package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Created on 2026/7/2.
 */
@CodeHistory(date = "2026/7/2")
public class TransferableTransformer implements JsonContextTransformer<Transferable> {

    @Override
    public void transform(@NotNull Transferable transferable, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(DataFlavorTransformer.CLASS);
            out.stringValue(transferable.getClass().getName());
        }
        {
            DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
            if (dataFlavors != null && dataFlavors.length != 0) {
                out.key("dataFlavors");
                out.openArray();
                for (DataFlavor dataFlavor : dataFlavors) {
                    DataFlavorTransformer.INSTANCE.transform(dataFlavor, out, context);
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
