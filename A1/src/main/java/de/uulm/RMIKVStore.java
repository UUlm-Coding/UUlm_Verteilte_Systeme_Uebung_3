package de.uulm;

import de.uulm.interfaces.IRemoteKVStore;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIKVStore extends UnicastRemoteObject implements IRemoteKVStore {

    HashMap<String, String> store;

    public RMIKVStore() throws RemoteException {
        super();
        Registry registry = LocateRegistry.createRegistry(1099);

        registry.rebind("RemoteKVStore", this);
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
