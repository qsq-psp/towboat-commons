package mujica.reflect.source;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.CodeHistoryTimeline;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@CodeHistory(date = "2026/2/1")
public class ChronicleAnnotationProcessor extends AbstractProcessor {

    public ChronicleAnnotationProcessor() {
        super();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        try {
            Files.createFile(Path.of("D:\\Download\\Ultramarine\\touch"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(
                CodeHistory.class.getName(),
                CodeHistoryTimeline.class.getName()
        );
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(CodeHistory.class)) {
            accept(element.getAnnotation(CodeHistory.class));
        }
        for (Element element : roundEnvironment.getElementsAnnotatedWith(CodeHistoryTimeline.class)) {
            for (CodeHistory codeHistory : element.getAnnotation(CodeHistoryTimeline.class).value()) {
                accept(codeHistory);
            }
        }
        return true;
    }

    public void accept(@NotNull CodeHistory codeHistory) {
        System.out.println(codeHistory.date());
    }
}
