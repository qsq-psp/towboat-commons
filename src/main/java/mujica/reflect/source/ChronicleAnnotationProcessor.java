package mujica.reflect.source;

import mujica.reflect.modifier.CodeHistory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created on 2026/2/1.
 */
@CodeHistory(date = "2026/2/1")
public class ChronicleAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        return false;
    }
}
