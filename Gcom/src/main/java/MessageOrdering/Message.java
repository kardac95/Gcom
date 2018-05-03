package MessageOrdering;

import javafx.util.Pair;

public class Message {
    private Pair<VectorClock,String > message;

    public Message(){
        VectorClock vc = new VectorClock();
        this.message = new Pair<>(vc,"");
    }

    public String getMessage() {
        return message.getValue();
    }

    public VectorClock getVectorClock() {
        return message.getKey();
    }

    public void setMessage(Pair<VectorClock, String> message) {
        this.message = message;
    }
}
