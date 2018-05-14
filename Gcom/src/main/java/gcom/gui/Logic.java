package gcom.gui;

import gcom.groupmanagement.GroupManager;
import gcom.groupmanagement.Member;
import gcom.Message;
import javafx.application.Platform;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class Logic {

    private GroupManager GM;
    private String userName;
    private String port;
    private String localIp;
    private Member me;

    public Logic(String userName, String port){
        this.userName = userName;
        this.port = port;

        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            localIp = ipAddr.getHostAddress();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        me = new Member(userName, localIp, port);

        this.GM = new GroupManager(me);

    }

    public GroupManager getGM() {
        return GM;
    }

    public Member getMe() {
        return me;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName ;
    }

    public String getLocalIp() {
        return localIp;
    }

    public Thread updateTask(GuiController g) {
        return new Thread(() -> {
            while(true) {
                Message m = null;
                try {
                    m = ((LinkedBlockingQueue<Message>)this.GM.getOutgoingQueue()).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Update!");
                Platform.runLater(g::updateTree);
                final Message message = m;
                Platform.runLater(() -> {
                    g.setTextInTextFlow(message);
                });

            }
        });
    }

}
