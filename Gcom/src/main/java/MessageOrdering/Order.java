package MessageOrdering;

import GroupManagement.Group;
import GroupManagement.Member;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;

public class Order {
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;

    private ArrayQueue<Message> outgoingQueue;
    private ArrayQueue<Message> incomingQueue;

    public Order() {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.outgoingQueue = new ArrayQueue<>(10);
        this.incomingQueue = new ArrayQueue<>(20);
    }

    void messageGroup(Group group, Member sender, String message){
        vectorClock.set(memberIndex, vectorClock.get(memberIndex) + 1);
        outgoingQueue.add(new Message(group, sender.getName(), message, vectorClock));
    }

    void setVectorClock(ArrayList<Integer> vectorClock, Integer memberIndex){
        this.vectorClock = vectorClock;
        this.memberIndex= memberIndex;
    }

    public Message getNextOutgoingMessage() {
        return outgoingQueue.remove(0);
    }

    public Message getNextIncommingMessage() {
        return incomingQueue.remove(0);
    }
}

