package GUI;

import GroupManagement.GroupManager;
import GroupManagement.Member;

public class Logic {

    //public Logic logic;
    private GroupManager GM;
    //private StartController sC;
    //private connectController cC;
    //private GuiController gC;


    public Logic(){
        this.GM = new GroupManager();
        //this.sC = new StartController();
        //this.cC = new connectController();
        //this.gC = new GuiController();


        //sC.addObserver(this);
        //cC.addObserver(this);
        //gC.addObserver(this);
        //System.out.println("Adds Obsersers");

        GM.createGroup("Best Group");
        GM.createGroup("Worst Group");
        GM.joinGroup("Best Group", new Member("Dzora","Teg", "90421"));
        GM.joinGroup("Worst Group", new Member("Kalle","08", "noob"));
    }

    public GroupManager getGM() {
        return GM;
    }

    /*@Override
    public void update(Observable obs, Object obj) {
        if(obs.equals(obj) ) {
            this.logic = sC.getStartLogic();
            System.out.println("StartLogic");
        }
        else if(obs.equals(obj)) {
            this.logic = gC.getGUILogic();
            System.out.println("GUILogic");
        }
        else if (obs.equals(obj)) {
            this.logic = cC.getConnectILogic();
            System.out.println("ConnectLogic");
        }
        sC.setLogic(logic);
        cC.setLogic(logic);
        gC.setLogic(logic);
        System.out.println("UPDATE LOGIC");
    }*/


}
