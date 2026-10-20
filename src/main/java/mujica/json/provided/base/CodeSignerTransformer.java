package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.security.CodeSigner;
import java.security.Timestamp;
import java.security.cert.Certificate;
import java.util.List;

/**
 * Created on 2026/9/18.
 */
public class CodeSignerTransformer implements JsonContextTransformer<CodeSigner> {

    public static final CodeSignerTransformer INSTANCE = new CodeSignerTransformer();

    @Override
    public void transform(CodeSigner in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            List<? extends Certificate> certificateList = in.getSignerCertPath().getCertificates();
            out.key("certificatePath");
            out.openArray();
            for (Certificate certificate : certificateList) {
                CertificateTransformer.INSTANCE.transform(certificate, out, context);
            }
            out.closeArray();
        }
        {
            Timestamp timestamp = in.getTimestamp();
            if (timestamp != null) {
                out.key("timestamp");
                out.numberValue(timestamp.getTimestamp().getTime());
            }
        }
        out.closeObject();
    }
}
