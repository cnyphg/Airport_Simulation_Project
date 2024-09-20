package Assignment_5;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Refill extends Thread {
    private int planeID;

    public Refill(int planeID) {
        setName("Thread-Supply-Truck");
        this.planeID = planeID;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Refilling supplies and cleaning for Plane " + planeID + "...");
        try {
            Thread.sleep(2000); // Supply refill takes 2 seconds.
        } catch (InterruptedException e) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println(Thread.currentThread().getName() + ": Plane " + planeID + "'s refilling supplies and cleaning has completed!");
    }
}
