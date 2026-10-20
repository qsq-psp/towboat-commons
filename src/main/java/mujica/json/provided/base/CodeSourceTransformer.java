package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;

/**
 * Created on 2026/9/14.
 */
public class CodeSourceTransformer implements JsonContextTransformer<CodeSource> {

    public static final CodeSourceTransformer INSTANCE = new CodeSourceTransformer();

    @Override
    public void transform(CodeSource in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            URL url = in.getLocation();
            if (url != null) {
                out.key("location");
                out.stringValue(url.toString());
            }
        }
        {
            CodeSigner[] codeSigners = in.getCodeSigners();
            if (codeSigners != null) {
                out.key("codeSigners");
                out.openArray();
                for (CodeSigner codeSigner : codeSigners) {
                    CodeSignerTransformer.INSTANCE.transform(codeSigner, out, context);
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
