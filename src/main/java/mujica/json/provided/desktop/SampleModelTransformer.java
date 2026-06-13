package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.image.SampleModel;

/**
 * Created on 2026/5/31.
 */
public class SampleModelTransformer implements JsonContextTransformer<SampleModel> {

    public static final SampleModelTransformer INSTANCE = new SampleModelTransformer();

    @Override
    public void transform(@NotNull SampleModel in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(RectangleTransformer.WIDTH);
            out.numberValue(in.getWidth());
            out.stringKey(RectangleTransformer.HEIGHT);
            out.numberValue(in.getHeight());
            out.stringKey("bandCount");
            out.numberValue(in.getNumBands());
            out.stringKey("dataElementCount");
            out.numberValue(in.getNumDataElements());
            out.stringKey("dataType");
            out.numberValue(in.getDataType());
            out.stringKey("transferType");
            out.numberValue(in.getTransferType());
        }
        out.closeObject();
    }
}
