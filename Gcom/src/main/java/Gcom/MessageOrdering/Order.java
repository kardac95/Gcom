package Gcom.MessageOrdering;

import Gcom.GroupManagement.Group;
import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.Communication;
import Gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Order {
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;
    private ArrayBlockingQueue<Message> outgoingQueue;
    private ArrayBlockingQueue<Message> incomingQueue;
    private Communication comm;


    public Order(Member myInfo) {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.outgoingQueue = new ArrayBlockingQueue<>(10);
        this.incomingQueue = new ArrayBlockingQueue<>(20);

        comm = new CommunicationObject();
        comm.initCommunication(myInfo);
    }

    public void messageGroup(Group group, Member sender, String message){
        vectorClock.set(memberIndex, vectorClock.get(memberIndex) + 1);
        outgoingQueue.add(new Message(group, sender, message, "message", vectorClock));

    }

    public void setVectorClock(ArrayList<Integer> vectorClock, int memberIndex){
        this.vectorClock = vectorClock;
        this.memberIndex= memberIndex;
    }

    public void incrementVectorClock(int index) {
        vectorClock.set(index, vectorClock.get(index) + 1);
    }

    public Message getNextOutgoingMessage() {
        return outgoingQueue.remove();
    }

    public void addNextIncomingMessage(Message message) {
        incomingQueue.add(message);
    }

    public Message getNextIncommingMessage() {
        return incomingQueue.remove();
    }
}

