/**
 *  Syslog Server Java:
 *  Multiplatform: Easy setup Syslog Server tool for recording network messages. 
 *  It opens the UDP port 514 on your computer to act as Syslog Server and displays 
 *  the messages immediately in your screen.
 *  
 *  Copyright (C)  Julio Molina Soler <jmolinaso@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jlog514.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;


import net.sf.jlog514.worker.MainLog;
import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class MainUIJlog514 extends JFrame implements ActionListener{
	private Logger log = Logger.getLogger(MainUIJlog514.class.getSimpleName());
	private JFileChooser fc = new JFileChooser();
	private JTextArea textArea= new JTextArea(500,200);
	private JToggleButton follow = new JToggleButton("Follow up");
	private MainLog l514;
	private Thread t;
	private int localPort = 514;
	
	public MainUIJlog514(){
		super("Java Syslog Server"); 
		Dimension theSize = new Dimension(700,400);
		setSize(theSize);
		//create menubar
		JMenuBar menu = new JMenuBar();
		
		JButton save = new JButton("Save view");
		save.addActionListener(this);
		save.setActionCommand("Save");
		menu.add(save);
		
		JButton startListener = new JButton("Start Listening");
		startListener.addActionListener(this);
		startListener.setActionCommand("StartListener");
		menu.add(startListener);
		
		JButton stopListener = new JButton("Stop Listening");
		stopListener.addActionListener(this);
		stopListener.setActionCommand("StopListener");
		menu.add(stopListener);
		
		menu.add(follow);
		
		JButton options = new JButton("Change Port");
		options.addActionListener(this);
		options.setActionCommand("ChangePort");
		menu.add(options);
		
		JButton about = new JButton("About");
		about.addActionListener(this);
		about.setActionCommand("About");
		menu.add(about);
		
		setJMenuBar(menu);
		//create panel
		JPanel panel = new JPanel();
		setLayout(new BorderLayout());
		panel.setVisible(true);
		//Add options into panel

		textArea.setMargin(new Insets(5,5,5,5));
		textArea.setEditable(false);
		textArea.scrollRectToVisible(getBounds());
		JScrollPane logScrollPane = new JScrollPane(textArea);
		
		//add(log);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(panel);
		add(logScrollPane);
		setVisible(true);
	}

	// @Override
	public void actionPerformed(ActionEvent e) {
		if("OpenFile".equals(e.getActionCommand())) {
			int returnVal = fc.showOpenDialog(MainUIJlog514.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                OpenFile(file);
	        } else {
	        	addLog("Open File cancelled.");
	        }
		}
		
		if("Save".equals(e.getActionCommand())) {
			int returnVal = fc.showSaveDialog(MainUIJlog514.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                FileWriter fw;
				try {
					fw = new FileWriter(file);
				    fw.write(textArea.getText());
				} catch (IOException e1) {
					addLog(e1.getMessage());
				}
            
	        } else {
	        	addLog("Save cancelled.");
	        }
		}
			
		if("StartListener".equals(e.getActionCommand())){
			l514 = new MainLog();
			l514.init_thread(this, localPort);
			t = new Thread(l514);
			t.start();
	
		}
		
		if("StopListener".equals(e.getActionCommand())){
			l514.stopRequest();
		}
		
		if("ChangePort".equals(e.getActionCommand())){
			String inputValue = JOptionPane.showInputDialog("Current port: "+localPort+"\nInsert new port number");
			if(!inputValue.isEmpty())
				localPort = Integer.parseInt(inputValue);
		}
			
	
		if("About".equals(e.getActionCommand()))
			JOptionPane.showMessageDialog(null, 
					"jlog514 is an OpenSource tool\n" +
					"Version 1.0\n" +
					"Developed by Julio Molina Soler jmolinaso@gmail.com"
					,"About", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public void addLog(String i){
	    i = i.replaceAll("\\n","");
	    i = i.replaceAll("\\t","");
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		textArea.append(format.format(new Date())+": "+i+"\n");
		if(follow.isSelected()){
			String s = textArea.getText();
			int pos = s.length();
			textArea.setCaretPosition(pos);
		}
	}
	
	public void OpenFile(File pfile){
		if(!pfile.canRead()){
			addLog("Can't open the file, revise the permissions");
		}
		addLog("File Opened: "+pfile.toString());
		try {
		    BufferedReader in = new BufferedReader(new FileReader(pfile));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	textArea.append(str+"\n");
		    }
		    in.close();
		    addLog("File Closed: "+pfile.toString());
		} catch (IOException e) {
			addLog(e.getMessage());
		}
	}
	
	public void About(){
		
	}
	

}
