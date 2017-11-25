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

package net.sf.jlog514.listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author julio
 */
public class Jlog514Listener extends Observable implements Runnable{
    private Logger log = Logger.getLogger(Jlog514Listener.class.getSimpleName());
    private int localport;
    private volatile boolean running;
    private BlockingQueue<String> queue;
    private DatagramSocket socket;

    public int getLocalport() {
        return localport;
    }
    
    public String getLocalportString() {
        return ""+localport;
    }
    
    public void closeSocket(){
        if(!this.socket.isClosed()){
            this.socket.close();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Jlog514Listener(int localport,BlockingQueue<String> queue){
        this.localport = localport;
        this.queue = queue;
        this.running = true;           
    }

    public Jlog514Listener(BlockingQueue<String> queue) throws SocketException {
        this(514,queue);
    }
    
    // @Override
    public void run() {
        try {      
            this.socket = new DatagramSocket(this.localport);
            while(this.running){
                DatagramPacket data = new DatagramPacket(new byte[2048], 2048);
                socket.receive(data);
                setChanged();
                notifyObservers(data);
            }
            
        } catch (SocketException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        } finally {
            this.socket.close();
        }
    }
    
}
