package gcom.debugger;

import gcom.Message;
import gcom.communication.Communication;

public class DebugObject implements Debug{
    Debugger debug;

    public DebugObject(Communication comm) {
        debug = new Debugger(comm);
    }

    @Override
    public void StartDebugger() {
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
    public void play() {
        debug.setPlay(true);
    }

    @Override
    public void stop() {
        debug.setPlay(false);
    }

    @Override
    public void step() {
        debug.step();
    }
}
