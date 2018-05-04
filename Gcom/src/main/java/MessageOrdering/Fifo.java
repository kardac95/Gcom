package MessageOrdering;

import GroupManagement.Group;
import GroupManagement.Member;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;
import java.util.List;

public class Fifo implements Ordering {
    private List<Integer> vectorClock;
    private Integer clockIndex;
    private ArrayQueue<Message> sendQueue;

    public Fifo() {
        this.vectorClock = new ArrayList<>();
        vectorClock.add(0);
        this.clockIndex = 0;

        this.sendQueue = new ArrayQueue<>(10);
    }

    @Override
    public void messageGroup(Group group, Member sender, String message) {
        Message letter = new Message(group, sender.getName(), message, vectorClock);
        sendQueue.add(letter);
    }

    private void setVectorClock(List vectorClock, Integer index) {
        this.vectorClock = vectorClock;
        clockIndex = index;
    }

    public Message getNextMessage() {
        return sendQueue.remove(0);
    }
}
