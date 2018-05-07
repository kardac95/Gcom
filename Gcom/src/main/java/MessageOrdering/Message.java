package MessageOrdering;

import GroupManagement.Group;
import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private String message;
    private String sender;
    private Group group;
    private List<Integer> vectorClock;


    public Message(Group group, String sender, String message, List<Integer> vectorClock) {
        this.message = message;
        this.sender = sender;
        this.vectorClock = vectorClock;
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public List getVectorClock() {
        return vectorClock;
    }

    public Group getGroup() {
        return group;
    }
}
