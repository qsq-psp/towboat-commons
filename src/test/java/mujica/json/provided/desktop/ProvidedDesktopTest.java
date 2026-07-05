package mujica.json.provided.desktop;

import mujica.json.handler.LateKeyCheckAdapter;
import mujica.json.io.JsonIndentStringBuilderWriter;
import mujica.json.io.JsonStringWriter;
import mujica.json.reflect.JsonContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@CodeHistory(date = "2026/5/30")
public class ProvidedDesktopTest {

    private static final JsonContext CONTEXT = new JsonContext();

    @BeforeClass
    public static void initContext() {
        CONTEXT.loadProvidedDesktop().loadBasic();
    }

    private void caseObject(@NotNull String name, @NotNull Object object) throws IOException {
        final Path currentPath = Path.of("").toAbsolutePath();
        final Path targetPath = Path.of("target", "json-provided").toAbsolutePath();
        if (!(Files.isDirectory(targetPath) && targetPath.startsWith(currentPath))) {
            Assert.fail("target directory");
        }
        final JsonStringWriter jsonWriter = (new JsonIndentStringBuilderWriter()).setIndent("    ");
        CONTEXT.transform(object, new LateKeyCheckAdapter<>(jsonWriter, string -> !string.isBlank()));
        try (Writer writer = Files.newBufferedWriter(targetPath.resolve(name + ".json"), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            writer.write(jsonWriter.getString());
            writer.flush();
        }
    }

    private void caseObject(@NotNull Object object) throws IOException {
        caseObject(object.getClass().getName(), object);
    }

    @Test
    public void caseAction() throws IOException {
        caseObject(new AbstractAction("print") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e);
            }
        });
    }

    @Test
    public void caseAdjustable() throws IOException {
        caseObject(new JScrollBar(JScrollBar.HORIZONTAL, 2026, 1, 1, 9999));
    }

    @Test
    public void caseAffineTransform() throws IOException {
        caseObject(AffineTransform.getRotateInstance(Math.toRadians(100)));
    }

    @Test
    public void caseAlphaComposite() throws IOException {
        caseObject(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.24f));
    }

    @Test
    public void caseBasicStroke() throws IOException {
        caseObject(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
    }

    @Test
    public void caseBoundedRangeModel() throws IOException {
        caseObject(new DefaultBoundedRangeModel(250, 4, 0, 1000));
    }

    @Test
    public void caseColorModel() throws IOException {
        caseObject(ColorModelTransformer.INSTANCE);
    }

    @Test
    public void caseColorSpace() throws IOException {
        caseObject(ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB));
    }

    @Test
    public void caseColor() throws IOException {
        caseObject(Color.YELLOW);
    }

    @Test
    public void caseDataFlavor() throws IOException {
        caseObject(DataFlavor.stringFlavor);
    }

    @Test
    public void caseFont() throws IOException {
        caseObject(FontTransformer.INSTANCE);
    }

    @Test
    public void caseGradientPaint() throws IOException {
        caseObject(new GradientPaint(
                2.5f, 0.0f,
                Color.WHITE,
                7.5f, 2.0f,
                Color.DARK_GRAY
        ));
    }

    @Test
    public void caseGraphicsDevice() throws IOException {
        caseObject(GraphicsDeviceTransformer.INSTANCE);
    }

    @Test
    public void caseGraphics() throws IOException {
        final Graphics2D graphics2D = (new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB)).createGraphics();
        try {
            caseObject(graphics2D);
        } finally {
            graphics2D.dispose();
        }
    }

    @Test
    public void caseInsets() throws IOException {
        caseObject(new Insets(3, 3, 3, 3));
    }

    @Test
    public void caseKeyStroke() throws IOException {
        caseObject(KeyStroke.getKeyStroke('G'));
    }

    @Test
    public void caseShape() throws IOException {
        caseObject(new Rectangle(95, 95, 98, 98));
        caseObject(new Rectangle2D.Float(95.0f, 95.0f, 8.8f, 8.8f));
        caseObject(new Rectangle2D.Double(95.0, 95.0, 0.3, 0.3));
        caseObject(new Line2D.Float(-2.0f, -2.0f, 6.0f, 0.0f));
        caseObject(new Line2D.Double(-2.0, -2.0, 0.5, 1.75));
        caseObject(new Ellipse2D.Float(48.0f, 48.0f, 8.8f, 8.8f));
        caseObject(new Ellipse2D.Double(48.0, 48.0, 0.3, 0.3));
        caseObject(new RoundRectangle2D.Float(-10.0f, -10.0f, 20.0f, 20.0f, 1.0f, 1.0f));
        caseObject(new RoundRectangle2D.Double(-9.0, -9.0, 18.0, 18.0, 3.0, 3.0));
    }

    @Test
    public void caseToolkit() throws IOException {
        caseObject(ToolkitTransformer.INSTANCE);
    }
}
