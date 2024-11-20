package de.uulm;

import de.uulm.interfaces.ISubscribeKVStore;
import de.uulm.interfaces.ISubscriber;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubRMIKVStore extends UnicastRemoteObject implements ISubscribeKVStore {

    private final Map<String, String> store;
    private final Map<String, Set<ISubscriber>> subscriptions;

    protected SubRMIKVStore() throws RemoteException {
        super();
        store = new HashMap<>();
        subscriptions = new HashMap<>();
    }

    @Override
    public void subscribe(String key, ISubscriber sub) throws RemoteException {
        if (!store.containsKey(key)) {
            throw new RemoteException("Key not found");
        }
        subscriptions.computeIfAbsent(key, _ -> new HashSet<>()).add(sub);
    }

    @Override
    public void unsubscribe(String key, ISubscriber sub) throws RemoteException {
        if (!store.containsKey(key)) {
            throw new RemoteException("Key not found");
        }
        if (subscriptions.containsKey(key)) {
            subscriptions.get(key).remove(sub);
            if (subscriptions.get(key).isEmpty()) {
                subscriptions.remove(key);
            }
        }
    }

    @Override
    public String readRemote(String key) throws RemoteException {
        return store.get(key);
    }

    @Override
    public void writeRemote(String key, String value) throws RemoteException {
        store.put(key, value);
        // Notify subscribers
        if (subscriptions.containsKey(key)) {
            Set<ISubscriber> subs = subscriptions.get(key);
            for (ISubscriber sub : subs) {
                try {
                    sub.updateEntry(key, value);
                } catch (RemoteException e) {
                    System.err.println("Write exception: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void removeRemote(String key) throws RemoteException {
        store.remove(key);
        if (subscriptions.containsKey(key)) {
            Set<ISubscriber> subs = subscriptions.get(key);
            for (ISubscriber sub : subs) {
                try {
                    sub.removeEntry(key);
                } catch (RemoteException e) {
                    System.err.println("Remove exception: " + e.getMessage());
                }
            }
        }
    }
}
