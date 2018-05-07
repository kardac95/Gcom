package Gcom;

import Gcom.GroupManagement.Group;
import Gcom.GroupManagement.Member;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private String type;
    private String message;
    private Member sender;
    private Group group;
    private List<Integer> vectorClock;


    public Message(Group group, Member sender, String message, String type, List<Integer> vectorClock) {
        this.message = message;
        this.sender = sender;
        this.vectorClock = vectorClock;
        this.group = group;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Member getSender() {
        return sender;
    }

    public List getVectorClock() {
        return vectorClock;
    }

    public Group getGroup() {
        return group;
    }

    public String getType() {
        return type;
    }
}