package GroupManagement;

import java.util.HashMap;

public class GroupManager {
    //variable:groups
    private HashMap<String, Group> groups;

    public GroupManager() {
        this.groups = new HashMap<>();
    }

    //method:createGroup
    public void createGroup(String name) {
        groups.put(name, new Group(name));
    }
    public void createGroup(String name, HashMap<String, Member> members) {
        groups.put(name, new Group(name, members));
    }

    //method:removeGroup
    public void removeGroup(String name) {
        groups.remove(name);
    }

    //method:joinGroup
    public void joinGroup() {
        
    }
}
