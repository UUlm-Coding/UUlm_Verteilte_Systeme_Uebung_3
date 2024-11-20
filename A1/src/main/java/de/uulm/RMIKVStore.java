package de.uulm;

import de.uulm.interfaces.IRemoteKVStore;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIKVStore extends UnicastRemoteObject implements IRemoteKVStore {

    HashMap<String, String> store;

    protected RMIKVStore() throws RemoteException {
        super();
        store = new HashMap<>();
    }


    @Override
    public String readRemote(String key) throws RemoteException {
        if (!store.containsKey(key)) {
            throw new RemoteException("Key not found: " + key);
        }
        return store.get(key);
    }

    @Override
    public void writeRemote(String key, String value) throws RemoteException{
        store.put(key, value);
    }

    @Override
    public void removeRemote(String key) throws RemoteException{
        if (!store.containsKey(key)) {
            throw new RemoteException("Key not found: " + key);
        }
        store.remove(key);
    }
}
