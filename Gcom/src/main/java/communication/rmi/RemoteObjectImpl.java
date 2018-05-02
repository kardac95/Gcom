package communication.rmi;

import java.rmi.*;
import java.rmi.server.*;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {

    public RemoteObjectImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean printMessage(String msg) {
        System.out.println(msg);
        return true;
    }

}
