package de.craften.ui.swingmaterial;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

/**
 * A Material Design combo box.
 *
 * @see <a href="https://www.google.com/design/spec/components/buttons.html#buttons-dropdown-buttons">Dropdown buttons (Google design guidelines)</a>
 */
public class MaterialComboBox<T> extends JComboBox<T> {
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
                return null;
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(Roboto.REGULAR.deriveFont(15f));
        g2.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String text = getSelectedItem() != null ? getSelectedItem().toString() : "";
        g.drawString(text, 0, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);

        g2.setColor(MaterialColor.GREY_300);
        g2.fillRect(0, getHeight() - 9, getWidth(), 1);

        g2.fillPolygon(new int[]{getWidth() - 5, getWidth() - 10, getWidth() - 15}, new int[]{getHeight() - 24, getHeight() - 19, getHeight() - 24}, 3);
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

            g.setFont(Roboto.REGULAR.deriveFont(16f));
            if (selected) {
                g2.setColor(MaterialColor.PINK_500);
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
            return super.computePopupBounds(px, py - comboBox.getHeight(),
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
                        int w = thumbBounds.width;
                        int h = thumbBounds.height;
                        g.translate(thumbBounds.x, thumbBounds.y);
                        g.setColor(MaterialColor.GREY_500);
                        g.fillRect(0, 0, w, h);
                        g.translate(-thumbBounds.x, -thumbBounds.y);
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

    public static void main(String[] args) {
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
    }
}
