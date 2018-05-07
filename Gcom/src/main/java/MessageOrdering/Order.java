package MessageOrdering;

import GroupManagement.Group;
import GroupManagement.Member;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Order {
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;
    private ArrayBlockingQueue<Message> outgoingQueue;
    private ArrayBlockingQueue<Message> incomingQueue;


    public Order() {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.outgoingQueue = new ArrayBlockingQueue<>(10);
        this.incomingQueue = new ArrayBlockingQueue<>(20);
    }

    void messageGroup(Group group, Member sender, String message){
        vectorClock.set(memberIndex, vectorClock.get(memberIndex) + 1);
        outgoingQueue.add(new Message(group, sender.getName(), message, vectorClock));

    }

    void setVectorClock(ArrayList<Integer> vectorClock, int memberIndex){
        this.vectorClock = vectorClock;
        this.memberIndex= memberIndex;
    }

    void incrementVectorClock(int index) {
        vectorClock.set(index, vectorClock.get(index) + 1);
    }

    public Message getNextOutgoingMessage() {
        return outgoingQueue.remove();
    }

    public Message getNextIncommingMessage() {
        return incomingQueue.remove();
    }
}

