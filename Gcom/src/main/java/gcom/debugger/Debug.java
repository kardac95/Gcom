package gcom.debugger;

import gcom.Message;

import java.util.List;

public interface Debug {

    void startDebugger();
    void StopDebugger();
    void removeMessage(int i);
    void moveMessage(int src, int dest);
    Message getNextMessage();
    List getDebugBuffer();
    void play();
    void stop();
    void step();
    Thread monitorDebugBuffer(Runnable updateFunction);

}
