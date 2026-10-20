package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

/**
 * Created on 2026/9/17.
 */
public class CertificateTransformer implements JsonContextTransformer<Certificate> {

    public static final CertificateTransformer INSTANCE = new CertificateTransformer();

    @Override
    public void transform(Certificate in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(TypeTransformer.TYPE);
            out.stringValue(in.getType());
        }
        if (in instanceof X509Certificate) { // almost sure
            X509Certificate x509 = (X509Certificate) in;
            out.key(ModuleDescriptorTransformer.VERSION);
            out.numberValue(x509.getVersion());
            out.key("serialNumber");
            out.numberValue(x509.getSerialNumber());
            {
                Principal principal = x509.getIssuerDN();
                if (principal != null) {
                    out.key("issuerDistinguishedName");
                    PrincipalTransformer.INSTANCE.transform(principal, out, context);
                }
            }
            {
                Principal principal = x509.getSubjectDN();
                if (principal != null) {
                    out.key("subjectDistinguishedName");
                    PrincipalTransformer.INSTANCE.transform(principal, out, context);
                }
            }
            {
                Date date = x509.getNotBefore();
                if (date != null) {
                    out.key("notBefore");
                    out.numberValue(date.getTime());
                }
            }
            {
                Date date = x509.getNotAfter();
                if (date != null) {
                    out.key("notAfter");
                    out.numberValue(date.getTime());
                }
            }
            out.key("signatureAlgorithmName");
            out.stringValue(x509.getSigAlgName());
            out.key("signatureAlgorithmOID");
            out.stringValue(x509.getSigAlgOID());
            {
                boolean[] keyUsage = x509.getKeyUsage();
                if (keyUsage != null && keyUsage.length >= 9) {
                    out.key("digitalSignature");
                    out.booleanValue(keyUsage[0]);
                    out.key("nonRepudiation");
                    out.booleanValue(keyUsage[1]);
                    out.key("keyEncipherment");
                    out.booleanValue(keyUsage[2]);
                    out.key("dataEncipherment");
                    out.booleanValue(keyUsage[3]);
                    out.key("keyAgreement");
                    out.booleanValue(keyUsage[4]);
                    out.key("keyCertSign");
                    out.booleanValue(keyUsage[5]);
                    out.key("cRLSign");
                    out.booleanValue(keyUsage[6]);
                    out.key("encipherOnly");
                    out.booleanValue(keyUsage[7]);
                    out.key("decipherOnly");
                    out.booleanValue(keyUsage[8]);
                }
            }
            try {
                List<String> extendedKeyUsage = x509.getExtendedKeyUsage();
                if (extendedKeyUsage != null) {
                    out.key("extendedKeyUsage");
                    out.openArray();
                    for (String usage : extendedKeyUsage) {
                        out.stringValue(usage);
                    }
                    out.closeArray();
                }
            } catch (CertificateParsingException ignore) {}
            out.key("basicConstraints");
            out.numberValue(x509.getBasicConstraints());
        }
        out.closeObject();
    }
}
