package de.uulm;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {

        CachedRMIClient client1;
        CachedRMIClient client2;
        SubRMIKVStore subRMIKVStore;
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry gestartet auf Port 1099");

            subRMIKVStore = new SubRMIKVStore();
            registry.rebind("SubRMIKVStore", subRMIKVStore);

            client1 = new CachedRMIClient("localhost", 1099);
            client2 = new CachedRMIClient("localhost", 1099);
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            return;
        }

        client1.write("key1", "value1");
        System.out.println("Client1 wrote key1: value1");

        String value = client2.read("key1");
        System.out.println("Client2 read key1: " + value);

        client1.write("key1", "value2");
        System.out.println("Client1 updated key1 to: value2");

        value = client2.read("key1");
        System.out.println("Client2 read updated key1 from cache: " + value);

        client1.remove("key1");
        System.out.println("Client1 removed key1");


        value = client2.read("key1");
        System.out.println("Client2 read key1 after removal: " + value);

        try {
            UnicastRemoteObject.unexportObject(subRMIKVStore, true);
        } catch (Exception e) {
            System.err.println("Failed to unbind SubRMIKVStore");
        }
    }
}