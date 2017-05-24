package de.craften.ui.swingmaterial;

import static de.craften.ui.swingmaterial.MaterialTextField.HINT_OPACITY_MASK;
import static de.craften.ui.swingmaterial.MaterialTextField.LINE_OPACITY_MASK;
import de.craften.ui.swingmaterial.fonts.Roboto;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Date;

/**
 * A Material Design formatted field.
 *
 * @author Vitor "Pliavi" Silvério
 * @see
 * <a href="https://www.google.com/design/spec/components/text-fields.html">Text
 * fields (Google design guidelines)</a>
 */
public class MaterialFormattedTextField extends JFormattedTextField {
    private MaterialTextField.FloatingLabel floatingLabel;
    private MaterialTextField.Line line;
    private String hint = "";
    private Color accentColor = MaterialColor.CYAN_500;

    /**
     * Creates a new field with no mask.
     */
    public MaterialFormattedTextField() {
        floatingLabel = new MaterialTextField.FloatingLabel(this);
        line = new MaterialTextField.Line(this);
        initMaterialFormattedTextField();
    }

    /**
     * Creates a new field with the value. This will create an
     * <code>AbstractFormatterFactory</code> based on the type of
     * <code>value</code>.
     *
     * @param value Initial value
     */
    public MaterialFormattedTextField(Object value) {
        this();
        setValue(value);
    }

    /**
     * Creates a new field. <code>format</code> is wrapped in an appropriate
     * <code>AbstractFormatter</code> which is then wrapped in an
     * <code>AbstractFormatterFactory</code>.
     *
     * @param format Format used to look up an AbstractFormatter
     */
    public MaterialFormattedTextField(Format format) {
        this();
        setFormatterFactory(getDefaultFormatterFactory(format));
    }

    /**
     * Creates a new field with the specified <code>AbstractFormatter</code>.
     *
     * @param formatter AbstractFormatter to use for formatting.
     */
    public MaterialFormattedTextField(AbstractFormatter formatter) {
        this(new DefaultFormatterFactory(formatter));
    }

    /**
     * Creates a new field with the specified
     * <code>AbstractFormatterFactory</code>.
     *
     * @param factory AbstractFormatterFactory used for formatting.
     */
    public MaterialFormattedTextField(AbstractFormatterFactory factory) {
        this();
        setFormatterFactory(factory);
    }

    /**
     * Creates a new field with the specified
     * <code>AbstractFormatterFactory</code> and initial value.
     *
     * @param factory <code>AbstractFormatterFactory</code> used for formatting.
     * @param currentValue Initial value to use
     */
    public MaterialFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        this(currentValue);
        setFormatterFactory(factory);
    }

    /**
     * Initialize the default values of the field
     */
    private void initMaterialFormattedTextField() {
        setBorder(null);
        setFont(Roboto.REGULAR.deriveFont(16f));
        floatingLabel.setText("");

        setCaret(new DefaultCaret() {
            @Override
            protected synchronized void damage(Rectangle r) {
                MaterialFormattedTextField.this.repaint(); //fix caret not being removed completely
            }
        });
        getCaret().setBlinkRate(500);
    }

    /**
     * Gets the text of the floating label.
     *
     * @return text of the floating label
     */
    public String getLabel() {
        return floatingLabel.getText();
    }

    /**
     * Sets the text of the floating label.
     *
     * @param label text of the floating label
     */
    public void setLabel(String label) {
        floatingLabel.setText(label);
        floatingLabel.update();
        repaint();
    }

    /**
     * Gets the hint text. The hint text is displayed when this textfield is
     * empty.
     *
     * @return hint text
     */
    public String getHint() {
        return hint;
    }

    /**
     * Sets the hint text. The hint text is displayed when this textfield is
     * empty.
     *
     * @param hint hint text
     */
    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }
    
    /**
     * Gets the color the label changes to when this {@code materialTextField}
     * is focused.
     * @return the {@code "Color"} currently in use for accent. The default
     *         value is {@link MaterialColor#CYAN_300}.
     */
    public Color getAccent() {
        return accentColor;
    }

    /**
     * Sets the color the label changes to when this {@code materialTextField}
     * is focused. The default value is {@link MaterialColor#CYAN_300}.
     * @param accentColor the {@code "Color"} that should be used for accent.
     */
    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
        floatingLabel.setAccent(accentColor);
    }
    
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (floatingLabel != null)
            floatingLabel.updateForeground();
    }

    @Override
    public void setText(String s) {
        super.setText(s);
        floatingLabel.update();
        line.update();
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        floatingLabel.update();
        line.update();
        repaint();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        floatingLabel.update();
        line.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.translate(0, 9);
        super.paintComponent(g);
        g2.translate(0, -9);

        if (!getHint().isEmpty() && getText().length() == 0 && (getLabel().isEmpty() || isFocusOwner()) && floatingLabel.isFloatingAbove()) {
            g.setFont(Roboto.REGULAR.deriveFont(16f));
            g2.setColor(Utils.applyAlphaMask(getForeground(), HINT_OPACITY_MASK));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(getHint(), 0, metrics.getAscent() + 36);
        }

        floatingLabel.paint(g2);

        g2.setColor(Utils.applyAlphaMask(getForeground(), LINE_OPACITY_MASK));
        g2.fillRect(0, getHeight() - 9, getWidth(), 1);

        g2.setColor(accentColor);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 10, (int) line.getWidth(), 2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        //intentionally left blank
    }

    /**
     * Returns an AbstractFormatterFactory suitable for the passed in Object
     * type.
     */
    private AbstractFormatterFactory getDefaultFormatterFactory(Object type) {
        if (type instanceof DateFormat) {
            return new DefaultFormatterFactory(new DateFormatter((DateFormat) type));
        }
        if (type instanceof NumberFormat) {
            return new DefaultFormatterFactory(new NumberFormatter(
                    (NumberFormat) type));
        }
        if (type instanceof Format) {
            return new DefaultFormatterFactory(new InternationalFormatter(
                    (Format) type));
        }
        if (type instanceof Date) {
            return new DefaultFormatterFactory(new DateFormatter());
        }
        if (type instanceof Number) {
            AbstractFormatter displayFormatter = new NumberFormatter();
            ((NumberFormatter) displayFormatter).setValueClass(type.getClass());
            AbstractFormatter editFormatter = new NumberFormatter(
                    new DecimalFormat("#.#"));
            ((NumberFormatter) editFormatter).setValueClass(type.getClass());

            return new DefaultFormatterFactory(displayFormatter,
                    displayFormatter, editFormatter);
        }
        return new DefaultFormatterFactory(new DefaultFormatter());
    }

}
