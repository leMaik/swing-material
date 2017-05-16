package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;

/**
 * A Material Design combo box.
 *
 * @see <a href="https://www.google.com/design/spec/components/buttons.html#buttons-dropdown-buttons">Dropdown buttons (Google design guidelines)</a>
 */
public class MaterialComboBox<T> extends JComboBox<T> {
    //DS-addons: use a Line to signal focus
    private MaterialTextField.Line line = new MaterialTextField.Line(this);
    //DS-addons: use an accentColor instead of a hardcoded one
    private Color accentColor = MaterialColor.PINK_500;
    //DS-addons: use hints as text fields do
    private String hint = "";
    
    public MaterialComboBox() {
        setModel(new DefaultComboBoxModel<T>());
        setRenderer(new FieldRenderer<T>(this));
        setUI(new BasicComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new Popup(comboBox);
                popup.getAccessibleContext().setAccessibleParent(comboBox);
                return popup;
            }

            @Override
            protected JButton createArrowButton() {
                //DS-addons: Now this was giving me a NPE every single time,
                //so I checked out what super.createArrowButton() usually does
                //and adapted it. It needs some testing in OSes other than Windows.
                //return null;
                //return super.createArrowButton();
                JButton button = new javax.swing.plaf.basic.BasicArrowButton(
                        javax.swing.plaf.basic.BasicArrowButton.SOUTH,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT);
                button.setName("ComboBox.arrowButton");
                return button;
            }
        });
        //DS-addons: Prevent overlapping with other form components
        setOpaque(false);
        setBackground(MaterialColor.TRANSPARENT);
    }
    
    //DS-addons: use an accentColor instead of a hardcoded one
    /**
     * Gets the color the label changes to when this {@code materialTextField}
     * is focused.
     * @return the {@code "Color"} currently in use for accent. The default
     *         value is {@link MaterialColor#PINK_500}.
     */
    public Color getAccent() {
        return accentColor;
    }

    /**
     * Sets the color the label changes to when this {@code materialTextField}
     * is focused. The default value is {@link MaterialColor#PINK_500}.
     * @param accentColor the {@code "Color"} that should be used for accent.
     */
    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
    }
    //

    //DS-addons: use hints as text fields do
    /**
     * Gets the hint text. The hint text is displayed when the list inside this
     * combo box is empty or no element has been selected yet.
     *
     * @return hint text
     */
    public String getHint() {
        return hint;
    }

    /**
     * Sets the hint text. The hint text is displayed when the list inside this
     * combo box is empty or no element has been selected yet.
     *
     * @param hint hint text
     */
    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }
    //
    
    //DS-addons: use a Line to signal focus
    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        line.update();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //g.setColor(Color.WHITE);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(Roboto.REGULAR.deriveFont(16f));
        //DS-addons: use hints as text fields do
        //g2.setColor(Color.BLACK);
        g.setColor(getSelectedItem() == null ? MaterialColor.MIN_BLACK:getForeground());
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        //DS-addons: use hints as text fields do
        String text = getSelectedItem() != null ? getSelectedItem().toString() : (hint != null ? hint:"");
        g.drawString(text, 0, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);

        g2.setColor(MaterialColor.GREY_300);
        g2.fillRect(0, getHeight() - 9, getWidth(), 1);

        if (isFocusOwner()) g2.setColor(accentColor);
        //DS-addons: Relativize position of the arrow
        //g2.fillPolygon(new int[]{getWidth() - 5, getWidth() - 10, getWidth() - 15}, new int[]{getHeight() - 24, getHeight() - 19, getHeight() - 24}, 3);
        g2.fillPolygon(new int[]{getWidth() - 5, getWidth() - 10, getWidth() - 15}, new int[]{getHeight()/2 - 3, getHeight()/2 + 3, getHeight()/2 - 3}, 3);
        
        //DS-addons: use a Line to signal focus
        g2.setColor(accentColor);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 10, (int) line.getWidth(), 2);
    }

    public static class FieldRenderer<T> extends JComponent implements ListCellRenderer<T> {
        private final MaterialComboBox comboBox;
        private String text;
        private boolean mouseOver = false;
        private boolean selected = false;

        public FieldRenderer(MaterialComboBox comboBox) {
            this.comboBox = comboBox;
        }

        @Override
        public Component getListCellRendererComponent(JList jList, Object o, int index, boolean isSelected, boolean cellHasFocus) {
            text = o != null ? o.toString() : "";
            setSize(jList.getWidth(), 56);
            setPreferredSize(new Dimension(jList.getWidth(), 32));
            setOpaque(true);
            mouseOver = isSelected;
            selected = comboBox.getSelectedIndex() == index;
            return this;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (mouseOver) {
                g.setColor(MaterialColor.GREY_200);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(Roboto.REGULAR.deriveFont(15f));
            if (selected) {
                //DS-addons: use an accentColor instead of a hardcoded one
                //g2.setColor(MaterialColor.PINK_500);
                g2.setColor(comboBox.accentColor);
            } else {
                g2.setColor(Color.BLACK);
            }
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(text, 24, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);
        }
    }

    public static class Popup extends BasicComboPopup {
        public Popup(JComboBox combo) {
            super(combo);
            setBackground(Color.WHITE);
            setOpaque(false);
            setBorderPainted(false);
        }

        @Override
        protected JScrollPane createScroller() {
            JScrollPane scroller = super.createScroller();
            scroller.setVerticalScrollBar(new ScrollBar(Adjustable.VERTICAL));
            scroller.setBorder(new MatteBorder(16, 0, 16, 0, Color.WHITE));
            return scroller;
        }

        @Override
        protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
            return super.computePopupBounds(px, py - comboBox.getHeight() + 10,
                    (int) Math.max(comboBox.getPreferredSize().getWidth(), pw), ph);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }
    }

    public static class ScrollBar extends JScrollBar {
        public ScrollBar(int orientation) {
            super(orientation);
            setPreferredSize(new Dimension(5, 100));

            setUI(new BasicScrollBarUI() {
                @Override
                protected ScrollListener createScrollListener() {
                    setUnitIncrement(56);
                    setBlockIncrement(560);
                    return super.createScrollListener();
                }

                @Override
                protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                    g.setColor(Color.WHITE);
                    g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    JButton dummyButton = new JButton();
                    dummyButton.setPreferredSize(new Dimension(0, 0));
                    return dummyButton;
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    JButton dummyButton = new JButton();
                    dummyButton.setPreferredSize(new Dimension(0, 0));
                    return dummyButton;
                }

                @Override
                protected Dimension getMinimumThumbSize() {
                    return new Dimension(5, 50);
                }

                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    if (!thumbBounds.isEmpty() && this.scrollbar.isEnabled()) {
                        //DS-addons: Minor changes in the appearance of the scrollbar
                        //int w = thumbBounds.width;
                        //int h = thumbBounds.height;
                        //g.translate(thumbBounds.x, thumbBounds.y);
                        //g.setColor(MaterialColor.GREY_500);
                        //g.fillRect(0, 0, w, h);
                        //g.translate(-thumbBounds.x, -thumbBounds.y);
                        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        boolean isVertical = ScrollBar.this.getOrientation()
                                == Adjustable.VERTICAL;
                        g.setColor(MaterialColor.GREY_500);
                        g.fillRoundRect(thumbBounds.x, thumbBounds.y,
                                thumbBounds.width, thumbBounds.height,
                                isVertical ? thumbBounds.width:thumbBounds.height,
                                isVertical ? thumbBounds.width:thumbBounds.height);
                    }
                }

                @Override
                public void layoutContainer(Container scrollbarContainer) {
                    super.layoutContainer(scrollbarContainer);
                    incrButton.setBounds(0, 0, 0, 0);
                    decrButton.setBounds(0, 0, 0, 0);
                }
            });
        }

        @Override
        public Component add(Component comp) {
            if (comp != null) {
                return super.add(comp);
            }
            return null;
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paint(g);
        }
    }

    /*public static void main(String[] args) {
        JFrame window = new JFrame();

        MaterialComboBox comboBox = new MaterialComboBox();
        for (int i = 1; i <= 1000; i++) {
            comboBox.addItem("Item " + i);
        }
        comboBox.setSize(200, 52);
        comboBox.setLocation(0, 20);

        MaterialTextField textField = new MaterialTextField();
        textField.setText("blubb");
        textField.setLocation(200, 0);
        textField.setSize(200, 72);

        window.setLayout(null);
        window.add(comboBox);
        window.add(textField);
        window.setSize(250, 100);
        window.setVisible(true);
    }*/
}
