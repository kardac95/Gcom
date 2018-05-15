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
        if(this.clock.isBefore(data.getVectorClock())) {

            if(this.clock.getValue(data.getVectorClock().getMyId()) == data.getVectorClock().getValue(data.getVectorClock().getMyId()) - 1) {
                outQueue.add(data);
                this.clock.updateVectorClock(data.getVectorClock());
            } else {
                this.buffer.add(data);
                sortBuffer();
            }

        } else {
            outQueue.add(data);
            this.clock.updateVectorClock(data.getVectorClock());
        }
    }

    private synchronized void sortBuffer() {
        new Thread(() -> {
            buffer.sort((m1, m2) -> (m1.getVectorClock().isBefore(m2.getVectorClock()) ? -1:1));

        }).start();
    }


}
