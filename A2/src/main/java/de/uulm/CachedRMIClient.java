package de.uulm;

import de.uulm.interfaces.ISubscribeKVStore;
import de.uulm.interfaces.ISubscriber;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class CachedRMIClient extends UnicastRemoteObject implements ISubscriber {

    private ISubscribeKVStore subRMIKVStore;
    private final HashMap<String, String> cache;

    public CachedRMIClient(String host, int port) throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            subRMIKVStore = (ISubscribeKVStore) registry.lookup("SubRMIKVStore");
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
        cache = new HashMap<>();
    }

    public void write(String key, String value) {
        try {
            subRMIKVStore.writeRemote(key, value);
            cache.put(key, value);
            subRMIKVStore.subscribe(key, this);
            System.out.println("Written: " + key + " -> " + value);
        } catch (RemoteException e) {
            System.err.println("Write exception: " + e.getMessage());
        }
    }

    public void remove(String key) {
        cache.remove(key);
        try {
            subRMIKVStore.unsubscribe(key, this);
            subRMIKVStore.removeRemote(key);
            System.out.println("Removed: " + key);
        } catch (RemoteException e) {
            System.err.println("Remove exception: " + e.getMessage());
        }
    }

    public String read(String key) {
        try {
            if (cache.containsKey(key)) {
                return cache.get(key);
            } else {
                String value = subRMIKVStore.readRemote(key);
                if (value != null) {
                    cache.put(key, value);
                    subRMIKVStore.subscribe(key, this);
                }
                return value;
            }
        } catch (RemoteException e) {
            System.err.println("Read exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateEntry(String key, String value) throws RemoteException {
        if (!cache.containsKey(key)) {
            throw new RemoteException("Key not found in cache");
        }
        cache.put(key, value);
        System.out.println("Cache updated for key: " + key + " with value: " + value);
    }

    @Override
    public void removeEntry(String key) throws RemoteException {
        if (!cache.containsKey(key)) {
            throw new RemoteException("Key not found in cache");
        }
        cache.remove(key);
        System.out.println("Cache entry removed for key: " + key);
    }
}
