package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;

/**
 * Created on 2026/9/23.
 */
public class MethodDescriptorTransformer implements JsonContextTransformer<MethodDescriptor> {

    static final FastString METHOD = new FastString("method");

    @Override
    public void transform(MethodDescriptor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            BeanDescriptorTransformer.transformExposed(in, out);
        }
        {
            Method method = in.getMethod();
            if (method != null) {
                out.key(DataFlavorTransformer.CLASS);
                out.stringValue(method.getDeclaringClass().getName());
                out.key(METHOD);
                out.stringValue(method.getName());
            }
        }
        {
            ParameterDescriptor[] parameterDescriptors = in.getParameterDescriptors();
            if (parameterDescriptors != null) {
                out.key("parameterDescriptors");
                out.openArray();
                for (ParameterDescriptor parameterDescriptor : parameterDescriptors) {
                    BeanDescriptorTransformer.transformExposed(parameterDescriptor, out);
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
