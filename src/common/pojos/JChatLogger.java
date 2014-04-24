package common.pojos;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JChatLogger implements Runnable, Observer {

    private OQueue logs;
    private final static Logger LOGGER = Logger.getLogger(JChatLogger.class.getSimpleName());

    public JChatLogger() {
        logs = new OQueue(this);
        LOGGER.setLevel(Level.ALL);
    }

    @Override
    public void update(Observable o, Object arg) {
        //System.out.println("Update of logs called");
        OQueue q = (OQueue) o;
        String s = (String) q.poll();
        LOGGER.log(Level.INFO, s + "\n");
    }

    public void log(String s) {
        logs.offer(s);
        //System.out.println("Offered log message");
    }

    @Override
    public void run() {

    }
}
