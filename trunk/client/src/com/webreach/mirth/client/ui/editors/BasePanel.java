/*
 * BasePanel.java
 *
 * Created on February 6, 2007, 1:44 PM
 */

package com.webreach.mirth.client.ui.editors;

import java.awt.CardLayout;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author  brendanh
 */
public class BasePanel extends javax.swing.JPanel
{
    private Map<Object, Object> data;
    
    /** Creates new form BasePanel */
    public BasePanel()
    {
        initComponents();
        this.setLayout( new CardLayout() );
        data = null;
    }

    // Adds a new card to the panel.  
    public void addCard( JPanel panel, String type ){
            this.add(panel, type);
    }

    // Shows a certain card
    public void showCard( String type ) {
            CardLayout cl = (CardLayout)this.getLayout();
            cl.show(this, type);
    }

    // return the data object
    public Map<Object, Object> getData() {
            return data;
    }

    // set the data object
    public void setData( Map<Object, Object> data ) {
            this.data = data;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
