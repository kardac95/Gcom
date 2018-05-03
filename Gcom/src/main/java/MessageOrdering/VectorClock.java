package MessageOrdering;

import java.util.Vector;

public class VectorClock {

    private int time;
    public Vector vector;

    public VectorClock(){
        time = 0;
        vector = new Vector(10,2);
    }
}
