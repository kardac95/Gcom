package gcom.messageordering;

import gcom.groupmanagement.Member;

import java.io.Serializable;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VectorClock implements Serializable, Cloneable {
    private String myId;
    private ConcurrentHashMap<String, Long> clock;

    public VectorClock(String myId) {
        this.myId = myId;
        this.clock = new ConcurrentHashMap<>();
        this.clock.put(myId, 0L);
    }

    public void updateVectorClock(VectorClock receivingClock ) {
        clock.put(receivingClock.myId, receivingClock.getValue(receivingClock.myId));

    }

    public String getMyId() {
        return myId;
    }

    public String toString() {
        return clock.values().toString();
    }

    public void inc() {
        clock.put(myId, clock.get(myId) + 1);
    }

    public Long getValue(String id) {
        return clock.get(id);
    }

    public void remove(String id) {
        clock.remove(id);
    }

    public Map<String, Long> getClock() {
        return clock;
    }

    public void addNewMemberClock(Member[] newMembers, VectorClock newClock) {
        Arrays.stream(newMembers).forEach(m -> {
            if(!clock.containsKey(m.getAddress()+m.getPort())) {
                clock.put(m.getAddress()+m.getPort(), newClock.getValue(m.getAddress()+m.getPort()));
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        Map <String, Long> cmpVector = ((VectorClock)o).getClock();
        for (String k: cmpVector.keySet()) {
            if(!clock.get(k).equals(cmpVector.get(k))) {
                return false;
            }
        }
        return true;
    }

    /* True if clock is before other, otherwise false */
    public boolean isBefore(VectorClock other) {
        boolean isBefore = false;

        for (String key : other.clock.keySet()) {
            System.err.println(key);
            int cmp = Long.compare(clock.get(key), other.clock.get(key));
            if (cmp > 0)
                return false;
            else //if (cmp < 0)
                isBefore = true;
        }

        return isBefore;
    }
    @Override
    public VectorClock clone() {
        final VectorClock clone;
        try {
            clone =(VectorClock) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new Error("Something impossible just happened");
        }
        clone.clock = new ConcurrentHashMap<>(this.clock);
        clone.myId = this.myId;
        return clone;
    }

}
