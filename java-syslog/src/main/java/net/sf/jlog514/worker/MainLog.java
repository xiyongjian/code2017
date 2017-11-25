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

package net.sf.jlog514.worker;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


import net.sf.jlog514.ui.MainUIJlog514;


public class MainLog implements Runnable{
	
	private MainUIJlog514 theUI;
	private int localPort = 8514;
	private boolean stopRequested;
	private DatagramSocket socket;
	private Thread runThread;
	
	public void run(){
		runThread = Thread.currentThread();
	    stopRequested = false;
		try {
			socket = new DatagramSocket(localPort);	
			theUI.addLog("Service log started on port "+ localPort);
		} catch (SocketException e) {
			theUI.addLog("Can't start listening: "+e.getMessage());
		}
		try {
			while(!stopRequested){
				DatagramPacket dato = new DatagramPacket(new byte[2048], 2048);
				socket.receive(dato);
				String msg = new String(dato.getData(), 0, dato.getLength());
				theUI.addLog(msg);
			}
			
		}  catch (IOException e){
			theUI.addLog(e.getMessage());
		}		
	
	}
	
	public void init_thread(MainUIJlog514 pUI,int pPort){
		theUI = pUI;
		localPort = pPort;
		
	}
		
	public void stopRequest() {
	    stopRequested = true;
	    
	    if (runThread != null) {
	    	socket.close();
	    	runThread.interrupt();
	    }
	  }
}