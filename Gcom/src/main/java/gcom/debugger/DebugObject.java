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
    public void StopDebugger() {
        debug.setDebug(true);
    }

    @Override
    public void removeMessage(int i) {
        debug.removeMessage(i);
    }

    @Override
    public void moveMessage(int src, int dest) {
        debug.moveMessage(src, dest);
    }

    @Override
    public Message getNextMessage() {
        return debug.getNextMessage();
    }

    @Override
    public List getDebugBuffer() {
        return debug.getDebugBuffer();
    }

    @Override
    public void play() {
        debug.play();
    }

    @Override
    public void stop() {
        debug.setDebug(true);
    }

    @Override
    public void step() {
        debug.step();
    }

    @Override
    public Thread monitorDebugBuffer(Runnable updateFunction) {
        return debug.monitorDebugBuffer(updateFunction);
    }
}
