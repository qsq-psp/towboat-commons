package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.SampleModel;

@CodeHistory(date = "2026/5/31")
public class SampleModelTransformer implements JsonContextTransformer<SampleModel> {

    public static final SampleModelTransformer INSTANCE = new SampleModelTransformer();

    @Override
    public void transform(@NotNull SampleModel sampleModel, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(RectangleTransformer.WIDTH);
            out.numberValue(sampleModel.getWidth());
            out.key(RectangleTransformer.HEIGHT);
            out.numberValue(sampleModel.getHeight());
            out.key("bandCount");
            out.numberValue(sampleModel.getNumBands());
            out.key("dataElementCount");
            out.numberValue(sampleModel.getNumDataElements());
            out.key("dataType");
            out.numberValue(sampleModel.getDataType());
            out.key("transferType");
            out.numberValue(sampleModel.getTransferType());
        }
        out.closeObject();
    }
}
