package Gcom.MessageOrdering;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VectorClock {
    String myId;
    ConcurrentHashMap<String, Long> clock;

    public VectorClock(String myId) {
        this.myId = myId;
    }

    public void updateVectorClock(VectorClock receivingClock ) {
        receivingClock.getClock().forEach((key, value) -> {
            Long clockValue = clock.get(key);
            clock.put(key, ((clockValue != null) && (clockValue > value)) ? clockValue:value);
        });
    }

    public void incVectorClock() {
        clock.put(myId, clock.get(myId) + 1);
    }

    public Long getValue(String id) {
        return clock.get(id);
    }

    public void removeKey(String id) {
        clock.remove(id);
    }

    private Map<String, Long> getClock() {
        return clock;
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
}
