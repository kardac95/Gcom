package gcom.debugger;

import gcom.Message;
import gcom.communication.Communication;

import java.util.List;

public class DebugObject implements Debug {
    Debugger debug;

    public DebugObject(Communication comm) {
        debug = new Debugger(comm);
    }

    @Override
    public void startDebugger() {
        debug.setDebug(true);
    }

    @Override
    public void stopDebugger() {
        debug.setDebug(false);
    }

    @Override
    public void removeMessage(String group, int i) {
        debug.removeMessage(group, i);
    }

    @Override
    public void moveMessage(String group, int src, int dest) {
        debug.moveMessage(group, src, dest);
    }

    @Override
    public Message getNextMessage() {
        return debug.getNextMessage();
    }

    @Override
    public List<Message> getDebugBuffer(String group) {
        return debug.getDebugBuffer(group);
    }

    @Override
    public void play(String group) {
        debug.play(group);
    }

    @Override
    public void stop() {
        debug.setDebug(true);
    }

    @Override
    public void step(String group) {
        debug.step(group);
    }

    @Override
    public Thread monitorDebugBuffer(Runnable updateFunction) {
        return debug.monitorDebugBuffer(updateFunction);
    }
}
