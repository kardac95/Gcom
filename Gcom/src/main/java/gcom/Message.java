package gcom;

import gcom.groupmanagement.Group;
import gcom.groupmanagement.Member;
import gcom.messageordering.VectorClock;

import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private String message;
    private Member sender;
    private Group group;
    private VectorClock vectorClock;

    public Message(Group group, Member sender, String message, String type, VectorClock vectorClock) {
        this.message = message;
        this.sender = sender;
        this.vectorClock = vectorClock;
        this.group = group;
        this.type = type;
    }

    public void setVectorClock(VectorClock vectorClock) {
        this.vectorClock = vectorClock;
    }

    public String getMessage() {
        return message;
    }

    public Member getSender() {
        return sender;
    }

    public String toString() {
        return vectorClock.toString();
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public Group getGroup() {
        return group;
    }

    public String getType() {
        return type;
    }
}