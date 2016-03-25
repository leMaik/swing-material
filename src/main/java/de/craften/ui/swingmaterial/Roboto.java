package de.craften.ui.swingmaterial;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Roboto font.
 *
 * @see <a href="https://www.google.com/design/spec/resources/roboto-noto-fonts.html">Roboto &amp; Noto fonts (Google design guidelines)</a>
 */
public class Roboto {
    public static final Font BLACK = loadFont("Roboto-Black.ttf").deriveFont(Font.BOLD);
    public static final Font BLACK_ITALIC = loadFont("Roboto-BlackItalic.ttf").deriveFont(Font.BOLD | Font.ITALIC);
    public static final Font BOLD = loadFont("Roboto-Bold.ttf").deriveFont(Font.BOLD);
    public static final Font BOLD_ITALIC = loadFont("Roboto-BoldItalic.ttf").deriveFont(Font.BOLD | Font.ITALIC);
    public static final Font ITALIC = loadFont("Roboto-Italic.ttf").deriveFont(Font.ITALIC);
    public static final Font LIGHT = loadFont("Roboto-Light.ttf").deriveFont(Font.PLAIN);
    public static final Font LIGHT_ITALIC = loadFont("Roboto-LightItalic.ttf").deriveFont(Font.ITALIC);
    public static final Font MEDIUM = loadFont("Roboto-Medium.ttf").deriveFont(Font.PLAIN);
    public static final Font MEDIUM_ITALIC = loadFont("Roboto-MediumItalic.ttf").deriveFont(Font.ITALIC);
    public static final Font REGULAR = loadFont("Roboto-Regular.ttf").deriveFont(Font.PLAIN);
    public static final Font THIN = loadFont("Roboto-Thin.ttf").deriveFont(Font.PLAIN);
    public static final Font THIN_ITALIC = loadFont("Roboto-ThinItalic.ttf").deriveFont(Font.ITALIC);

    private static Font loadFont(String resourceName) {
        try (InputStream inputStream = Roboto.class.getResourceAsStream("/fonts/" + resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Could not load " + resourceName, e);
        }
    }
}
