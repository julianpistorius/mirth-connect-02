/*
 * JavaScriptPanel.java
 *
 * Created on February 6, 2007, 12:16 PM
 */

package com.webreach.mirth.client.ui.editors;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.JavaScriptTokenMarker;

import com.webreach.mirth.client.ui.UIConstants;

/**
 * 
 * @author brendanh
 */
public class JavaScriptPanel extends BasePanel
{
    private static SyntaxDocument scriptDoc;

    private LineNumber lineNumbers;

    private MirthEditorPane parent;

    private String header = "{";

    private String footer = "}";

    /** Creates new form JavaScriptPanel */
    public JavaScriptPanel(MirthEditorPane p)
    {
        super();
        parent = p;
        initComponents();
        scriptDoc = new SyntaxDocument();
        scriptDoc.setTokenMarker(new JavaScriptTokenMarker());
        scriptTextPane.setDocument(scriptDoc);
        scriptTextPane.getDocument().addDocumentListener(new DocumentListener()
        {
            public void changedUpdate(DocumentEvent arg0)
            {
            }

            public void insertUpdate(DocumentEvent arg0)
            {
                parent.modified = true;
            }

            public void removeUpdate(DocumentEvent arg0)
            {
                parent.modified = true;
            }
        });
    }

    public Map<Object, Object> getData()
    {
        Map<Object, Object> m = new HashMap<Object, Object>();
        m.put("Script", scriptTextPane.getText().trim());
        return m;
    }

    public void setData(Map<Object, Object> m)
    {
        boolean modified = parent.modified;

        if (m != null)
            scriptTextPane.setText((String) m.get("Script"));

        else
            scriptTextPane.setText("");

        parent.modified = modified;
    }

    public String getJavaScript()
    {
        return scriptTextPane.getText();
    }

    public void setHighlighters()
    {
        scriptTextPane.setBackground(UIConstants.DRAG_HIGHLIGHTER_COLOR);
    }

    public void unsetHighlighters()
    {
        scriptTextPane.setBackground(UIConstants.BACKGROUND_COLOR);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        scriptTextPane = new com.webreach.mirth.client.ui.components.MirthSyntaxTextArea(true, true);

        setBackground(new java.awt.Color(255, 255, 255));
        scriptTextPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(scriptTextPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, scriptTextPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.webreach.mirth.client.ui.components.MirthSyntaxTextArea scriptTextPane;
    // End of variables declaration//GEN-END:variables

}
