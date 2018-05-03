package MessageOrdering;

import GroupManagement.Member;
import javafx.util.Pair;

import java.util.Vector;

public class VectorClock {

    private Pair<Member,Integer> vc;

    private int time;
    public Vector vector;

    public VectorClock(){
        time = 0;
        vector = new Vector(10,2);
    }
}
