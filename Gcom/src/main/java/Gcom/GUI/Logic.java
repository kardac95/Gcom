package Gcom.GUI;

import Gcom.GroupManagement.GroupManager;
import Gcom.GroupManagement.Member;
import Gcom.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class Logic {

    private GroupManager GM;
    private String userName;
    private String port;
    private String localIp;

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

        this.GM = new GroupManager(new Member(userName, localIp, port));

    }

    public GroupManager getGM() {
        return GM;
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

    public Runnable updateTask(GuiController g) {
        return () -> {
            while(true) {
                Message m;
                try {
                    m = ((LinkedBlockingQueue<Message>)this.GM.getOutgoingQueue()).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                g.updateTree(this);
                g.addGroupTab();
            }
        };
    }

}
