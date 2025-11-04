package mujica.reflect.bytecode;

import mujica.io.nest.BufferedLimitedDataInputStream;
import mujica.io.codec.IndentWriter;
import mujica.io.codec.RepeatCharIndentWriter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2025/9/22.
 */
@SuppressWarnings("SameParameterValue")
@CodeHistory(date = "2025/9/22")
public class JavaP {

    public static void main(String[] args) {
        transform(
                "D:\\Java\\CDP\\target\\classes\\mujica\\io\\compress\\TowboatInflaterInputStream.class",
                "D:\\Java\\coo\\TowboatInflaterInputStream.javap.txt"
        );
    }

    private static void transform(@NotNull String src, @NotNull String dst) {
        try (BufferedLimitedDataInputStream is = new BufferedLimitedDataInputStream(new FileInputStream(src))) {
            ClassFile classFile = new ClassFile();
            classFile.read(is);
            try (IndentWriter iw = new RepeatCharIndentWriter(new FileWriter(dst, StandardCharsets.UTF_8))) {
                classFile.write(iw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
