package gcom.debugger;

import gcom.Message;

import java.util.List;

public interface Debug {

    void startDebugger();
    void stopDebugger();
    void removeMessage(String group, int i);
    void moveMessage(String group, int src, int dest);
    Message getNextMessage();
    List getDebugBuffer(String group);
    void play(String group);
    void stop();
    void step(String group);
    Thread monitorDebugBuffer(Runnable updateFunction);

    void setOrderBuffer(String name, List<Message> buffer);
    List <Message> getOrderBuffer(String groupName);
}
