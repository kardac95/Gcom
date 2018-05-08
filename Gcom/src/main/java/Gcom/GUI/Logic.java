package Gcom.GUI;

import Gcom.GroupManagement.GroupManager;
import Gcom.GroupManagement.Member;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Logic {

    private GroupManager GM;
    private String userName;
    private String port;
    private String localIp;


    public Logic(){
        this.GM = new GroupManager();

        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            localIp = ipAddr.getHostAddress();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
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
        this.userName = userName;
    }

    public void initCommunication(Member member) {
        GM.initOrdering(member);
    }


    public String getLocalIp() {
        return localIp;
    }
}