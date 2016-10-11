package de.craften.ui.swingmaterial;

import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.UIManager;
import javax.swing.event.*;
import javax.swing.plaf.UIResource;
import javax.swing.text.*;


public class MaterialFormattedTextField extends MaterialTextField {

    private static final String uiClassID = "FormattedTextFieldUI";
    private static final Action[] defaultActions =
            { new CommitAction(), new CancelAction() };

    public static final int COMMIT = 0;

    public static final int COMMIT_OR_REVERT = 1;

    public static final int REVERT = 2;

    public static final int PERSIST = 3;

    private AbstractFormatterFactory factory;

    private AbstractFormatter format;

    private Object value;

    private boolean editValid;
    
    private int focusLostBehavior;
    /**
     * Indicates the current value has been edited.
     */
    private boolean edited;
    /**
     * Used to set the dirty state.
     */
    private DocumentListener documentListener;
    /**
     * Masked used to set the AbstractFormatterFactory.
     */
    private Object mask;
    /**
     * ActionMap that the TextFormatter Actions are added to.
     */
    private ActionMap textFormatterActionMap;
    /**
     * Indicates the input method composed text is in the document
     */
    private boolean composedTextExists = false;
    /**
     * A handler for FOCUS_LOST event
     */
    private FocusLostHandler focusLostHandler;



    
    
    public MaterialFormattedTextField() {
        super();
        enableEvents(AWTEvent.FOCUS_EVENT_MASK);
        setFocusLostBehavior(COMMIT_OR_REVERT);
    }

    public MaterialFormattedTextField(Object value) {
        this();
        setValue(value);
    }

    public MaterialFormattedTextField(java.text.Format format) {
        this();
        setFormatterFactory(getDefaultFormatterFactory(format));
    }

    public MaterialFormattedTextField(AbstractFormatter formatter) {
        this(new DefaultFormatterFactory(formatter));
    }

    public MaterialFormattedTextField(AbstractFormatterFactory factory) {
        this();
        setFormatterFactory(factory);
    }

    public MaterialFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        this(currentValue);
        setFormatterFactory(factory);
    }

    public void commitEdit() throws ParseException {
        AbstractFormatter format = getFormatter();

        if (format != null) {
            setValue(format.stringToValue(getText()), false, true);
        }
    }

    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), defaultActions);
    }

}
