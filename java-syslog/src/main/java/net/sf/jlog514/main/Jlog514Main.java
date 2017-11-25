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

package net.sf.jlog514.main;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.sf.jlog514.listener.Jlog514Listener;
import net.sf.jlog514.ui.MainFrameJlog514;
import org.apache.log4j.Logger;

/**
 *
 * @author julio
 */
public class Jlog514Main implements Runnable{
    Properties properties;
    private Logger log = Logger.getLogger(Jlog514Main.class.getSimpleName());

    public Jlog514Main(Properties properties) {
        this.properties = properties;
    }

    // @Override
    public void run() {
        int localport = Integer.parseInt(properties.getProperty("port"));
        BlockingQueue<String> pipe = new LinkedBlockingQueue();
        Jlog514Listener listen = new Jlog514Listener(localport, pipe);        
        if(properties.getProperty("ui").equals("true")){
           MainFrameJlog514 ui = new MainFrameJlog514(listen);            
           listen.addObserver(ui);
           ui.setVisible((true));
        }
        else {
            try {
                Thread listener = new Thread(listen);
                listener.setName("Listener");
                listener.start();
                listener.join();
            } catch (InterruptedException ex) {
               log.error(ex);
            }
        }
    }
    
    
    
}
