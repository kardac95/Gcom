package gcom.debugger;

import gcom.Message;

public interface Debug {

    void StartDebugger();
    void StopDebugger();
    void removeMessage(int i);
    void moveMessage(int src, int dest);
    Message getNextMessage();
    void play();
    void stop();
    void step();

}
