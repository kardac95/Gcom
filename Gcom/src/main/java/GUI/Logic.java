package GUI;

import GroupManagement.GroupManager;
import GroupManagement.Member;

public class Logic {

    private GroupManager GM;

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

}
