package mujica.json.provided.xml;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.CharacterData;

/**
 * Created on 2026/5/5.
 */
public class CharacterDataTransformer implements JsonContextTransformer<CharacterData> {

    public static final CharacterDataTransformer INSTANCE = new CharacterDataTransformer();

    @Override
    public void transform(@NotNull CharacterData in, @NotNull JsonHandler out, JsonContext context) {
        out.stringValue(in.getData());
    }
}
