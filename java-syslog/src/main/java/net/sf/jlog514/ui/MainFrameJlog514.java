/*
 * Copyright (C) 2014 julio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jlog514.ui;

import java.net.DatagramPacket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import net.sf.jlog514.listener.Jlog514Listener;

/**
 *
 * @author julio
 */
public class MainFrameJlog514 extends javax.swing.JFrame implements Observer{
    private final Jlog514Listener listener;
    private Thread thr;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    /**
     * Creates new form MainFrameJlog514
     * @param listener
     */
    public MainFrameJlog514(Jlog514Listener listener) {
        initComponents();
        this.listener = listener;
        this.mainPort.setText(this.listener.getLocalportString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainScrollPanel = new javax.swing.JScrollPane();
        mainTextArea = new java.awt.TextArea();
        mainToolbar = new javax.swing.JToolBar();
        mainStartButton = new java.awt.Button();
        mainStopButton = new java.awt.Button();
        mainPropertiesButton = new java.awt.Button();
        toolbarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(150, 0), new java.awt.Dimension(10, 32767));
        mainLabelPort = new javax.swing.JLabel();
        mainPort = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        mainScrollPanel.setViewportView(mainTextArea);

        mainToolbar.setRollover(true);

        mainStartButton.setLabel("Start");
        mainStartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainStartButtonMouseClicked(evt);
            }
        });
        mainToolbar.add(mainStartButton);

        mainStopButton.setLabel("Stop");
        mainStopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainStopButtonMouseClicked(evt);
            }
        });
        mainToolbar.add(mainStopButton);

        mainPropertiesButton.setLabel("Properties");
        mainToolbar.add(mainPropertiesButton);
        mainToolbar.add(toolbarFiller);

        mainLabelPort.setText("Port:");
        mainToolbar.add(mainLabelPort);
        mainToolbar.add(mainPort);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainToolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
            .addComponent(mainScrollPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mainStartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainStartButtonMouseClicked
        if (this.thr == null){
            this.thr = new Thread(this.listener);
            this.thr.setName("GUI-Listener");
            this.thr.start();
        }
        
        
    }//GEN-LAST:event_mainStartButtonMouseClicked

    private void mainStopButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainStopButtonMouseClicked
        if (this.thr.isAlive()){
            this.listener.closeSocket();
            this.thr.interrupt();
            this.thr = null;
        }
    }//GEN-LAST:event_mainStopButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mainLabelPort;
    private java.awt.Label mainPort;
    private java.awt.Button mainPropertiesButton;
    private javax.swing.JScrollPane mainScrollPanel;
    private java.awt.Button mainStartButton;
    private java.awt.Button mainStopButton;
    private java.awt.TextArea mainTextArea;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.Box.Filler toolbarFiller;
    // End of variables declaration//GEN-END:variables

    // @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String){
            mainTextArea.append((String) arg + "\n");
        }
        else if (arg instanceof DatagramPacket){
            DatagramPacket dat = (DatagramPacket)arg;            
            String load = new String(dat.getData(),0,dat.getLength());
            String append = format.format(new Date()) +" [" + dat.getAddress().toString();
            append += "] " + load + "\n";            
            mainTextArea.append(append);
        }
    }
}
