package gcom.messageordering;

import gcom.groupmanagement.Member;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VectorClock implements Serializable {
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

    public void inc() {
        clock.put(myId, clock.get(myId) + 1);
    }

    public Long getValue(String id) {
        return clock.get(id);
    }

    public void remove(String id) {
        clock.remove(id);
    }

    private Map<String, Long> getClock() {
        return clock;
    }

    public void aidsMethod(Member[] kyslords) {
        Arrays.stream(kyslords).forEach(kyslord -> {
            if(!clock.containsKey(kyslord.getAddress()+kyslord.getPort())) {
                clock.put(kyslord.getAddress()+kyslord.getPort(), 0L);
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

        Set keySet = other.clock.keySet();

        for (Object key : keySet) {
            int cmp = Long.compare(clock.get(key), other.clock.get(key));
            if (cmp > 0)
                return false;
            else if (cmp < 0)
                isBefore = true;
        }

        return isBefore;
    }
}
