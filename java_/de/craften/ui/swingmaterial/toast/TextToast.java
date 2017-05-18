package de.craften.ui.swingmaterial.toast;

import java.awt.*;

/**
 * A toast that contains text.
 *
 * @see <a href="https://www.google.com/design/spec/components/snackbars-toasts.html">Snackbars and toasts</a>
 */
public class TextToast extends Toast {
    private final String content;

    /**
     * Creates a new toast.
     *
     * @param content the content of this toast
     */
    public TextToast(String content) {
        this.content = content;
    }

    /**
     * Gets the content of this toast.
     *
     * @return the content of this toast
     */
    public String getContent() {
        return content;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        FontMetrics metrics = g.getFontMetrics(FONT);
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(FONT);
        g.setColor(Color.WHITE);
        g.drawString(getContent(), 24, y);
    }
}
