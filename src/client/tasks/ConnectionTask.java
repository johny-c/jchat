/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.tasks;

import client.networking.NetworkManager;
import client.networking.R;
import common.utils.Conventions;
import common.utils.Utils;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author johny
 */
public class ConnectionTask extends SwingWorker<Boolean, String> implements Conventions {

    private final JLabel informativeLabel;

    public ConnectionTask(JLabel informingLabel) {
        informativeLabel = informingLabel;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        Thread.currentThread().setName("CONNECTION TASK");
        R.log("executing");

        publish("Connecting to Server");

        Utils.printThreads();

        return NetworkManager.connect();
        /*
         if (NetworkManager.isConnected()) {
         publish("Sending request");
         NetworkManager.send(firstMessage);
         publish("Waiting for response");
         }

         return null;
         */
    }

    @Override
    protected void process(List<String> guiUpdates) {
        informativeLabel.setText(guiUpdates.get(guiUpdates.size() - 1));
    }

    @Override
    protected void done() {

        //informativeLabel.setVisible(false);
    }

}
