package Gcom.GUI;

import Gcom.GroupManagement.GroupManager;
import Gcom.GroupManagement.Member;

public class Logic {

    private GroupManager GM;
    private String userName;
    private String port;


    public Logic(){
        this.GM = new GroupManager();
        GM.createGroup("Best Group");
        GM.createGroup("Worst Group");
        GM.joinGroup("Best Group", new Member("Dzora","Teg", "90421"));
        GM.joinGroup("Worst Group", new Member("Kalle","08", "noob"));
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
}
