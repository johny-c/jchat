/*
 * Copyright (C) 2014 johny
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package client.networking;

import common.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author johny
 */
public class JChatLogger implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("JChatLogger");
    private final BlockingQueue queue;
    private static final String NAME = "JCHAT-LOGGER-THREAD";
    private volatile boolean needed;
    private volatile Thread thisThread;

    JChatLogger(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        Thread.currentThread().setName(NAME);
        thisThread = Thread.currentThread();

        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter  
            fh = new FileHandler(Utils.getAppDir() + File.separator + "log.txt");
            LOGGER.setLevel(Level.ALL);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            // the following statement is used to log any messages  
            LOGGER.log(Level.INFO, "Logger START\n\n");

        } catch (SecurityException | IOException e) {
            R.log(e.toString());
        }

        needed = true;
        while (needed) {
            try {
                String s = (String) queue.take();
                LOGGER.log(Level.INFO, s);
            } catch (InterruptedException ex) {
                Logger.getLogger(JChatLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("JCHAT LOGGER FINISHED: ");
    }

    public void stop() {
        needed = false;
        thisThread.interrupt();
    }

}
