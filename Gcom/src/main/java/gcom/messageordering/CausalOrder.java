package gcom.messageordering;

import gcom.Message;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class CausalOrder extends Order {

    public CausalOrder(String myId) {
        super(myId);
    }

    @Override
    public void Ordering(Message data, Queue<Message> outQueue) {
        if(data.getType().equals("message")) {
            if(this.clock.isBefore(data.getVectorClock())) {

                if(this.clock.getValue(data.getVectorClock().getMyId()) == data.getVectorClock().getValue(data.getVectorClock().getMyId()) - 1) {
                    outQueue.add(data);
                    this.clock.updateVectorClock(data.getVectorClock());
                    checkBuffer(outQueue);
                } else {
                    this.buffer.add(data);
                    sortBuffer();
                }

            } else {
                outQueue.add(data);
                this.clock.updateVectorClock(data.getVectorClock());
                checkBuffer(outQueue);
            }
        } else {
            outQueue.add(data);
        }
    }

    private void checkBuffer(Queue<Message> outQueue) {
        while(!this.buffer.isEmpty()) {
            Message first = this.buffer.get(0);
            if(this.clock.getValue(first.getVectorClock().getMyId()) ==
                    first.getVectorClock().getValue(first.getVectorClock().getMyId()) - 1) {

                outQueue.add(first);
                this.buffer.remove(0);
            } else {
                break;
            }
        }
    }

    private boolean isNext() {
        if(!this.buffer.isEmpty()) {
            Message first = this.buffer.get(0);
            //first.getVectorClock().getClock().
        }
        return false;
    }

    private void sortBuffer() {
        buffer.sort((m1, m2) -> (m1.getVectorClock().isBefore(m2.getVectorClock()) ? -1:1));
    }
}